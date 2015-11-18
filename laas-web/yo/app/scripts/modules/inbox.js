LaaS.module('Inbox', function (Inbox, LaaS, Backbone, Marionette) {
  'use strict';

  var appContext = LaaS.Util.Constants.APPCONTEXT;
  var apiVersion = LaaS.Util.Constants.APIVERSION;

  var baseTemplatePath = 'app/handlebars/notification';
  LaaS.Inbox.queryTask = new LaaS.Util.ScheduledTask(function () {
    $.get(appContext + apiVersion+'/notifications/search/findMyUnreadCount', function (data) {
      var count = parseInt(data);
      if(count != NaN && count > 0){
        $('#inboxMenuItem .label').text(data);
        $('#inbox .label').text(data);
        $('#inboxMenuItem .label').css('visibility','visible');
        $('#inbox .label').css('visibility','visible');
      }else{
        $('#inboxMenuItem .label').css('visibility','hidden');
        $('#inbox .label').css('visibility','hidden');
      }
    })
  }, 10000);

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
      this.$('a.link_handler').click(function(event){
        event.preventDefault();
        LaaS.navigate($(this).attr('href'),true);
      });
      this.$('#notificationMenu a.item').click(function(e){
        $('#notificationMenu a.item').removeClass('active');
        $(e.target).addClass('active');
        $('#pagingWrapper').empty();
        var clickedId = $(this).attr('id');
        $.when(LaaS.request('notification:entities:me',{status:clickedId})).done(function(pagedNotifications){
          $('#content').html(template({notifications:pagedNotifications.notifications}));
          $('#pagingWrapper').html('<div id="paging">');
          $('#paging').twbsPagination({
            totalPages: pagedNotifications.page.totalPages,
            startPage: pagedNotifications.page.number + 1,
            visiblePages: 6,
            first: '<<',
            prev: '<',
            next: '>',
            last: '>>',
            onPageClick: function (event, page) {
              var template = JST[baseTemplatePath + '/notificationlist'];
              var status = $('a.active.item').id;
              $.when(LaaS.request('notification:entities:me',{page:page-1,status:status})).done(function (data) {
                var html = template({notifications: data.notifications});
                $('#content').html(html);
              });
            }
          });
        });
      });

      if(this.paging.number + 1 <= this.paging.totalPages){
        this.$('#paging').twbsPagination({
          totalPages: this.paging.totalPages,
          startPage: this.paging.number + 1,
          visiblePages: 6,
          first: '<<',
          prev: '<',
          next: '>',
          last: '>>',
          onPageClick: function (event, page) {
            var template = JST[baseTemplatePath + '/notificationlist'];
            var status = $('a.active.item').id;
            $.when(LaaS.request('notification:entities:me',{page:page-1,status:status})).done(function (data) {
              var html = template({notifications: data.notifications});
              $('#content').html(html);
            });
          }
        })
      }
    },

    onDomRefresh:function(){
      this.$('p.description').click(function(e){
        e.preventDefault();
        if($('#NEW').hasClass('active'))
          var apiAddress = appContext + "/controllers/notifications/"+$(this).attr('id');
        $.getJSON(apiAddress,function(data){
          $(e.target).css('text-decoration','line-through');
        });
      });
    }
  });

  var NotificationController = Marionette.Controller.extend({
    listMyNotifications: function () {
      $.when(LaaS.request('notification:entities:me',{status:'NEW'})).done(function(pagedNotifications){
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
