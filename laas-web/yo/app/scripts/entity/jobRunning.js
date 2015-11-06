LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + '/controllers/jobRunnings';

    var jobRunningModel = Backbone.Model.extend({
        initialize: function(option){
            if(option && option.id){
                this.id = option.id;
            }
        },
        url: function () {
            console.log(this.id);
            return baseUrl + "/" + this.id + "/result";
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
});
