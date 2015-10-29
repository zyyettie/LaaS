LaaS.module('Util', function(Util, LaaS, Backbone, Marionette) {
    'use strict';
    Util.Constants = {APPCONTEXT:'/laas-server'};
    Util.getComboboxSelected = function(value, target_value) {
        return value == target_value ? "selected='selected'" : "";
    };
});
