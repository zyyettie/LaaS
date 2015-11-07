LaaS.module('Home', function (Home, LaaS, Backbone, Marionette) {
    'use strict';


    var HeaderView = Marionette.ItemView.extend({
        template: JST['app/handlebars/header'],
        onRender : function(){
            this.$('#logout').on('click',function(e){
                LaaS.cleanSessionStorage();
                LaaS.stopTasks();
                $.get('/laas-server/controllers/logout').always(function(){
                    LaaS.navigate('login',true);
                });
            });
        }
    });

    var NavView = Marionette.ItemView.extend({
        template: JST['app/handlebars/navigator'],
        onRender: function () {
            this.$('#sidebarmenu').sidebar('toggle');
            this.$('a.item').on('click', function (event) {
                event.preventDefault();
                $('a.item').removeClass('active');
                $(event.target).addClass('active');
                LaaS.navigate($(this).attr('href'), true);
            });

        }
    });

    var MainView = Marionette.ItemView.extend({
        template: JST['app/handlebars/home']
    });

    var SideBarView = Marionette.ItemView.extend({
        template: function () {
            return '<h2>this is side bar to fill something</h2>';
        }
    });

    var FooterView = Marionette.ItemView.extend({
        template: JST['app/handlebars/footer']
    });

    Home.showViewFrame = function(mainView) {
        LaaS.headerRegion.show(new HeaderView());
        LaaS.mainNavRegion.show(new NavView());
        if(mainView){
            LaaS.mainRegion.show(mainView);
        }
        LaaS.footerRegion.show(new FooterView());
    };

    var HomeController = Marionette.Controller.extend({
        showHome: function () {
            var uid = sessionStorage.getItem('uid');
            if(uid != null && uid != undefined){
                LaaS.Home.showViewFrame(new MainView());
            }else{
                LaaS.navigate('login',true);
            }
        },
        uploadLogs: function () {
            LaaS.mainRegion.show(new LaaS.Views.FileUploader());
        }
    });

    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'home(/)': 'showHome',
                'upload(/)': 'uploadLogs'
            },
            controller: new HomeController()
        });
    });
});
