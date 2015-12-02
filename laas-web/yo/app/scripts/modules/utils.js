LaaS.module('Util', function(Util, LaaS, Backbone, Marionette) {
    'use strict';

    Util.Constants = {APPCONTEXT:'/laas-server', APIVERSION:'/api/v1', CONTROLLERS:'/controllers'};

    Util.ScheduledTask = function(func,interval,isOnce){
        this.func = func;
        this.interval = interval;
        this.isOnce = isOnce;
    };

    Util.getFileDisplaySize = function(size) {
        size = size | 0;
        var unit = ["KB", "MB", "GB", "TB", "PB"];
        var convertedSize = size;
        for (var i=0; i<unit.length; i++) {
            convertedSize = Math.ceil(convertedSize / 1024);
            if (convertedSize < 1024) {
                break;
            }
        }

        return i>=unit.length ? ""+convertedSize+unit[unit.length - 1] :""+convertedSize+unit[i];
    };
});
