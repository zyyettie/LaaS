LaaS.module('Home', function (Home, LaaS, Backbone, Marionette) {
    'use strict';


    var HeaderView = Marionette.ItemView.extend({
        template: function () {
            return '<h2 style="text-align: center;"><div id="logout" class="ui button">Logout</div></h2>';
        },
        onRender : function(){
            this.$('#logout').on('click',function(e){
                $.get('/laas-server/controllers/logout',function(){
                    sessionStorage.removeItem('uid');
                    sessionStorage.removeItem('username');
                    sessionStorage.removeItem('role');
                    LaaS.navigate('login',true);
                });
            });
        }
    });

    var NavView = Marionette.ItemView.extend({
        template: JST['app/handlebars/navigator'],
        onRender: function () {
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
        template: function () {
            return '<h2 style="text-align: center;">this is footer to fill something</h2>';
        }
    });

    Home.showViewFrame = function(mainView) {
        LaaS.headerRegion.show(new HeaderView());
        LaaS.mainNavRegion.show(new NavView());
        LaaS.mainRegion.show(mainView);
        LaaS.footerRegion.show(new FooterView());
    };

    var HomeController = Marionette.Controller.extend({
        showHome: function () {
            var uid = sessionStorage.getItem('uid')
            if(uid == null || uid == undefined){
                LaaS.navigate('login',true);
            }else{
                LaaS.Home.showViewFrame(new MainView());
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
