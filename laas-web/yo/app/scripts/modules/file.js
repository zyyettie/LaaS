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

    File.FileSelectView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.files = options.data.files;
            this.paging = options.data.page;
            this.job = options.job;
            this.jobmodel = options.jobmodel;
        },
        template: JST['app/handlebars/file/layout'],
        onRender: function () {
            var template = JST[baseTemplatePath + '/select'];
            var html = template({files: this.files});
            this.$('#content').html(html);
            if (this.paging.number + 1 <= this.paging.totalPages) {
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
                this.files[i].selected = false;
                this.files[i].checked = "";
                for (var j = 0; j < this.job.files.length; j++) {
                    if (this.files[i].id == this.job.files[j].id) {
                        this.files[i].selected = true;
                        this.files[i].checked = 'checked=""';
                        break;
                    }
                }
            }
            return {files: this.files};
        },
        events: {
            'click #select_file': 'selectFile',
            'click #select_file_cancel': 'cancelSelect'
        },
        selectFile: function () {
            var selectFiles = [];
            for (var i = 0; i < this.files.length; i++) {
                var fileid = this.files[i].id;
                if ($("#file_checkbox_" + fileid).prop("checked")) {
                    selectFiles.push(this.files[i]);
                }
            }

            var jobView = new LaaS.Job.JobView({model: this.jobmodel, job: this.job, scenarioList: this.job.scenarioList,
                selectedScenarios: this.job.selectedScenarios, files: selectFiles, selectedParameterDefines: this.job.selectedParameterDefines});
            LaaS.mainRegion.show(jobView);
            if (this.job.id == undefined) {
                LaaS.navigate('/jobnew');
            } else {
                LaaS.navigate('/jobs/' + this.job.id);
            }
        },
        cancelSelect: function () {
            var jobView = new LaaS.Job.JobView({model: this.jobmodel, job: this.job, scenarioList: this.job.scenarioList,
                selectedScenarios: this.job.selectedScenarios, files: this.job.files, selectedParameterDefines: this.job.selectedParameterDefines});
            LaaS.mainRegion.show(jobView);
            if (this.job.id == undefined) {
                LaaS.navigate('/jobnew');
            } else {
                LaaS.navigate('/jobs/' + this.job.id);
            }
        }
    });

    File.MyFileView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.files = options.files;
            this.paging = options.page;
            this.selectFiles = [];
        },
        template: JST['app/handlebars/file/layout'],
        onRender: function () {
            var that = this;
            var template = JST[baseTemplatePath + '/mylist'];
            var html = template({files: this.files});
            this.$('#content').html(html);
            if (this.paging.number + 1 <= this.paging.totalPages) {
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
                this.files[i].size = Math.round(this.files[i].size / 1024);
            }
            return {files: this.files};
        },
        events: {
            'click #upload_my_files': 'uploadMyFiles',
            'click #delete_my_files': 'deleteMyFiles',
            'click [type="checkbox"]':'clickCheckbox'
        },
        clickCheckbox: function(){
            //here is to put all the items checked on different pages in selectFiles
            for (var i = 0; i < this.files.length; i++) {
                var isChecked = $("#file_checkbox_" + this.files[i].id).prop("checked");
                if (!isChecked) {
                    //need to see if this item has been pushed to the selectFiles before, if so, remove it
                    for (var j = 0; j < this.selectFiles.length; j++) {
                        if (this.selectFiles[j].id == this.files[i].id) {
                            this.selectFiles.splice(j, 1);
                            break;
                        }
                    }
                } else {
                    //meaning the item has been selected on the page
                    var isPushed = false;
                    for (var j = 0; j < this.selectFiles.length; j++) {
                        if (this.selectFiles[j].id == this.files[i].id) {
                            isPushed = true;
                            break;
                        }
                    }
                    if (!isPushed) {
                        this.selectFiles.push(this.files[i]);
                    }
                }
            }
        },
        uploadMyFiles: function () {
            $.when(LaaS.request('fileType:entities')).done(function (data) {
                LaaS.mainRegion.show(new LaaS.Views.FileUploader({'url': '/files/me', 'fileTypes': data.fileTypes}));
                LaaS.navigate('/files/me/upload');
            });

        },
        deleteMyFiles: function () {
            $.ajax({
                type: "POST",
                url: appContext + "/controllers/files",
                data: JSON.stringify(this.selectFiles),
                success: function (data) {
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
