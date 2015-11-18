LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
  'use strict';

  var baseUrl = LaaS.Util.Constants.APPCONTEXT +LaaS.Util.Constants.APIVERSION+'/notifications';

  LaaS.reqres.setHandler('notification:entities:me', function(options) {
    var options = options || {page:0,size:10,projection:'notificationsummary',status:'ALL'};
    var page = options.page || 0;
    var size = options.size || 10;
    var projection = options.projection || 'notificationsummary';
    var status = options.status || 'ALL';
    var notifications = $.Deferred();
    var apiAddress = baseUrl+"/search/findMyNotification?status="+status + "&page=" + page + "&size=" + size + "&projection=" + projection;
    if(status == 'ALL'){
      apiAddress = baseUrl+"/search/allMyNotifications?page=" + page + "&size=" + size + "&projection=" + projection;
    }
    $.getJSON(apiAddress).done(function(data){
      notifications.resolve({notifications:data._embedded ? data._embedded.notifications:[],page:data.page});
    });
    return notifications.promise();
  });
});
