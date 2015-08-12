LaaS.module('Rule', function(Rule, LaaS, Backbone, Marionette) {
    'use strict';

    var RuleView = Marionette.ItemView.extend({
        template : JST['app/handlebars/rule']
    });

    var RuleController = Marionette.Controller.extend({
        showRule: function() {
            LaaS.mainNavRegion.show(new RuleView());
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                '(/)': 'showRule'
            },
            controller: new RuleController()
        });
    });
});
