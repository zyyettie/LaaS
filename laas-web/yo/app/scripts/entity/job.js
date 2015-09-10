LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = '/api/v1/jobs';

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
});
