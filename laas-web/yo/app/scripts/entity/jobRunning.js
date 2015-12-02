LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + LaaS.Util.Constants.APIVERSION + '/jobRunnings';

    var jobRunningModel = Backbone.Model.extend({
        initialize: function(option){
            if(option && option.id){
                this.id = option.id;
            }
        },
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
            return url/* + "?projection=" + this.projection*/;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        }
    });
    LaaS.reqres.setHandler('jobRunning:entity', function (option) {
        var jobRunning = new jobRunningModel(option);
        var defer = $.Deferred();
        jobRunning.fetch().then(function () {
            defer.resolve(jobRunning);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('jobRunning:entities', function (option) {
        var options = options || {page:0,size:10,projection:'jobRunningSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'jobRunningSummary';
        var url = baseUrl + "?&page=" + page + "&size=" + size + "&projection=" + projection;
        var jobRunnings = $.Deferred();
        $.getJSON(url).done(function(data){
            jobRunnings.resolve({jobRunnings:data._embedded ? data._embedded.jobRunnings : [], page:data.page});
        });
        return jobRunnings.promise();
    });

    LaaS.reqres.setHandler('jobRunning:myEntities', function (option) {
        var options = options || {page:0,size:10,projection:'jobRunningSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'jobRunningSummary';

        var userName = sessionStorage.getItem("username");

        var url =  baseUrl+"/search/findJobRunningsOwnedBy?userName=" + userName + "&page=" + page + "&size=" + size + "&projection=" + projection;
        var jobRunnings = $.Deferred();
        $.getJSON(url).done(function(data){
            jobRunnings.resolve({jobRunnings:data._embedded ? data._embedded.jobRunnings : [], page:data.page});
        });
        return jobRunnings.promise();
    });
});
