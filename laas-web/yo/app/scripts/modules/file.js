LaaS.module('File', function (File, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;
    var baseTemplatePath = 'app/handlebars/file';

    var FileView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.file = options.attributes;
        },
        template: function (data) {
            var template = JST['app/handlebars/file/detail'];
            var html = template(data.file);
            return html;
        },
        serializeData: function () {
            return {file: this.file};
        }
    });


    var checkSelected = function(that){
        //here is to put all the items checked on different pages in selectFiles
        for (var i = 0; i < that.files.length; i++) {
            var isChecked = $("#file_checkbox_" + that.files[i].id).prop("checked");
            if (!isChecked) {
                //need to see if this item has been pushed to the selectFiles before, if so, remove it
                for (var j = 0; j < that.selectFiles.length; j++) {
                    if (that.selectFiles[j].id == that.files[i].id) {
                        that.selectFiles.splice(j, 1);
                        break;
                    }
                }
            } else {
                //meaning the item has been selected on the page
                var isPushed = false;
                for (var j = 0; j < that.selectFiles.length; j++) {
                    if (that.selectFiles[j].id == that.files[i].id) {
                        isPushed = true;
                        break;
                    }
                }
                if (!isPushed) {
                    that.selectFiles.push(that.files[i]);
                }
            }
        }
    };

    File.FileSelectView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.files = options.data.files;
            this.paging = options.data.page;
            var jobinfo = sessionStorage.getItem('jobinfo');
            if (jobinfo) {
                jobinfo = JSON.parse(jobinfo);
                this.job = jobinfo;
                //this.jobmodel = jobinfo.jobmodel;
                this.selectFiles = this.job.files;
            }

            this.uploadedFiles = [];
            var uploadedFiles = sessionStorage.getItem('uploadedFiles');
            if (uploadedFiles) {
                uploadedFiles = JSON.parse(uploadedFiles);
                $.extend(this.uploadedFiles, uploadedFiles);
            }
        },
        template: JST['app/handlebars/file/layout'],
        onRender: function () {
            var that = this;
            var template = JST[baseTemplatePath + '/content'];
            var html = template({files: this.files});
            this.$('#content').html(html);
            this.$('#buttons').html(JST[baseTemplatePath + '/select']);
            if (this.paging.number + 1 < this.paging.totalPages) {
                this.$('#paging').twbsPagination({
                    totalPages: this.paging.totalPages,
                    startPage: this.paging.number + 1,
                    visiblePages: 6,
                    first: '<<',
                    prev: '<',
                    next: '>',
                    last: '>>',
                    onPageClick: function (event, page) {
                        $.when(LaaS.request('myFiles:entities', {page: page - 1})).done(function (data) {
                            that.files = data.files;

                            for (var i = 0; i < data.files.length; i++) {
                                data.files[i].selected = false;
                                data.files[i].checked = "";
                                for (var j = 0; j < that.selectFiles.length; j++) {
                                    if (data.files[i].id == that.selectFiles[j].id) {
                                        data.files[i].selected = true;
                                        data.files[i].checked = 'checked=""';
                                        break;
                                    }
                                }
                            }
                            var html = template({files: data.files});
                            $('#content').html(html);
                        });
                    }
                })
            }
        },
        serializeData: function () {
            if (this.job.files == undefined) {
                return {files: this.files};
            }
            for (var i = 0; i < this.files.length; i++) {
                this.files[i].displaySize = LaaS.Util.getFileDisplaySize(this.files[i].size);
                this.files[i].selected = false;
                this.files[i].checked = "";
                for (var j = 0; j < this.job.files.length; j++) {
                    if (this.files[i].id == this.job.files[j].id) {
                        this.files[i].selected = true;
                        this.files[i].checked = 'checked=""';
                        break;
                    }
                }

                if (!this.files[i].selected) {
                    for (var j=0; j < this.uploadedFiles.length; j++) {
                        if (this.files[i].id == this.uploadedFiles[j].id) {
                            this.selectFiles.push(this.files[i]);
                            this.files[i].selected = true;
                            this.files[i].checked = 'checked=""';
                            break;
                        }
                    }
                }
            }
            return {files: this.files};
        },
        events: {
            'click #select_file': 'selectFile',
            'click #select_file_cancel': 'cancelSelect',
            'click #upload_file':'uploadFile',
            'click [type="checkbox"]':'clickCheckbox'
        },
        clickCheckbox: function(){
            checkSelected(this);
        },
        selectFile: function () {
            if (!this.job.id) {
                var jobView = new LaaS.Job.JobView({model: new LaaS.Entities.JobModel(), job: this.job, scenarioList: this.job.scenarioList,
                    selectedScenarios: this.job.selectedScenarios, files: this.selectFiles, inputParameterDefs: this.job.inputParameterDefs,
                    fileTypes:this.job.fileTypes});
                LaaS.mainRegion.show(jobView);
                LaaS.navigate('/jobnew');
                sessionStorage.removeItem('jobinfo');
                sessionStorage.removeItem('uploadedFiles');
            } else {
                var that = this;
                $.when(LaaS.request('job:entity', {id:this.job.id})).done(function(jobModel) {
                    var jobView = new LaaS.Job.JobView({model: jobModel, job: that.job, scenarioList: that.job.scenarioList,
                        selectedScenarios: that.job.selectedScenarios, files: that.selectFiles, inputParameterDefs: that.job.inputParameterDefs,
                        fileTypes:that.job.fileTypes});
                    LaaS.mainRegion.show(jobView);
                    LaaS.navigate('/jobs/' + that.job.id);
                    sessionStorage.removeItem('jobinfo');
                    sessionStorage.removeItem('uploadedFiles');
                });
            }
        },
        cancelSelect: function () {
            var jobView = new LaaS.Job.JobView({model: new LaaS.Entities.JobModel(), job: this.job, scenarioList: this.job.scenarioList,
                selectedScenarios: this.job.selectedScenarios, files: this.job.files, inputParameterDefs: this.job.inputParameterDefs,
                fileTypes:this.job.fileTypes});
            LaaS.mainRegion.show(jobView);
            if (!this.job.id) {
                LaaS.navigate('/jobnew');
            } else {
                LaaS.navigate('/jobs/' + this.job.id);
            }
            sessionStorage.removeItem('jobinfo');
            sessionStorage.removeItem('uploadedFiles');
        },
        uploadFile: function() {
            if (!this.job) {
                return;
            }
            var url = '/jobnew/fileselect';
            if (this.job.id) {
                url = '/jobs/'+this.job.id+'/fileselect';
            }

            if (this.job.fileTypes) {
                LaaS.mainRegion.show(new LaaS.Views.FileUploader({'url': url, 'fileTypes': this.job.fileTypes}));
                LaaS.navigate(url + '/upload');
            } else {
                $.when(LaaS.request('fileType:entities')).done(function (data) {
                    LaaS.mainRegion.show(new LaaS.Views.FileUploader({'url': url, 'fileTypes': data.fileTypes}));
                    LaaS.navigate(url + '/upload');
                });
            }
        }
    });


    File.MyFileView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.files = options.files;
            this.paging = options.page;
            this.selectFiles = [];
            sessionStorage.removeItem('uploadedFiles');
        },
        template: JST['app/handlebars/file/layout'],
        onRender: function () {
            var that = this;
            var template = JST[baseTemplatePath + '/content'];
            var html = template({files: this.files});
            this.$('#content').html(html);
            this.$('#buttons').html(JST[baseTemplatePath + '/mylist']);
            if (this.paging.number + 1 < this.paging.totalPages) {
                this.$('#paging').twbsPagination({
                    totalPages: this.paging.totalPages,
                    startPage: this.paging.number + 1,
                    visiblePages: 6,
                    first: '<<',
                    prev: '<',
                    next: '>',
                    last: '>>',
                    onPageClick: function (event, page) {
                        $.when(LaaS.request('myFiles:entities', {page: page - 1})).done(function (data) {
                            that.files = data.files;

                            for (var i = 0; i < data.files.length; i++) {
                                data.files[i].selected = false;
                                data.files[i].checked = "";
                                for (var j = 0; j < that.selectFiles.length; j++) {
                                    if (data.files[i].id == that.selectFiles[j].id) {
                                        data.files[i].selected = true;
                                        data.files[i].checked = 'checked=""';
                                        break;
                                    }
                                }
                            }
                            var html = template({files: data.files});
                            $('#content').html(html);
                        });
                    }
                })
            }
        },
        serializeData: function () {
            for (var i = 0; i < this.files.length; i++) {
                this.files[i].displaySize = LaaS.Util.getFileDisplaySize(this.files[i].size);
            }
            return {files: this.files};
        },
        events: {
            'click #upload_my_files': 'uploadMyFiles',
            'click #delete_my_files': 'deleteMyFiles',
            'click [type="checkbox"]':'clickCheckbox'
        },
        clickCheckbox: function(){
            checkSelected(this);
        },
        uploadMyFiles: function () {
            $.when(LaaS.request('fileType:entities')).done(function (data) {
                LaaS.mainRegion.show(new LaaS.Views.FileUploader({'url': '/files/me', 'fileTypes': data.fileTypes}));
                LaaS.navigate('/files/me/upload');
            });

        },
        deleteMyFiles: function () {
            if(this.selectFiles.length == 0){
                toastr.error('Please select files that you want to delete');
                return;
            }
            $.ajax({
                type: "POST",
                url: appContext + "/controllers/files",
                data: JSON.stringify(this.selectFiles),
                success: function (data) {
                    toastr.info('Delete files successfully');
                    $.when(LaaS.request('myFiles:entities')).done(function (data) {
                        var view = new LaaS.File.MyFileView(data);
                        LaaS.mainRegion.show(view);
                    });
                },
                dataType: "json",
                contentType: "application/json"
            });
        }
    });

    var FileController = Marionette.Controller.extend({
        showFile: function (id) {
            $.when(LaaS.request('file:entity', {'id': id})).done(function (data) {
                var view = new FileView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showMyFiles: function () {
            $.when(LaaS.request('myFiles:entities')).done(function (data) {
                var view = new LaaS.File.MyFileView(data);
                LaaS.mainRegion.show(view);
            });
        }
    });

    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'files/me(/)': 'showMyFiles',
                'files/:id(/)': 'showFile'
            },
            controller: new FileController()
        });
    });
});
