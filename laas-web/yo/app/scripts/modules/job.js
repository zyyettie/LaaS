LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;
    var apiVersion = LaaS.Util.Constants.APIVERSION;
    Job.JobView = Marionette.ItemView.extend({
        initialize: function (options) {
            if (this.model == undefined) {
                this.model = options.model;
            }

            if (options.job == undefined) {
                this.job = {};
            } else {
                this.job = jQuery.extend({}, options.job);
            }
            this.scenarioList = options.scenarioList;
            this.fileList = options.fileList;
            this.scenario = options.scenario;
            this.files = options.files;
            this.selectedParameterDefines = options.selectedParameterDefines;
            this.fileTypes = options.fileTypes;
        },
        serializeData: function () {
            var data = {job: this.job, scenarioList: this.scenarioList, fileList: this.fileList,
                scenario: this.scenario, files: this.files,
                selectedParameterDefines: this.selectedParameterDefines, fileTypes: this.fileTypes};
            if (!data.job.selectedname) {
                data.job.selectedname = "Select Scenario";
            }
            data.job.scenarioList = data.scenarioList;
            data.job.files = data.files ? data.files : [];
            data.job.selectedParameterDefines = data.selectedParameterDefines ? data.selectedParameterDefines : [];
            data.job.fileTypes = data.fileTypes ? data.fileTypes : [];

            var json = data.job.parameters ? JSON.parse(data.job.parameters) : {};
            $.extend(data.job, json);

            if (data.scenario) {
                for (var i = 0; i < data.scenarioList.length; i++) {
                    if (data.scenarioList[i].id == data.scenario.id) {
                        data.job.scenarioList[i].selected = "selected";
                        break;
                    }
                }
                data.job.selectedid = data.scenario.id;
                data.job.selectedname = data.scenario.name;
                data.job.scenario = data.scenario;
            }
           // }
            return data;
        },
        onRender: function () {
            var that = this;
            if (!this.job.id) {
                this.$('#title').text('New Job');
            }
            if (this.job.scenario && this.job.files && this.job.files.length > 0) {
                this.$('#selectedScenario').prop("disabled", "true");
            } else {
                this.$('#selectedScenario').removeProp("disabled");
            }
            this.$('select').dropdown();

            var json = this.job.parameters ? JSON.parse(this.job.parameters) : {};
            var subHtml = LaaS.Form.generateParameterSubForm(this.selectedParameterDefines, json);
            this.$('#parameters').html(subHtml);

            if (this.job.files && this.job.files.length > 0 ) {
                var size = 10;
                var currentFiles = [];
                for (var i=0; i<size && i<this.job.files.length; i++) {
                    currentFiles.push(this.job.files[i]);
                }
                var template = JST['app/handlebars/job/file'];
                var subHtml = template({files:currentFiles});
                this.$('#files').html(subHtml);
                var totalPages = Math.floor(this.job.files.length / size) + 1;
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
                            //var size = 10;
                            var currentFiles = [];
                            for (var i=size*(page-1); i<size*page; i++) {
                                if (!that.job.files[i]) {
                                    break;
                                }
                                currentFiles.push(that.job.files[i]);
                            }
                            var template = JST['app/handlebars/job/file'];
                            var html = template({files: currentFiles});
                            $('#files').html(html);
                        }
                    })
                }
            }
            this.$('[name="selectedScenario"]').on('change', function () {
                var id = this.selectedOptions[0].value;
                for (var i=0; i<that.scenarioList.length; i++) {
                    if (that.scenarioList[i].id == id) {
                        var parameterDefinesUrl = that.scenarioList[i]._links.parameterDefines.href;
                        var fileTypeUrl = that.scenarioList[i]._links.fileTypes.href;
                        $.when(LaaS.request("parameterDefine:entitiesByUrl", {url: parameterDefinesUrl}),
                            LaaS.request('fileType:entitiesByUrl', {url: fileTypeUrl}))
                            .done(function(selectedParameterDefines, fileTypes) {
                            that.job.selectedParameterDefines = selectedParameterDefines.parameterDefines;
                            var json = that.job.parameters ? JSON.parse(that.job.parameters) : {};
                            var subHtml = LaaS.Form.generateParameterSubForm(selectedParameterDefines.parameterDefines, json);
                            $('#parameters').html(subHtml);
                            LaaS.Form.enableDatetimeControl(selectedParameterDefines.parameterDefines);

                            that.job.fileTypes = fileTypes.fileTypes;
                        });
                        break;
                    }
                }
            });
            this.$("button").on("click", function(){
                console.log("click detail...");
                this.$.attr("data-id", function(arr){
                    console.log(arr);
                });
            });
            this.$("#remove_file").on("click", function() {
                var id = this.attributes["value"]["value"];
                that.removeFile(id);
                $('#fileitem_'+id).empty();
                if (!that.job.files || that.job.files.length == 0) {
                    $('#files').empty();
                    $('#paging').empty();
                    $('#selectedScenario').parent().removeClass('disabled');
                }
            })

        },
        template: function (data) {
            var template = JST['app/handlebars/job/detail'];

            var html = template(data.job);

            var json = data.job.parameters ? JSON.parse(data.job.parameters) : {};
            var subHtml = LaaS.Form.generateParameterSubForm(data.selectedParameterDefines, json);

            html = html.replace('<div id="parameters"></div>', '<div id="parameters">'+subHtml+'</div>');

            return html;
        },
        events: {
            'click #job_save': 'saveJob',
            'click #job_run': 'runJob',
            'click #add_file': 'addFile'
        },
        getParameter: function (json, defines) {
            if (!defines) {
                return "{}";
            }

            var strJson = "{";
            for (var i=0; i<defines.length; i++) {
                if (i>0) {
                    strJson += ", ";
                }
                strJson += '"'+defines[i]["name"] + '":"' + json[defines[i]["name"]]+'"';
            }
            strJson += "}";

            return strJson;
        },
        saveJob: function () {
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            if (json.name == '' || json.scenario == '') {
                toastr.error('Please input name and select scenario.');
                return;
            }

            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);

            json.scenario = appContext+apiVersion+"/scenarios/" + json.selectedScenario;

            json.files = [];
            for (var i=0; i<this.job.files.length; i++) {
                json.files.push(appContext+apiVersion+"/files/"+this.job.files[i].id);
            }

            this.model.save(json, {patch: true, success: function () {
                toastr.info('Save Job successfully.');
                that.job.id = that.model.id;
                LaaS.navigate('/jobs/' + that.model.id + '/edit');
                $('#title').text('Editing Job');

            }, error: function () {
                toastr.error('Save Job failed.');
            }});
        },
        runJob: function () {
            console.log("save & run the job");
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            if (json.name == '' || json.scenario == '') {
                toastr.error('Please input name and select scenario.');
                return;
            }
            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);
            json.scenario = appContext+apiVersion+"/scenarios/" + json.selectedScenario;

            json.files = [];
            for (var i=0; i<this.job.files.length; i++) {
                json.files.push(appContext+apiVersion+"/files/"+this.job.files[i].id);
            }

            this.model.save(json, {patch: true, success: function (response) {
                $.getJSON(appContext+"/controllers/jobs/" + response.id).done(function (json) {
                    toastr.info('Save and Run Job successfully.');
                    var success = json.success;
                    if(success === true){
                        var sync = json.is_syn;
                        if(sync === true){
                            $.when(LaaS.request('jobResult:entity',{id:json.job_running_id}))
                                .done(function (jobRunningResult) {
                                    var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult,sync:true});
                                    LaaS.mainRegion.show(jobResultView);
                                });

                        }else{
                            var jobResultView = new LaaS.JobResult.JobResultView({sync:false});
                            LaaS.mainRegion.show(jobResultView);
                        }
                    }else{
                        var rootcauses = json.rootcauses;
                        var jobResultView = new LaaS.JobResult.JobResultView({success:false,rootcauses:rootcauses});
                        LaaS.mainRegion.show(jobResultView);
                    }
                    LaaS.navigate('/jobRunnings/'+json.job_running_id+"/result");
                    }).fail(function(json){
                        toastr.info('Failed due to '+json);
                    });

            }, error: function () {
                toastr.error('Save Job failed.');
            }});
        },
        addFile: function () {
            if (!this.job.fileTypes || this.job.fileTypes.length == 0) {
                toastr.info('Please select one Scenario!');
                return;
            }
            var thisjob = this.job;
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);
            $.extend(thisjob, json);
            if (thisjob.selectedScenario != undefined && thisjob.selectedScenario.length > 0
                && (!thisjob.scenario || thisjob.scenarioList[thisjob.selectedScenario].id != thisjob.scenario.id)) {
                thisjob.scenario = thisjob.scenarioList[thisjob.selectedScenario - 1];
                thisjob.selectedid = thisjob.scenario.id;
                thisjob.selectedname = thisjob.scenario.name;
            }
            $.when(LaaS.request('myFiles:entities')).done(function(data) {
                sessionStorage.setItem('jobinfo', JSON.stringify(thisjob));
                var fileSelectView = new LaaS.File.FileSelectView({data:data});
                LaaS.mainRegion.show(fileSelectView);
                if (thisjob.id == undefined) {
                    LaaS.navigate('/jobnew/fileselect');
                } else {
                    LaaS.navigate('/jobs/'+thisjob.id+'/fileselect');
                }
            }).fail(function() {
                toastr.error('Cannot load files.');
            });
        },
        removeFile : function(id) {
            for (var i=0; i<this.files.length; i++) {
                if (this.files[i]["id"] == id) {
                    this.files.splice(i, 1);
                    break;
                }
            }
        }
    });

    var JobListView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.jobs = options.jobs;
        },
        template: function (data) {
            var template = JST['app/handlebars/job/list'];
            var html = template(data);
            return html;
        },
        serializeData: function () {
            return {jobs: this.jobs};
        },
        events:{
            'click button.job-show': "showClicked"
        },
        showClicked: function(event){
            var jobId = event.target.dataset["id"];
            LaaS.navigate('/jobs/'+jobId,true);
        }
    });

    var JobController = Marionette.Controller.extend({
        jobnew: function () {
            $.when(LaaS.request('job:new'), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function (job, scenario, file) {
                    LaaS.mainRegion.show(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                    //LaaS.Home.showViewFrame(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                });
        },
        showJob: function (id) {
            $.when(LaaS.request('job:entity', {'id':id}), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function(jobModel, scenarioList, fileList){
                    $.when(LaaS.request("scenario:entityByUrl", {"url":jobModel.attributes._links.scenario.href}), LaaS.request("file:entitiesByUrl", {"url":jobModel.attributes._links.files.href}))
                        .done(function(scenario, selectedFiles) {
                            if (scenario) {
                                $.when(LaaS.request("parameterDefine:entitiesByUrl", {"url":scenario._links.parameterDefines.href}),
                                    LaaS.request("fileType:entitiesByUrl", {url:scenario._links.fileTypes.href}))
                                    .done(function(selectedParameterDefines, fileTypes) {
                                    var view = new LaaS.Job.JobView({model:jobModel, job:jobModel.attributes, scenarioList:scenarioList.scenarios,
                                        fileList:fileList.files, scenario:scenario, files:selectedFiles.files,
                                        selectedParameterDefines:selectedParameterDefines.parameterDefines, fileTypes:fileTypes.fileTypes});
                                    LaaS.mainRegion.show(view);
                                });
                            }
                        });
                });
        },
        showJobs: function () {
            $.when(LaaS.request('job:entities')).done(function (data) {
                var view = new JobListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        selectJobFiles: function(id) {
            var jobStr = sessionStorage.getItem('jobinfo');
            if (jobStr) {
                var jobJson = JSON.parse(jobStr);
                if (id == jobJson.id) {
                    $.when(LaaS.request('myFiles:entities')).done(function(data) {
                        var fileSelectView = new LaaS.File.FileSelectView({data:data});
                        LaaS.mainRegion.show(fileSelectView);
                    }).fail(function() {
                            toastr.error('Cannot load files.');
                    });
                } else {
                    toastr.error('Cannot load files.');
                    sessionStorage.removeItem('jobinfo');
                }
            } else {
                toastr.error('Cannot load files.');
            }
        }
    });


    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'jobnew/fileselect': 'selectJobFiles',
                'jobs/:id/fileselect' : 'selectJobFiles',
                'jobnew(/)': 'jobnew',
                'jobs(/)': 'showJobs',
                'jobs/:id(/)': 'showJob',
                'jobs/:id/edit(/)': 'showJob'
            },
            controller: new JobController()
        });
    });
});
