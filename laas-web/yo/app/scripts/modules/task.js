LaaS.module('Task', function(Task, LaaS, Backbone, Marionette) {
    'use strict';

    var TaskView = Marionette.ItemView.extend({

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
        }
    });

    var TaskController = Marionette.Controller.extend({
        showTasks: function() {
            $.when(LaaS.request('task:entities')).done(function(data){
               var view = new TaskListView(data);
               LaaS.mainRegion.show(view);
            });
        },
        showTask: function(){

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
