LaaS.module('Task', function(Task, LaaS, Backbone, Marionette) {
    'use strict';

    var TaskView = Marionette.ItemView.extend({
        initialize : function(options){
            this.task = options.model.attributes;
            this.inputParameterDefs = options.inputParameterDefs;

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
            data.task.inputParameterDefs = this.inputParameterDefs;
            data.task.productList = this.productList;
            data.task.fileTypeList = this.fileTypeList;
            data.task.selectedProduct = this.selectedProduct;
            data.task.selectedFileType = this.selectedFileType;
            return data;
        },
        onRender: function() {
            var that = this;
            this.$('select').dropdown();

            if (this.inputParameterDefs && this.inputParameterDefs.length > 0 ) {
                var size = 5;
                var currentParameterDefs = [];
                for (var i=0; i<size && i<this.inputParameterDefs.length; i++) {
                    currentParameterDefs.push(this.inputParameterDefs[i]);
                }
                var template = JST['app/handlebars/task/inputParameter'];
                var subHtml = template({inputParameterDefs:currentParameterDefs});
                this.$('#inputParameters').html(subHtml);
                var totalPages = Math.floor(this.inputParameterDefs.length / size) + 1;
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
                            var currentParameterDefs = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.inputParameterDefs[i]) {
                                    break;
                                }
                                currentParameterDefs.push(that.inputParameterDefs[i]);
                            }
                            var template = JST['app/handlebars/task/inputParameter'];
                            var html = template({files: currentParameterDefs});
                            $('#inputParameters').html(html);
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
                $.when(LaaS.request('inputParameterDef:entitiesByUrl', {'url':taskModel.attributes._links.inputParameterDefs.href}),
                    LaaS.request('product:entityByUrl', {'url':taskModel.attributes._links.product.href}),
                    LaaS.request('fileType:entitiesByUrl', {'url':taskModel.attributes._links.fileType.href}))
                    .done(function(inputParameterDefs, selectedProduct, selectedFileType) {
                    var view = new TaskView({model:taskModel, inputParameterDefs:inputParameterDefs.inputParameterDefs,
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
