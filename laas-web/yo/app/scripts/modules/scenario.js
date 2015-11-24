LaaS.module('Scenario', function(Scenario, LaaS, Backbone, Marionette) {
    'use strict';

    var ScenarioView = Marionette.ItemView.extend({
        initialize : function(options){
            this.scenario = options.model.attributes;
            this.productList = options.productList;
            this.workflows = options.workflows;
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
            data.scenario.workflows = this.workflows;
            data.scenario.selectedProduct = this.selectedProduct;
            data.scenario.fileTypes = this.fileTypes;
            data.scenario.parameterDefines = this.parameterDefines;
            return data;
        },
        onRender: function() {
            var that = this;
            this.$('select').dropdown();

            if (this.workflows && this.workflows.length > 0 ) {
                var size = 10;
                var currentWorkflows = [];
                for (var i=0; i<size && i<this.workflows.length; i++) {
                    currentWorkflows.push(this.workflows[i]);
                }
                var template = JST['app/handlebars/scenario/workflow'];
                var subHtml = template({workflows:currentWorkflows});
                this.$('#workflows').html(subHtml);
                var totalPages = Math.floor(this.workflows.length / size) + 1;
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
                            var currentWorkflows = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.workflows[i]) {
                                    break;
                                }
                                currentWorkflows.push(that.workflows[i]);
                            }
                            var template = JST['app/handlebars/scenario/workflow'];
                            var html = template({workflows: currentWorkflows});
                            $('#workflows').html(html);
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
                $.when(LaaS.request('workflow:entitiesByUrl', {'url': scenarioModel.attributes._links.workflows.href}),
                    LaaS.request('product:entityByUrl', {'url':scenarioModel.attributes._links.product.href}),
                    LaaS.request('fileType:entitiesByUrl', {'url':scenarioModel.attributes._links.fileTypes.href}),
                    LaaS.request('parameterDefine:entitiesByUrl', {'url':scenarioModel.attributes._links.parameterDefines.href}))
                    .done(function(relatedWorkflows, selectedProduct, selectedFileTypes, selectedParameterDefines) {
                    var view = new ScenarioView({model:scenarioModel, productList:productList.products,
                        workflows:relatedWorkflows.workflows, selectedProduct:selectedProduct.product, fileTypes:selectedFileTypes.fileTypes,
                        parameterDefines:selectedParameterDefines.parameterDefines});
                    LaaS.mainRegion.show(view);
                })
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
