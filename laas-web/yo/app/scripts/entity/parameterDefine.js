LaaS.module('Entities', function(Entities,LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/parameterDefines';

    Entities.ParameterDefineModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
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

    LaaS.reqres.setHandler('parameterDefine:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var paradefs = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.parameterDefines : [];
            paradefs.resolve({parameterDefines:list});
        });
        return paradefs.promise();
    });
})
