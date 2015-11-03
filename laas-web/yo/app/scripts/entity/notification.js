LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
  'use strict';

  var baseUrl = LaaS.Util.Constants.APPCONTEXT + '/api/v1/notifications';

  LaaS.reqres.setHandler('notification:entities:me', function(options) {
    var options = options || {page:0,size:10,projection:'notificationsummary'};
    var page = options.page || 0;
    var size = options.size || 10;
    var projection = options.projection || 'notificationsummary';
    var notifications = $.Deferred();
    $.getJSON(baseUrl+"/search/findMyNotification?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
      notifications.resolve({notifications:data._embedded ? data._embedded.notifications:[],page:data.page});
    });
    return notifications.promise();
  });

});
