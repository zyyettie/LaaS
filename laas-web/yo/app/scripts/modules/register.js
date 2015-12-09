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
                fields: {
                    username: {
                        identifier  : 'userName',
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
            $('#nav').css('visibility','hidden');
            $('#header').hide();
            $('body').css('background-image','url(images/login.jpg)');
        },
        ui:{
            submitBtn:'.teal.button'
        },

        events:{
            'click @ui.submitBtn' : 'validateForm',
            'click #register': 'register',
            'click #cancel': 'cancelRegister'
        },
        register : function(){
            var model = new RegisterModel();
            var json = this;
            json = Backbone.Syphon.serialize(json);
            model.save(json, {patch: true, success: function (option) {
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
                var user = $('.ui.form').form('get values');
                $.ajax({
                    type: "POST",
                    url: appContext+"/controllers/login",
                    data: JSON.stringify(user),
                    success: function(data){
                        sessionStorage.setItem("uid",data.id);
                        sessionStorage.setItem("username",data.name);
                        sessionStorage.setItem("role",data.role.name);
                        LaaS.scheduleTask(LaaS.Inbox.queryTask);
                        LaaS.TaskQueue.push(LaaS.Inbox.queryTask);
                        LaaS.navigate('/home',true);
                    },
                    dataType: "json",
                    contentType: "application/json"
                });
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
