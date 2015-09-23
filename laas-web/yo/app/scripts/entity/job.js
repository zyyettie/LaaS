LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.URLPREFIX+'/api/v1/jobs';

    var JobModel = Backbone.Model.extend({
        url: function(){
            return this.id ? baseUrl+"/" + this.id : baseUrl;
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
            }
        }
    });

    LaaS.reqres.setHandler('job:new', function() {
        return new JobModel();
    });

    LaaS.reqres.setHandler('job:entities', function(options) {
        var options = options || {page:0,size:10,projection:'jobSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'jobSummary';
        var jobs = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            jobs.resolve({jobs:data._embedded.jobs,page:data.page});
        });
        return jobs.promise();
    });

    LaaS.reqres.setHandler('job:entity', function(options) {
        var job = new JobModel(options);
        var defer = $.Deferred();
        job.fetch().then(function () {
            defer.resolve(job);
        });
        return defer.promise();
    });
});
