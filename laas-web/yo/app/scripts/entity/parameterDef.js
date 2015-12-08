LaaS.module('Entities', function(Entities,LaaS, Backbone, Marionette) {
    'use strict';

    var inputBaseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/inputParameterDefs';
    var outputBaseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/outputParameterDefs';

    Entities.InputParameterDefModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? inputBaseUrl+"/" + this.id : inputBaseUrl;
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
            var url = this.id ? outputBaseUrl+"/" + this.id : outputBaseUrl;
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

    LaaS.reqres.setHandler('inputParameterDef:entity', function (options) {
        var paradef = new LaaS.Entities.InputParameterDefModel(options);
        var defer = $.Deferred();
        paradef.fetch().then(function () {
            defer.resolve(paradef);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('inputParameterDef:entities', function (options) {
        var options = options || {page:0,size:10, projection:'inputParameterDefSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'inputParameterDefSummary';
        var paradefs = $.Deferred();
        $.getJSON(inputBaseUrl+"?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            paradefs.resolve({inputParameterDefs:data._embedded ? data._embedded.inputParameterDefs : [], page:data.page});
        });
        return paradefs.promise();
    });

    LaaS.reqres.setHandler('inputParameterDef:entitiesByUrl', function(options) {
        var options = options || {url:inputBaseUrl};
        var url = options.url || inputBaseUrl;
        var paradefs = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.inputParameterDefs : [];
            paradefs.resolve({inputParameterDefs:list});
        });
        return paradefs.promise();
    });
});
