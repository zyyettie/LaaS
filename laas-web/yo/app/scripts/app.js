LaaS = new Marionette.Application();

LaaS.addRegions({
  headerRegion: '#header',
  mainNavRegion: '#nav',
  mainRegion: '#main',
  //sidebarRegion: '#aside',
  footerRegion: '#footer',
  dialogRegion: '#login-dialog',
  registerRegion: '#register-dialog'
});

LaaS.navigate = function(route,options) {
  options || (options = {});
  Backbone.history.navigate(route, options);
};

toastr.options.positionClass = 'toast-top-center';

LaaS.on('start', function() {
  Backbone.history.start({ pushState: true,root:'/laas-server/'});
  Backbone.Intercept.start();
});

$(document).ajaxError(function (event, xhr, options,thrownError) {
  var statusCode = xhr.status;
  if(statusCode == 401 || statusCode == 403) {
    LaaS.navigate('login',true);
  }
});