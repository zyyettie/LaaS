LaaS.module('Util', function(Util, LaaS, Backbone, Marionette) {
    'use strict';

    Util.Constants = {APPCONTEXT:'/laas-server', APIVERSION:'/api/v1', CONTROLLERS:'/controllers'};

    Util.ScheduledTask = function(func,interval,isOnce){
        this.func = func;
        this.interval = interval;
        this.isOnce = isOnce;
    };
});
