LaaS.module('Home', function (Home, LaaS, Backbone, Marionette) {
    'use strict';

    var appContext = LaaS.Util.Constants.APPCONTEXT;
    var HeaderView = Marionette.ItemView.extend({
        template: function(){
            var template = JST['app/handlebars/header'];
            return template({username:sessionStorage.getItem("username")});
        },

        onRender : function(){
            this.$('#logout').on('click',function(e){
				LaaS.isClickingLogout = true;
                LaaS.cleanSessionStorage();
                LaaS.stopTasks();
                $.get('/laas-server/controllers/logout').always(function(){
                    window.location.href = appContext + '/login';
                });
            });
            this.$('.dropdown .menu .item').on('click',function(e){
                LaaS.navigate($(this).attr('href'), true);
            });
//            this.$('#mailtous').click(function(){
//                alert('email to:');
//            });
            this.$('#homepage').click(function(){
                LaaS.navigate('/home',true);
            });
            this.$('#inbox').click(function(){
                LaaS.navigate('/notifications/me',true);
            });
            $('#header').show();
            $('body').css('background-image','none');
            $('#nav').css({'min-height':window.innerHeight-60,'background-color':'#A0F3E9','visibility':'visible'});
        }
    });

    var NavView = Marionette.ItemView.extend({
        initialize: function(){
            this.isAdmin = LaaS.isAdmin();
        },
        serializeData: function(){
            return {isAdmin:this.isAdmin};
        },
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
        template: JST['app/handlebars/footer']
    });

    Home.showViewFrame = function(mainView) {
        //$('body').css('background-image','none');
        LaaS.headerRegion.show(new HeaderView());
        if(mainView){
            LaaS.mainRegion.show(mainView);
        }
        LaaS.mainNavRegion.show(new NavView());
        LaaS.footerRegion.show(new FooterView());
    };

    var HomeController = Marionette.Controller.extend({
        showHome: function () {
            var uid = sessionStorage.getItem('uid');
            if(uid){
                LaaS.Home.showViewFrame(new MainView());
            }else{
                $.getJSON(appContext+'/controllers/users/current',function(data){
                    sessionStorage.setItem("uid",data.id);
                    sessionStorage.setItem("username",data.name);
                    sessionStorage.setItem("role",data.role.name);
                    LaaS.Home.showViewFrame(new MainView());
                }).fail(function(){
                    LaaS.navigate('/login',true);
                });
            }
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
