LaaS = new Marionette.Application();

LaaS.addRegions({
  headerRegion: '#header',
  mainNavRegion: '#nav',
  mainRegion: '#main',
  sidebarRegion: '#aside',
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
