LaaS.module('Scenario', function(Scenario, LaaS, Backbone, Marionette) {
    'use strict';

    var ScenarioView = Marionette.ItemView.extend({
        initialize : function(options){
            this.scenario = options.attributes;
        },
        template : function(data){
            var template = JST['app/handlebars/scenario/detail'];
            var html = template(data.scenario);
            return html;
        },
        serializeData:function(){
            return {scenario:this.scenario};
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
            $.when(LaaS.request('scenario:entity', {'id':id})).done(function(data){
               var view = new ScenarioView(data);
                LaaS.mainRegion.show(view);
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
