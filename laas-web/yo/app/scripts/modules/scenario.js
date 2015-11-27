LaaS.module('Scenario', function(Scenario, LaaS, Backbone, Marionette) {
    'use strict';

    var ScenarioView = Marionette.ItemView.extend({
        initialize : function(options){
            this.scenario = options.model.attributes;
            this.productList = options.productList;
            this.tasks = options.tasks;
            this.selectedProduct = options.selectedProduct;
            this.fileTypes = options.fileTypes;
            this.parameterDefines = options.parameterDefines;
        },
        template : function(data){
            var template = JST['app/handlebars/scenario/detail'];
            var html = template(data.scenario);
            return html;
        },
        serializeData:function(){
            var data =  {scenario:this.scenario};
            data.scenario.productList = this.productList;
            data.scenario.tasks = this.tasks;
            data.scenario.selectedProduct = this.selectedProduct;
            data.scenario.fileTypes = this.fileTypes;
            data.scenario.parameterDefines = this.parameterDefines;
            return data;
        },
        onRender: function() {
            var that = this;
            this.$('select').dropdown();

            if (this.tasks && this.tasks.length > 0 ) {
                var size = 10;
                var currentTasks = [];
                for (var i=0; i<size && i<this.tasks.length; i++) {
                    currentTasks.push(this.tasks[i]);
                }
                var template = JST['app/handlebars/scenario/task'];
                var subHtml = template({tasks:currentTasks});
                this.$('#tasks').html(subHtml);
                var totalPages = Math.floor(this.tasks.length / size) + 1;
                if (totalPages > 1) {
                    this.$('#paging').twbsPagination({
                        totalPages: totalPages,
                        startPage: 1,
                        visiblePages: 6,
                        first: '<<',
                        prev: '<',
                        next: '>',
                        last: '>>',
                        onPageClick: function (event, page) {
                            var currentTasks = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.tasks[i]) {
                                    break;
                                }
                                currentTasks.push(that.tasks[i]);
                            }
                            var template = JST['app/handlebars/scenario/task'];
                            var html = template({tasks: currentTasks});
                            $('#tasks').html(html);
                        }
                    })
                }
            }
        },
        events: {
            'click #scenario_save': 'saveScenario',
            'click #scenario_cancel': 'cancelScenario'
        },
        cancelScenario: function() {
            window.history.back();
        },
        saveScenario: function() {

        }
    });

    var ScenarioListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.scenarios = options.scenarios;
        },
        template : function(data){
            var template = JST['app/handlebars/scenario/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {scenarios:this.scenarios};
        },
        events:{
            'click button.scenario-show': "showClicked"
        },
        showClicked: function(event){
            var scenarioId = event.target.dataset["id"];
            LaaS.navigate('/scenarios/'+scenarioId, true);
        }
    });

    var ScenarioController = Marionette.Controller.extend({
        showScenarios: function() {
            $.when(LaaS.request('scenario:entities')).done(function(data){
               var view = new ScenarioListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showScenario: function(id){
            $.when(LaaS.request('scenario:entity', {'id':id}), LaaS.request('product:entities')).done(function(scenarioModel, productList){
                $.when(LaaS.request('orderedTask:entitiesByUrl', {'url': scenarioModel.attributes._links.orderedTasks.href}),
                    LaaS.request('product:entityByUrl', {'url':scenarioModel.attributes._links.product.href}),
                    LaaS.request('fileType:entitiesByUrl', {'url':scenarioModel.attributes._links.fileTypes.href}),
                    LaaS.request('parameterDefine:entitiesByUrl', {'url':scenarioModel.attributes._links.parameterDefines.href}))
                    .done(function(orderedTasks, selectedProduct, selectedFileTypes, selectedParameterDefines) {
                        orderedTasks.tasks = orderedTasks.tasks.sort(function(a, b) {
                            if (a.order < b.order) {
                                return -1;
                            } else if (a.order > b.order) {
                                return 1;
                            }
                            return 0;
                        });
                        var groupUrl = [];
                        for (var i=0; i<orderedTasks.tasks.length; i++) {
                            groupUrl.push(orderedTasks.tasks[i]._links.task.href);
                        }
                        $.when(LaaS.request('task:entitiesByGroupEntityUrl', {'groupUrl':groupUrl})).done(function(tasks) {
                            for (var i=0; i<tasks.tasks.length; i++) {
                                $.extend(orderedTasks.tasks[i], tasks.tasks[i]);
                            }
                            var view = new ScenarioView({model:scenarioModel, productList:productList.products,
                                tasks:orderedTasks.tasks, selectedProduct:selectedProduct.product, fileTypes:selectedFileTypes.fileTypes,
                                parameterDefines:selectedParameterDefines.parameterDefines});
                            LaaS.mainRegion.show(view);
                        });
                });
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'scenarios(/)': 'showScenarios',
                'scenarios/:id(/)' : 'showScenario'
            },
            controller: new ScenarioController()
        });
    });
});
