LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/tasks';

    var TaskModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
            return url/* + "?projection=" + this.projection*/;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        },
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
            /*
            if(options && options.projection){
                this.projection = options.projection;
            }else{
                this.projection = 'taskSummary';
            } */
        }
    });

    LaaS.reqres.setHandler('task:entity', function (options) {
        var task = new TaskModel(options);
        var defer = $.Deferred();
        task.fetch().then(function () {
            defer.resolve(task);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('task:entities', function (options) {
        var options = options || {page:0,size:10,projection:'taskSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'taskSummary'
        var tasks = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            tasks.resolve({tasks:data._embedded ? data._embedded.tasks : [], page:data.page});
        });
        return tasks.promise();
    });

    LaaS.reqres.setHandler('task:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var tasks = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.tasks : [];
            tasks.resolve({tasks:list});
        });
        return tasks.promise();
    });
});
