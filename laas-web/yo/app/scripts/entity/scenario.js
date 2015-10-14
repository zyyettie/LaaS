LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+'/api/v1/scenarios';

    Entities.ScenarioModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
            return url + "?projection=" + this.projection;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        },
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
            if(options && options.projection){
                this.projection = options.projection;
            }else{
                this.projection = 'scenarioSummary';
            }
        }
    });

    LaaS.reqres.setHandler('scenario:entity', function (options) {
        var scenario = new ScenarioModel(options);
        var defer = $.Deferred();
        scenario.fetch().then(function () {
            defer.resolve(scenario);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('scenario:entities', function (options) {
        var options = options || {page:0,size:10,projection:'scenarioSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'scenarioSummary';
        var scenarios = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            scenarios.resolve({scenarios:data._embedded.scenarios,page:data.page});
        });
        return scenarios.promise();
    });
});
