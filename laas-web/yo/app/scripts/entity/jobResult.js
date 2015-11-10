LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + '/controllers/jobRunnings';

    var jobResultModel = Backbone.Model.extend({
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
    LaaS.reqres.setHandler('jobResult:entity', function (option) {
        var jobResult = new jobResultModel(option);
        var defer = $.Deferred();
        jobResult.fetch().then(function () {
            defer.resolve(jobResult);
        });
        return defer.promise();
    });
})