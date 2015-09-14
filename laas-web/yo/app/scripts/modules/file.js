LaaS.module('File', function(File, LaaS, Backbone, Marionette) {
    'use strict';

    var FileView = Marionette.ItemView.extend({
        initialize : function(options){
            this.file = options.attributes;
        },
        template : function(data){
            var template = JST['app/handlebars/file/detail'];
            var html = template(data.file);
            return html;
        },
        serializeData:function(){
            return {file:this.file};
        }
    });

    var FileListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.files = options.files;
        },
        template : function(data){
            var template = JST['app/handlebars/file/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {files:this.files};
        }
    });

    var FileController = Marionette.Controller.extend({
        showFiles: function() {
            $.when(LaaS.request('file:entities')).done(function(data){
                var view = new FileListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showFile: function(id){
            $.when(LaaS.request('file:entity', {'id':id})).done(function(data){
                var view = new FileView(data);
                LaaS.mainRegion.show(view);
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'files(/)': 'showFiles',
                'files/:id(/)' : 'showFile'
            },
            controller: new FileController()
        });
    });
});
