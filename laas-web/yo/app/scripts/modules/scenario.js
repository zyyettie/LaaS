LaaS.module('Scenario', function(Scenario, LaaS, Backbone, Marionette) {
    'use strict';

    var ScenarioView = Marionette.ItemView.extend({
        initialize : function(options){
            this.scenario = options.model.attributes;
            this.productList = options.productList;
            this.tasks = options.tasks;
            this.selectedProduct = options.selectedProduct;
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
            return data;
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
                $.when(LaaS.request('task:entitiesByUrl', {'url': scenarioModel.attributes._links.tasks.href}),
                    LaaS.request('product:entitiesByUrl', {'url':scenarioModel.attributes._links.product.href}))
                    .done(function(relatedTasks, selectedProduct) {
                    var view = new ScenarioView({model:scenarioModel, productList:productList.products,
                        tasks:relatedTasks.tasks, selectedProduct:selectedProduct.products});
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
