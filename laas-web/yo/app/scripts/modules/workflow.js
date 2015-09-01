LaaS.module('Workflow', function(Workflow, LaaS, Backbone, Marionette) {
    'use strict';

    var WorkflowView = Marionette.ItemView.extend({
        initialize : function(options){
            this.workflow = options.attributes;
        },
        template : function(data){
            var template = JST['app/handlebars/workflow/detail'];
            var html = template(data.workflow);
            return html;
        },
        serializeData:function(){
            return {workflow:this.workflow};
        }
    });

    var WorkflowListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.workflows = options.workflows;
        },
        template : function(data){
            var template = JST['app/handlebars/workflow/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {workflows:this.workflows};
        }
    });

    var WorkflowController = Marionette.Controller.extend({
        showWorkflows: function() {
            $.when(LaaS.request('workflow:entities')).done(function(data){
                var view = new WorkflowListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showWorkflow: function(id){
//            var workflow = LaaS.request("workflow:entity", id);
//            var workflowView;
//            LaaS.mainRegion.show(view);
            $.when(LaaS.request('workflow:entity', {'id':id})).done(function(data){
                var view = new WorkflowView(data);
                LaaS.mainRegion.show(view);
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'workflows(/)': 'showWorkflows',
                'workflows/:id(/)' : 'showWorkflow'
            },
            controller: new WorkflowController()
        });
    });
});

