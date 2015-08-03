LaaS = new Marionette.Application();

LaaS.addRegions({
  mainNavRegion: '#header',
  //subNavRegion: '#submenu',
  mainRegion: '#main',
  footerRegion: '#footer',
  dialogRegion: '#login-dialog',
  registerRegion: '#register-dialog'
});

LaaS.navigate = function(route,options) { 
  options || (options = {}); 
  Backbone.history.navigate(route, options);
};

LaaS.on('start', function() {
  Backbone.history.start({ pushState: true });
});
