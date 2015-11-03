LaaS.module('Inbox', function (Inbox, LaaS, Backbone, Marionette) {
  'use strict';

  var appContext = LaaS.Util.Constants.APPCONTEXT;

  var baseTemplatePath = 'app/handlebars/notification';
  LaaS.Inbox.queryTask = new LaaS.Util.ScheduledTask(function () {
    $.get(appContext + '/api/v1/notifications/search/findMyUnreadCount', function (data) {
      $('#inboxMenuItem .label').text(data);
    })
  }, 30000);

  var NotificationListView = Marionette.ItemView.extend({
    initialize: function (pagedNotifications) {
      this.notificationList = pagedNotifications.notifications;
      this.paging = pagedNotifications.page;
    },

    template: JST[baseTemplatePath+'/layout'],

    onRender:function(){
      var template = JST[baseTemplatePath + '/notificationlist'];
      var html = template({notifications: this.notificationList});
      this.$('#content').html(html);
      if(this.paging.number + 1 <= this.paging.totalPages){
        $('#paging').twbsPagination({
          totalPages: this.paging.totalPages,
          startPage: this.paging.number + 1,
          visiblePages: 6,
          first: 'First',
          prev: 'Previous',
          next: 'Next',
          last: 'Last',
          onPageClick: function (event, page) {
            var template = JST[baseTemplatePath + '/notificationlist'];
            $.when(LaaS.request('notification:entities:me',{page:page-1})).done(function (data) {
              var html = template({notifications: data.notifications});
              $('#content').html(html);
            });
          }
        })
      }
    }
  });

  var NotificationController = Marionette.Controller.extend({
    listMyNotifications: function () {
      $.when(LaaS.request('notification:entities:me')).done(function(pagedNotifications){
         var list = new NotificationListView(pagedNotifications);
        LaaS.mainRegion.show(list);
      });
    }
  });

  LaaS.addInitializer(function () {
    new Marionette.AppRouter({
      appRoutes: {
        'notifications/me(/)': 'listMyNotifications'
      },
      controller: new NotificationController()
    });
  });

});