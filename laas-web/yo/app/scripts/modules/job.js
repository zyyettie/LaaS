LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;
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
            this.selectedScenarios = options.selectedScenarios;
            this.files = options.files;
        },
        serializeData: function () {
            var data = {job: this.job, scenarioList: this.scenarioList, fileList: this.fileList, selectedScenarios: this.selectedScenarios, files: this.files};
            if (data.job.selectedname == undefined) {
                data.job.selectedname = "Select Scenario";
            }
            data.job.scenarioList = data.scenarioList;
            data.job.files = data.files == undefined ? [] : data.files;

            var json = data.job.parameters == undefined ? {} : JSON.parse(data.job.parameters);
            data.job.N = json["N"];
            data.job.order = json["order"];
            data.job.desc = json["order"] == "desc" ? "selected='selected'" : "";
            data.job.asc = json["order"] == "asc" ? "selected='selected'" : "";
            if (data.selectedScenarios != undefined && data.selectedScenarios.length > 0 && data.selectedScenarios.length > 0) {
                for (var i = 0; i < data.scenarioList.length; i++) {
                    if (data.scenarioList[i].id == data.selectedScenarios[0].id) {
                        data.job.scenarioList[i].selected = "selected";
                        break;
                    }
                }
                data.job.selectedid = data.selectedScenarios[0].id;
                data.job.selectedname = data.selectedScenarios[0].name;
                data.job.selectedScenarios = data.selectedScenarios;
            }
           // }
            return data;
        },
        onRender: function () {
            this.$('select').dropdown();
            this.$('[name="selectedScenario"]').on('change', function () {
                if (this.options[this.selectedIndex].innerHTML == 'Scenario - Top N') {
                    var render = JST['app/handlebars/job/topN'];
                    var subhtml = render({N: 50, order: 'desc', desc: 'selected', asc: ''});
                    subhtml = subhtml.replace("selected=\"desc\"", "selected='selected'").replace("selected=\"asc\"", "");
                    $('#parameters').append(subhtml);
                } else {
                    $('#parameters').empty();
                }
            });
            this.$("button").on("click", function(){
                console.log("click detail...");
                this.$.attr("data-id", function(arr){
                    console.log(arr);
                });
            });

        },
        template: function (data) {
            var template;
            var html;
            if (data.job.id == undefined) {
                template = JST['app/handlebars/job/add'];
            } else {
                template = JST['app/handlebars/job/detail'];
            }
                html = template(data.job);
                if (data.job.selectedname == 'Scenario - Top N')  {
                    var render = JST['app/handlebars/job/topN'];
                    var subhtml = render(data.job);
                    subhtml = subhtml.replace("selected=\"desc\"", data.job.desc).replace("selected=\"asc\"", data.job.asc);
                    html = html.replace("<div id=\"parameters\"><\/div>", "<div id=\"parameters\">"+subhtml+"<\/div>");
                }

            return html;
        },
        events: {
            'click #job_save': 'saveJob',
            'click #job_run': 'runJob',
            'click #add_file': 'addFile',
            'click #job_show': 'showJob'
        },
        saveJob: function () {
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            if (json.name == '' || json.scenario == '') {
                toastr.error('Please input name and select scenario.');
                return;
            }
            json.parameters = JSON.stringify({N: json['N'], order: json['order']});
            //json.parameters = "{N:"+json['N']+", order:"+json['order']+"}";
            json.scenarios = [];
            json.scenarios.push(appContext+"/api/v1/scenarios/" + json.selectedScenario);

            json.files = [];
            for (var i=0; i<this.job.files.length; i++) {
                json.files.push(appContext+"/api/v1/files/"+this.job.files[i].id);
            }

            //this.model.url='/jobs/'+json.id;
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
            json.parameters = JSON.stringify({N: json['N'], order: json['order']});
            json.scenarios = [];
            json.scenarios.push(appContext+"/api/v1/scenarios/" + json.selectedScenario);

            json.files = [];
            for (var i=0; i<this.job.files.length; i++) {
                json.files.push(appContext+"/api/v1/files/"+this.job.files[i].id);
            }

            this.model.save(json, {patch: true, success: function (response) {
                $.getJSON(appContext+"/controllers/jobs/" + response.id).done(function (json) {
                        toastr.info('Save and Run Job successfully.');var job_running_id = json.job_running_id;
                    $.when(LaaS.request('jobRunning:entity',job_running_id))
                        .done(function (jobRunning) {
                            var jobResultView = new JobResultView(jobRunning);
                            LaaS.mainRegion.show(jobResultView);
                        });

                    LaaS.navigate('/jobs/showResult');
                    }).fail(function(json){
                        toastr.info('Failed due to '+json);
                    });

            }, error: function () {
                toastr.error('Save Job failed.');
            }});
        },
        addFile: function () {
            var thisjob = this.job;
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            json.parameters = JSON.stringify({N: json['N'], order: json['order']});
            thisjob = $.extend({}, thisjob, json);
            if (thisjob.selectedScenario != undefined && thisjob.selectedScenario.length > 0
                && (thisjob.selectedScenarios == undefined || thisjob.selectedScenarios.length<=0 || thisjob.scenarioList[thisjob.selectedScenario].id != thisjob.selectedScenarios[0].id)) {
                thisjob.selectedScenarios = [];
                thisjob.selectedScenarios.push(thisjob.scenarioList[thisjob.selectedScenario - 1]);
                thisjob.selectedid = thisjob.selectedScenarios[0].id;
                thisjob.selectedname = thisjob.selectedScenarios[0].name;
            }
            $.when(LaaS.request('file:entities')).done(function(data) {
                var fileSelectView = new LaaS.File.FileSelectView({job:thisjob, files:data.files, jobmodel:that.model});
                LaaS.mainRegion.show(fileSelectView);
                if (thisjob.id == undefined) {
                    LaaS.navigate('/jobnew/fileselect');
                } else {
                    LaaS.navigate('/jobs/'+thisjob.id+'/fileselect');
                }
            }).fail(function() {
                toastr.error('Cannot load files.');
            });
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
        }
    });

    var JobResultView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.jobRunning = options.attributes;
        },
        template: function (data) {
            var template = JST['app/handlebars/job/result'];
            var html = template(data.jobRunning);
            return html;
        },
        serializeData: function () {
            return {jobRunning: this.jobRunning};
        }
    });

    var JobController = Marionette.Controller.extend({
        jobnew: function () {
            $.when(LaaS.request('job:new'), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function (job, scenario, file) {
                    //LaaS.mainRegion.show(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                    LaaS.Home.showViewFrame(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                });
        },
        showJob: function (id) {
            $.when(LaaS.request('job:entity', {'id':id}), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function(jobModel, scenarioList, fileList){
                var scenarioModel = new LaaS.Entities.ScenarioModel();
                scenarioModel.url = jobModel.attributes._links.scenarios.href;
                scenarioModel.fetch({
                    success: function(model, response) {
                        var selectedScenarios = response._embedded.scenarios;
                        var fileModel = new LaaS.Entities.FileModel();
                        fileModel.url = jobModel.attributes._links.files.href;
                        fileModel.fetch({
                            success: function(model, response) {
                                var selectedFiles = [];
                                if (response._embedded != undefined && response._embedded != null) {
                                    selectedFiles = response._embedded.files;
                                }
                                var view = new LaaS.Job.JobView({model:jobModel, job:jobModel.attributes, scenarioList:scenarioList.scenarios,
                                    fileList:fileList.files, selectedScenarios:selectedScenarios, files:selectedFiles});
                                LaaS.Home.showViewFrame(view);
                            },
                            error: function(err) {
                                console.log(err);
                            }
                        })
                    },
                    error: function(err) {
                        console.log(err);
                    }
                })
            });
        },
        showJobs: function () {
            $.when(LaaS.request('job:entities')).done(function (data) {
                var view = new JobListView(data);
                LaaS.Home.showViewFrame(view);
            });
        }
    });


    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'jobnew(/)': 'jobnew',
                'jobs(/)': 'showJobs',
                'jobs/:id(/)': 'showJob'
            },
            controller: new JobController()
        });
    });
});
