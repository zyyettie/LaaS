LaaS.module('Task', function(Task, LaaS, Backbone, Marionette) {
    'use strict';

    var TaskView = Marionette.ItemView.extend({
        initialize : function(options){
            this.task = options.model.attributes;
            this.parameters = options.parameters;

            this.productList = options.productList;
            this.fileTypeList = options.fileTypeList;
            this.selectedProduct = options.selectedProduct;
            this.selectedFileType = options.selectedFileType;
        },
        template : function(data){
            var template = JST['app/handlebars/task/detail'];
            var html = template(data.task);
            return html;
        },
        serializeData:function(){
            var data = {task:this.task};
            data.task.parameters = this.parameters;
            data.task.productList = this.productList;
            data.task.fileTypeList = this.fileTypeList;
            data.task.selectedProduct = this.selectedProduct;
            data.task.selectedFileType = this.selectedFileType;
            return data;
        },
        onRender: function() {
            var that = this;
            this.$('select').dropdown();

            if (this.parameters && this.parameters.length > 0 ) {
                var size = 5;
                var currentParameters = [];
                for (var i=0; i<size && i<this.parameters.length; i++) {
                    currentParameters.push(this.parameters[i]);
                }
                var template = JST['app/handlebars/task/parameter'];
                var subHtml = template({parameters:currentParameters});
                this.$('#parameters').html(subHtml);
                var totalPages = Math.floor(this.parameters.length / size) + 1;
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
                            var currentParameters = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.parameters[i]) {
                                    break;
                                }
                                currentParameters.push(that.parameters[i]);
                            }
                            var template = JST['app/handlebars/task/parameter'];
                            var html = template({files: currentParameters});
                            $('#parameters').html(html);
                        }
                    })
                }
            }
        },
        events: {
            'click #task_save': 'saveTask',
            'click #task_cancel': 'cancelTask'
        },
        cancelTask: function() {
            window.history.back();
        },
        saveTask: function() {

        }
    });

    var TaskListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.tasks = options.tasks;
        },
        template : function(data){
            var template = JST['app/handlebars/task/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {tasks:this.tasks};
        },
        events:{
            'click button.task-show': "showClicked"
        },
        showClicked: function(event){
            var taskId = event.target.dataset["id"];
            LaaS.navigate('/tasks/'+taskId, true);
        }
    });

    var TaskController = Marionette.Controller.extend({
        showTasks: function() {
            $.when(LaaS.request('task:entities')).done(function(data){
               var view = new TaskListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showTask: function(id){
            $.when(LaaS.request('task:entity', {'id':id}), LaaS.request('product:entities'), LaaS.request('fileType:entities'))
                .done(function(taskModel, productList, fileTypeList){
                $.when(LaaS.request('parameterDefine:entitiesByUrl', {'url':taskModel.attributes._links.parameterDefines.href}),
                    LaaS.request('product:entityByUrl', {'url':taskModel.attributes._links.product.href}),
                    LaaS.request('fileType:entitiesByUrl', {'url':taskModel.attributes._links.fileType.href}))
                    .done(function(relatedParameterDefines, selectedProduct, selectedFileType) {
                    var view = new TaskView({model:taskModel, parameters:relatedParameterDefines.parameterDefines,
                        productList:productList.products, fileTypeList:fileTypeList.fileTypes, selectedProduct: selectedProduct.product,
                        selectedFileType:selectedFileType.fileTypes});
                    LaaS.mainRegion.show(view);
                });
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'tasks(/)': 'showTasks',
                'tasks/:id(/)' : 'showTask'
            },
            controller: new TaskController()
        });
    });
});
