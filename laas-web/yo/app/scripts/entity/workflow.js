LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.URLPREFIX+'/api/v1/workflows';

    var WorkflowModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl + "?projection=" + this.projection;
            return url;
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
            }else{
                this.projection = 'workflowSummary';
            }
        }
    });

    LaaS.reqres.setHandler('workflow:entity', function (options) {
        var workflow = new WorkflowModel(options);
        var defer = $.Deferred();
        workflow.fetch().then(function () {
            defer.resolve(workflow);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('workflow:entities', function (options) {
        var options = options || {page:0,size:10,projection:'workflowSummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'workflowSummary'
        var workflows = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            workflows.resolve({workflows:data._embedded.workflows,page:data.page});
        });
        return workflows.promise();
    });
});

