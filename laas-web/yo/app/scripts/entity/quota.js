LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/quotas';

    Entities.QuotaModel = Backbone.Model.extend({
        url:function() {
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
            return url;
        },
        isNew:function() {
            return this.id == null || this.id == undefined;
        },
        initialize:function(options) {
            if (options && options.id) {
                this.id = options.id;
            }
        }
    });

    LaaS.reqres.setHandler('quota:entityOfCurrentUser', function(options) {
        var quota = $.Deferred();
        var userName = sessionStorage.getItem("username");

        $.getJSON(baseUrl+"/search/findUserQuota?userName=" + userName).done(function(data){
            quota.resolve({quota:data._embedded.quotas[0]});
        });
        return quota.promise();
    });
});