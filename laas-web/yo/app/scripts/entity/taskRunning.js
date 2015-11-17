LaaS.module('Entities', function (Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT + LaaS.Util.Constants.APIVERSION + '/taskRunnings';

    var taskRunningModel = Backbone.Model.extend({
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
    LaaS.reqres.setHandler('taskRunning:entity', function (option) {
        var taskRunning = new taskRunningModel(option);
        var defer = $.Deferred();
        taskRunning.fetch().then(function () {
            defer.resolve(taskRunning);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('taskRunning:entities', function (option) {
        var url = LaaS.Util.Constants.APPCONTEXT + LaaS.Util.Constants.APIVERSION + '/taskRunnings';
        var taskRunnings = $.Deferred();
        $.getJSON(url).done(function(data){
            taskRunnings.resolve({taskRunnings:data._embedded ? data._embedded.taskRunnings : [], page:data.page});
        });
        return taskRunnings.promise();
    });

    LaaS.reqres.setHandler('taskRunning:entitiesByUrl', function (option) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var taskRunnings = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.taskRunnings : [];
            taskRunnings.resolve({taskRunnings:list});
        });
        return taskRunnings.promise();
    });
});
