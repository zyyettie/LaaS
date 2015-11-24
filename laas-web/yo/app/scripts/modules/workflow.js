LaaS.module('Workflow', function(Workflow, LaaS, Backbone, Marionette) {
    'use strict';

    var WorkflowView = Marionette.ItemView.extend({
        initialize : function(options){
            this.workflow = options.attributes;
            this.productList = options.productList;
            this.tasks = options.tasks;
            this.selectedProduct = options.selectedProduct;
            this.fileTypes = options.fileTypes;
            this.parameterDefines = options.parameterDefines;
        },
        template : function(data){
            var template = JST['app/handlebars/workflow/detail'];
            var html = template(data.workflow);
            return html;
        },
        serializeData:function(){
            var data = {workflow:this.workflow};
            data.workflow.productList = this.productList;
            data.workflow.tasks = this.tasks;
            data.workflow.selectedProduct = this.selectedProduct;
            data.workflow.fileTypes = this.fileTypes;
            data.workflow.parameterDefines = this.parameterDefines;
            return data;
        },
        onRender:function() {
            var that = this;
            this.$('select').dropdown();

            if (this.tasks && this.tasks.length > 0 ) {
                var size = 10;
                var currentTasks = [];
                for (var i=0; i<size && i<this.tasks.length; i++) {
                    currentTasks.push(this.tasks[i]);
                }
                var template = JST['app/handlebars/workflow/task'];
                var subHtml = template({workflows:currentWorkflows});
                this.$('#tasks').html(subHtml);
                var totalPages = Math.floor(this.tasks.length / size) + 1;
                if (totalPages > 1) {
                    this.$('#paging').twbsPagination({
                        totalPages: totalPages,
                        startPage: 1,
                        visiblePages: 6,
                        first: '<<',
                        prev: '<',
                        next: '>',
                        last: '>>',
                        onPageClick: function (event, page) {
                            var currentTasks = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.tasks[i]) {
                                    break;
                                }
                                currentTasks.push(that.tasks[i]);
                            }
                            var template = JST['app/handlebars/workflow/task'];
                            var html = template({workflows: currentTasks});
                            $('#tasks').html(html);
                        }
                    })
                }
            }
        },
        events: {
            'click #workflow_save': 'saveWorkflow',
            'click #workflow_cancel': 'cancelWorkflow'
        },
        cancelWorkflow: function() {
            window.history.back();
        },
        saveWorkflow: function() {

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
        },
        events:{
            'click button.workflow-show': "showClicked"
        },
        showClicked: function(event){
            var workflowId = event.target.dataset["id"];
            LaaS.navigate('/workflows/'+workflowId, true);
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
            $.when(LaaS.request('workflow:entity', {'id':id}), LaaS.request('product:entities')).done(function(workflowModel, productList){
                $.when(LaaS.request('task:entitiesByUrl', {'url': workflowModel.attributes._links.tasks.href}),
                        LaaS.request('product:entityByUrl', {'url':workflowModel.attributes._links.product.href}),
                        LaaS.request('fileType:entitiesByUrl', {'url':workflowModel.attributes._links.fileTypes.href}),
                        LaaS.request('parameterDefine:entitiesByUrl', {'url':workflowModel.attributes._links.parameterDefines.href}))
                    .done(function(relatedTasks, selectedProduct, selectedFileTypes, selectedParameterDefines) {
                        var view = new WorkflowView({model:workflowModel, productList:productList.products,
                            tasks:relatedTasks.tasks, selectedProduct:selectedProduct.product, fileTypes:selectedFileTypes.fileTypes,
                            parameterDefines:selectedParameterDefines.parameterDefines});
                        LaaS.mainRegion.show(view);
                    })
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

