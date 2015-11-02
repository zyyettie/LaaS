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

    var FileListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.files = options.files;
            this.jobid = options.jobid;
        },
        template : function(data){
            var template;
            if (data.jobid == undefined) {
                 template = JST['app/handlebars/file/list'];
            } else {
                 template = JST['app/handlebars/file/select'];
            }
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {jobid:this.jobid, files:this.files};
        },
        events: {
            'click #select_file':'selectFile',
            'click #select_file_cancel':'cancelSelect'
        },
        selectFile: function() {

        },
        cancelSelect: function() {

        }
    });

    var MyFileView = Marionette.ItemView.extend({
        initialize : function(options){
            this.files = options.files;
        },
        template : function(data){
            var template = JST['app/handlebars/file/mylist'];
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
        },
        selectFiles: function(jobid) {
            $.when(LaaS.request('file:entities'), LaaS.request('job:entity', {'id':jobid})).done(function(data, job) {
                var fileModel = new LaaS.FileModel();
                fileModel.url = job.attributes._links.files.href;
                fileModel.fetch({
                    success: function(model, reponse) {
                        var selectFiles = reponse._embedded.files;
                        for (var i=0; i<data.files.length; i++) {
                            for (var j=0; j<selectFiles.length; j++) {
                                data.files[i].selected = false;
                                data.files[i].checked = "";
                                if (data.files[i].id == selectFiles[j].id) {
                                    data.files[i].selected = true;
                                    data.files[i].checked = "checked";
                                    break;
                                }
                            }
                        }

                        var view = new FileListView({jobid:jobid, files:data.files});
                        LaaS.mainRegion.show(view);
                    },
                    error: function(err) {
                        console.log(err);
                    }
                });
            });
        },
        showMyFiles: function(){
            $.when(LaaS.request('myFiles:entities')).done(function(data){
                var view = new MyFileView({files:data.files});
                LaaS.mainRegion.show(view);
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'files(/)': 'showFiles',
                'files/:id(/)' : 'showFile',
                'myfiles(/)':'showMyFiles'
                //'fileselect/:jobid(/)' : 'selectFiles'
            },
            controller: new FileController()
        });
    });
});
