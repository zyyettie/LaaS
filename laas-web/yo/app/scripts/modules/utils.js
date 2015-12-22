LaaS.module('Util', function (Util, LaaS, Backbone, Marionette) {
    'use strict';

    Util.Constants = {APPCONTEXT: '/laas-server', APIVERSION: '/api/v1', CONTROLLERS: '/controllers'};

    Util.ScheduledTask = function (func, interval, isOnce) {
        this.func = func;
        this.interval = interval;
        this.isOnce = isOnce;
    };

    Util.getFileDisplaySize = function (size) {
        size = size | 0;

        var number = Number(Math.ceil(size / 1024));

        return number.toLocaleString() + " KB";
    };

    Util.showErrors = function (errors) {
        var errorStr = '';
        for (var i = 0; i < errors.length; i++) {
            errorStr += '<li>' + errors[i] + '</li>'
        }

        if (errors.length > 0) {
            $('#errors').html(errorStr);
            $('#errors').show();
            return true;
        } else {
            return false;
        }
    }
});
