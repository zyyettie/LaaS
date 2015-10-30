LaaS.module('Util', function(Util, LaaS, Backbone, Marionette) {
    'use strict';
    Util.Constants = {APPCONTEXT:'/laas-server'};
    Util.getComboboxSelected = function(value, target_value) {
        return value.toLowerCase() == target_value.toLowerCase() ? "selected='selected'" : "";
    };
});
