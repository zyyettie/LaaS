LaaS.module('File', function(File, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;

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

    File.FileSelectView = Marionette.ItemView.extend({
        initialize: function(options) {
            this.files = options.files;
            this.job = options.job;
            this.jobmodel = options.jobmodel;
        },
        template : function(data) {
            var template = JST['app/handlebars/file/select'];
            var html = template(data);
            return html;
        },
        serializeData : function() {
            if (this.job.files == undefined) {
                return {files:this.files};
            }
            for (var i=0; i<this.files.length; i++) {
                this.files[i].selected = false;
                this.files[i].checked = "";
                for (var j=0; j<this.job.files.length; j++) {
                    if (this.files[i].id == this.job.files[j].id) {
                        this.files[i].selected = true;
                        this.files[i].checked = 'checked=""';
                        break;
                    }
                }
            }
            return {files:this.files};
        },
        events: {
            'click #select_file':'selectFile',
            'click #select_file_cancel':'cancelSelect'
        },
        selectFile: function() {
            var selectFiles = [];
            for (var i=0; i<this.files.length; i++) {
                var fileid = this.files[i].id;
                if ($("#file_checkbox_"+fileid).prop("checked")) {
                    selectFiles.push(this.files[i]);
                }
            }

            var jobView = new LaaS.Job.JobView({model:this.jobmodel, job:this.job, scenarioList:this.job.scenarioList,
                selectedScenarios:this.job.selectedScenarios, files:selectFiles, selectedParameterDefines:this.job.selectedParameterDefines});
            LaaS.mainRegion.show(jobView);
            if (this.job.id == undefined) {
                LaaS.navigate('/jobnew');
            } else {
                LaaS.navigate('/jobs/'+this.job.id);
            }
        },
        cancelSelect: function() {
            var jobView = new LaaS.Job.JobView({model:this.jobmodel, job:this.job, scenarioList:this.job.scenarioList,
                selectedScenarios:this.job.selectedScenarios, files:this.job.files, selectedParameterDefines:this.job.selectedParameterDefines});
            LaaS.mainRegion.show(jobView);
            if (this.job.id == undefined) {
                LaaS.navigate('/jobnew');
            } else {
                LaaS.navigate('/jobs/'+this.job.id);
            }
        }
    });

    File.MyFileView = Marionette.ItemView.extend({
        initialize : function(options){
            this.files = options.files;
        },
        template : function(data){
            var template = JST['app/handlebars/file/mylist'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            for (var i=0; i<this.files.length; i++) {
                this.files[i].size = Math.round(this.files[i].size / 1024);
            }
            return {files:this.files};
        },
        events: {
            'click #upload_my_files':'uploadMyFiles',
            'click #delete_my_files':'deleteMyFiles'
        },
        uploadMyFiles: function(){
            $.when(LaaS.request('fileTypes:entities')).done(function(data){
                LaaS.mainRegion.show(new LaaS.Views.FileUploader({'url':'/files/me','fileTypes':data.fileTypes}));
                LaaS.navigate('/files/me/upload');
            });

        },
        deleteMyFiles: function() {
            var selectFiles = [];
            for (var i=0; i<this.files.length; i++) {
                var fileId = this.files[i].id;
                if ($("#file_checkbox_"+fileId).prop("checked")) {
                    selectFiles.push(this.files[i]);
                }
            }

            $.ajax({
                type: "POST",
                url: appContext+"/controllers/files",
                data: JSON.stringify(selectFiles),
                success: function(data){
                    $.when(LaaS.request('myFiles:entities')).done(function(data){
                        var view = new LaaS.File.MyFileView({files:data.files});
                        LaaS.mainRegion.show(view);
                    });
                },
                dataType: "json",
                contentType: "application/json"
            });
        }
    });

    var FileController = Marionette.Controller.extend({
        showFile: function(id){
            $.when(LaaS.request('file:entity', {'id':id})).done(function(data){
                var view = new FileView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showMyFiles: function(){
            $.when(LaaS.request('myFiles:entities')).done(function(data){
                var view = new LaaS.File.MyFileView({files:data.files});
                LaaS.mainRegion.show(view);
            });
        }
    });

    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'files/me(/)':'showMyFiles'
            },
            controller: new FileController()
        });
    });
});
