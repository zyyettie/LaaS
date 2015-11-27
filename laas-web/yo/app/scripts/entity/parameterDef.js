LaaS.module('Entities', function(Entities,LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl1 = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/inputParameterDefs';
    var baseUrl2 = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/outputParameterDefs';

    Entities.InputParameterDefModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl1+"/" + this.id : baseUrl1;
            return url;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        },
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
        }
    });

    Entities.OutputParameterDefModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl2+"/" + this.id : baseUrl2;
            return url;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        },
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
        }
    });

    LaaS.reqres.setHandler('inputParameterDef:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl1};
        var url = options.url || baseUrl1;
        var paradefs = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.inputParameterDefs : [];
            paradefs.resolve({inputParameterDefs:list});
        });
        return paradefs.promise();
    });
});
