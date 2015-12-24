LaaS.module('Register', function(Register, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;
     var RegisterModel = Backbone.Model.extend({
        url: appContext+ "/controllers/users",
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
        }
    });
    var RegisterView = Register.RegisterView = Marionette.ItemView.extend({
        template : JST['app/handlebars/register'],
        onRender:function(){
            this.$('.ui.form').form({
                fields:{
                    username: {
                        identifier  : 'name',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : 'Please enter a username'
                            }
                        ]
                    },
                    email: {
                        identifier  : 'email',
                        optional    : true,
                        rules: [
                            {
                                type   : 'email',
                                prompt : 'Please enter a valid email'
                            }
                        ]
                    },
                    password: {
                        identifier  : 'password',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : 'Please enter a password'
                            },
                            {
                                type : 'minLength[6]',
                                prompt : 'password must be at least 6 characters'
                            }
                        ]
                    },
                    confirmpassword: {
                        identifier  : 'confirmpassword',
                        rules: [
                            {
                                type   : 'match[password]',
                                prompt : 'Password and Confirm Password don’t match'
                            }
                        ]
                    }
                }
            });
            $('#nav').css('visibility','hidden');
            $('#header').hide();
            $('body').css('background-image','url(images/login.jpg)');
        },
        events:{
            'click #register': 'validateForm',
            'click #cancel': 'cancelRegister'
        },
        register : function(){
            var $form = $('.ui.form');
            var user = $form.form('get values');
            var model = new RegisterModel();
            model.save(user, {patch: true, success: function (option) {
                toastr.info('User '+ option.attributes.name+' register successfully.');
                LaaS.navigate('/regDone',true);
            }, error: function (option) {
                toastr.error('User '+ option.attributes.name+' has been used, please pick up another name.');
            }});

        },

        cancelRegister: function(){
            LaaS.navigate('/login',true);
        },
        validateForm : function(event){
            if($('.ui.form').form('validate form')){
                this.register();
            }
        }
    });

    var RegisterDoneView = Register.RegisterDoneView = Marionette.ItemView.extend({
        template : JST['app/handlebars/regDone'],
        onRender:function(){
            $('#nav').css('visibility','hidden');
            $('#header').hide();
            $('body').css('background-image','url(images/login.jpg)');
        },
        events:{
            'click #backLogin': 'cancelRegister'
        },
        cancelRegister: function(){
            LaaS.navigate('/login',true);
        }
    });

    var HomeController = Marionette.Controller.extend({
        register: function() {
            LaaS.mainRegion.show(new RegisterView());
        },
        regDone: function(){
            LaaS.mainRegion.show(new RegisterDoneView());
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'register(/)': 'register',
                'regDone(/)': 'regDone'
            },
            controller: new HomeController()
        });
    });
});
