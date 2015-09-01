/**
 * Created with IntelliJ IDEA.
 * User: Johnson Jiang
 * Date: 8/31/15
 * Time: 12:00 AM
 */
LaaS.module('User', function (User, LaaS, Backbone, Marionette) {
    'use strict';

    var User = Backbone.Model.extend({
        urlRoot:"/controllers/users"
    });

    var myUser = new User();

    var UserView = Marionette.ItemView.extend({
        template: JST['app/handlebars/user'],
        ui: {
            submitBtn: '.submit.button'
        },
        model: myUser,
        events: {
            'click @ui.submitBtn': 'saveUser'
        },
        saveUser: function () {
            alert('save user data!');
            myUser.save();
        }
    });

    var UserController = Marionette.Controller.extend({
        user: function () {
            LaaS.mainRegion.show(new UserView());
        }
    });


    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'user(/)': 'user'
            },
            controller: new UserController()
        });
    });
});
