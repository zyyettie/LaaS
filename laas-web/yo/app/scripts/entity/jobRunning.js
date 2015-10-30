LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + '/api/v1/jobRunnings/';

    var jobRunningModel = Backbone.Model.extend({
        url: function () {
            return this.id ? baseUrl + "/" + this.id : baseUrl;
        }
    });
    LaaS.reqres.setHandler('jobRunning:entity', function (job_running_id) {
        var jobRunning = new jobRunningModel({id:job_running_id});
//        jobRunning.fetch();
//        return jobRunning;
        var defer = $.Deferred();
        jobRunning.fetch().then(function () {
            defer.resolve(jobRunning);
        });
        return defer.promise();
    });
});
