LaaS.module('Login', function(Login, LaaS, Backbone, Marionette) {
    'use strict';

    var LoginView = Marionette.ItemView.extend({
        template : JST['app/handlebars/login'],
        onRender:function(){
            this.$('.ui.form').form({
                fields: {
                    username: {
                        identifier  : 'username',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : 'Please enter your user name'
                            }
                        ]
                    },
                    password: {
                        identifier  : 'password',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : 'Please enter your password'
                            },
                            {
                                type   : 'length[6]',
                                prompt : 'Your password must be at least 6 characters'
                            }
                        ]
                    }
                }
            });
        },
        ui:{
            submitBtn:'.teal.button'
        },

        events:{
          'click @ui.submitBtn' : 'validateForm'
        },

        validateForm : function(event){
           if($('.ui.form').form('validate form')){
               var user = $('.ui.form').form('get values');
               $.ajax({
                   type: "POST",
                   url: "/controllers/login",
                   data: JSON.stringify(user),
                   success: function(data){
                       LaaS.navigate('/home',true);
                   },
                   dataType: "json",
                   contentType: "application/json"
               });
           }
        }
    });

    var HomeController = Marionette.Controller.extend({
        login: function() {
            LaaS.mainRegion.show(new LoginView());
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                '(/)': 'login',
                'login(/)': 'login'
            },
            controller: new HomeController()
        });
    });
});
