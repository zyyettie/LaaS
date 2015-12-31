LaaS.module('ParameterDef', function (ParameterDef, LaaS, Backbone, Marionette) {
    'use strict';


    var InputParameterDefView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.inputParameterDef = options.inputParameterDef;
            this.tasks = options.tasks;
            this.scenarios = options.scenarios;
        },
        template: function (data) {
            var template = JST['app/handlebars/inputParameterDef/detail'];
            var html = template(data.inputParameterDef);
            return html;
        },
        serializeData: function () {
            var typeList = [{type:"text"}, {type:"dropdown"}, {type:"time"}];
            for (var i=0; i<typeList.length; i++ ) {
                if (typeList[i].type == this.inputParameterDef.type) {
                    typeList[i].selected = "selected";
                }
            }
            this.inputParameterDef.typeList = typeList;
            return {inputParameterDef: this.inputParameterDef};
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
                var template = JST['app/handlebars/inputParameterDef/task'];
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
                            var template = JST['app/handlebars/inputParameterDef/task'];
                            var html = template({tasks: currentTasks});
                            $('#tasks').html(html);
                        }
                    })
                }
            }

            if (this.scenarios && this.scenarios.length > 0 ) {
                var size = 10;
                var currentScenarios = [];
                for (var i=0; i<size && i<this.scenarios.length; i++) {
                    currentScenarios.push(this.scenarios[i]);
                }
                var template = JST['app/handlebars/inputParameterDef/scenario'];
                var subHtml = template({scenarios:currentScenarios});
                this.$('#scenarios').html(subHtml);
                var totalPages = Math.floor(this.scenarios.length / size) + 1;
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
                            var currentScenarios = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.scenarios[i]) {
                                    break;
                                }
                                currentScenarios.push(that.scenarios[i]);
                            }
                            var template = JST['app/handlebars/inputParameterDef/scenario'];
                            var html = template({scenarios: currentScenarios});
                            $('#scenarios').html(html);
                        }
                    })
                }
            }
        }
    });

    var InputParameterDefListView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.inputParameterDefs = options.inputParameterDefs;
        },
        template: function(data) {
            var template = JST['app/handlebars/inputParameterDef/list'];
            var html = template(data);
            return html;
        },
        serializeData: function() {
            return {inputParameterDefs: this.inputParameterDefs};
        },
        events:{
            'click button.inputParameterDef-show': "showClicked"
        },
        showClicked: function(event){
            var inputParameterDefId = event.target.dataset["id"];
            LaaS.navigate('/inputParameterDefs/'+inputParameterDefId,true);
        }
    });

    var InputParameterDefController = Marionette.Controller.extend({
        showInputParameterDef: function (id) {
            $.when(LaaS.request('inputParameterDef:entity', {'id': id})).done(function (data) {
                if (data.attributes._links.task) {
                    $.when(LaaS.request('task:entityByUrl', {url:data.attributes._links.task.href}),
                        LaaS.request('scenario:entitiesByUrl', {url:data.attributes._links.scenarios.href}))
                        .done(function(task, scenarios) {
                        var view = new InputParameterDefView({inputParameterDef:data.attributes, tasks:[task], scenarios:scenarios.scenarios});
                        LaaS.mainRegion.show(view);
                    });
                } else {
                    var view = new InputParameterDefView({inputParameterDef:data.attributes, tasks:[]});
                    LaaS.mainRegion.show(view);
                }
            });
        },
        showInputParameterDefs: function () {
            $.when(LaaS.request('inputParameterDef:entities')).done(function (data) {
                var view = new InputParameterDefListView(data);
                LaaS.mainRegion.show(view);
            });
        }
    });

    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'inputParameterDefs(/)': 'showInputParameterDefs',
                'inputParameterDefs/:id(/)': 'showInputParameterDef'
            },
            controller: new InputParameterDefController()
        });
    });
});
