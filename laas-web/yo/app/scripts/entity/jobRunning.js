LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + '/controllers/jobRunnings';

    var jobRunningModel = Backbone.Model.extend({
        url: function () {
            console.log(this.id);
            return baseUrl + "/" + this.id + "/result";
        }
    });
    LaaS.reqres.setHandler('jobRunning:entity', function (job_running_id) {
        var jobRunning = new jobRunningModel({id:job_running_id});
        var defer = $.Deferred();
        jobRunning.fetch().then(function () {
            defer.resolve(jobRunning);
        });
        return defer.promise();
    });
});
