LaaS.module('Home', function(Home, LaaS, Backbone, Marionette) {
  'use strict';

  var HomeView = Marionette.ItemView.extend({
    template : JST['app/handlebars/home']
  });
 
  var HomeController = Marionette.Controller.extend({
    showHome: function() {
        LaaS.mainRegion.show(new HomeView());
      }
  });


  LaaS.addInitializer(function() {
    new Marionette.AppRouter({
      appRoutes : { 
        '(/)': 'showHome'
      },
      controller: new HomeController()
    });
  });
});
