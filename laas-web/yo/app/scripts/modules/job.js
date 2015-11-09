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
            this.selectedParameterDefines = options.selectedParameterDefines;
        },
        serializeData: function () {
            var data = {job: this.job, scenarioList: this.scenarioList, fileList: this.fileList, selectedScenarios: this.selectedScenarios, files: this.files, selectedParameterDefines: this.selectedParameterDefines};
            if (data.job.selectedname == undefined) {
                data.job.selectedname = "Select Scenario";
            }
            data.job.scenarioList = data.scenarioList;
            data.job.files = data.files ? data.files : [];
            data.job.selectedParameterDefines = data.selectedParameterDefines ? data.selectedParameterDefines : [];

            var json = data.job.parameters ? JSON.parse(data.job.parameters) : {};
            $.extend(data.job, json);
            /*
            data.job.N = json["N"];
            data.job.order = json["order"];
            data.job.desc = json["order"] == "desc" ? "selected='selected'" : "";
            data.job.asc = json["order"] == "asc" ? "selected='selected'" : "";
            data.job.category = json["category"];
            data.job.user = json['user'];
            data.job.startTime = json['startTime'];
            data.job.endTime = json['endTime'];
            */
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
            var that = this;
            this.$('select').dropdown();
            this.$('[name="selectedScenario"]').on('change', function () {
                $('#parameters').empty();

                var id = this.selectedOptions[0].value;
                for (var i=0; i<that.scenarioList.length; i++) {
                    if (that.scenarioList[i].id == id) {
                        var url = that.scenarioList[i]._links.parameterDefines.href;
                        $.when(LaaS.request("parameterDefine:entitiesByUrl", {url: url})).done(function(selectedParameterDefines) {
                            that.job.selectedParameterDefines = selectedParameterDefines.parameterDefines;
                            var json = that.job.parameters ? JSON.parse(that.job.parameters) : {};
                            var subHtml = LaaS.Form.generateParameterSubForm(selectedParameterDefines.parameterDefines, json);
                            $('#parameters').append(subHtml);
                        });
                        break;
                    }
                }

                this.$('#datetimepicker').datetimepicker();
                /*
                var render;
                var subhtml;
                if (this.options[this.selectedIndex].innerHTML == 'Scenario - Top N') {
                    render = JST['app/handlebars/job/topN'];
                    subhtml = render({N: 50, order: 'desc'});
                    subhtml = subhtml.replace("selected=\"desc\"", "selected='selected'").replace("selected=\"asc\"", "");
                    subhtml = subhtml.replace("selected=\"DBQUERY\"", "selected='selected'").replace("selected=\"SCRIPTTRACE\"", "");
                    $('#parameters').append(subhtml);
                } else if (this.options[this.selectedIndex].innerHTML == 'Scenario - Login Time') {
                    render = JST['app/handlebars/job/loginTime'];
                    subhtml = render({});
                    $('#parameters').append(subhtml);
                }   */
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
            })

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

            var json = data.job.parameters ? JSON.parse(data.job.parameters) : {};
            var subHtml = LaaS.Form.generateParameterSubForm(data.selectedParameterDefines, json);
            /*
            var render;
            var subhtml = "";
            if (data.job.selectedname == 'Scenario - Top N')  {
                render = JST['app/handlebars/job/topN'];
                subhtml = render(data.job);
                var desc =  LaaS.Form.getDropDownSelected(data.job.order, "DESC");
                var asc =  LaaS.Form.getDropDownSelected(data.job.order, "ASC");
                subhtml = subhtml.replace("selected=\"desc\"", desc).replace("selected=\"asc\"", asc);
                var dbquery = LaaS.Form.getDropDownSelected(data.job.category, "DBQUERY");
                var scripttrace = LaaS.Form.getDropDownSelected(data.job.category, "SCRIPTTRACE");
                subhtml = subhtml.replace("selected=\"DBQUERY\"",dbquery).replace("selected=\"SCRIPTTRACE\"", scripttrace);
            } else if (data.job.selectedname == 'Scenario - Login Time') {
                render = JST['app/handlebars/job/loginTime'];
                subhtml = render(data.job);
            }  */

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
            //return JSON.stringify({N: json['N'], order: json['order'], category: json['category'], user: json['user'], startTime: json['startTime'], endTime: json['endTime']});
        },
        saveJob: function () {
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            if (json.name == '' || json.scenario == '') {
                toastr.error('Please input name and select scenario.');
                return;
            }
            //json.parameters = JSON.stringify({N: json['N'], order: json['order']});
            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);
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
            //json.parameters = JSON.stringify({N: json['N'], order: json['order'], category: json['category']});
            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);
            json.scenarios = [];
            json.scenarios.push(appContext+"/api/v1/scenarios/" + json.selectedScenario);

            json.files = [];
            for (var i=0; i<this.job.files.length; i++) {
                json.files.push(appContext+"/api/v1/files/"+this.job.files[i].id);
            }

            this.model.save(json, {patch: true, success: function (response) {
                $.getJSON(appContext+"/controllers/jobs/" + response.id).done(function (json) {
                    toastr.info('Save and Run Job successfully.');
                    var sync = json.is_syn;
                    if(sync === true){
                        $.when(LaaS.request('jobRunning:entity',{id:json.job_running_id}))
                            .done(function (jobRunningResult) {
                                var jobResultView = new JobResultView({model:jobRunningResult,sync:true});
                                LaaS.mainRegion.show(jobResultView);
                            });

                    }else{
                        var jobResultView = new JobResultView({sync:false});
                        LaaS.mainRegion.show(jobResultView);
                    }
                    LaaS.navigate('/jobResults/'+json.job_running_id);
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
            //json.parameters = JSON.stringify({N: json['N'], order: json['order']});
            json.parameters = this.getParameter(json, that.job.selectedParameterDefines);
            $.extend(thisjob, json);
            //thisjob = $.extend({}, thisjob, json);
            if (thisjob.selectedScenario != undefined && thisjob.selectedScenario.length > 0
                && (thisjob.selectedScenarios == undefined || thisjob.selectedScenarios.length<=0 || thisjob.scenarioList[thisjob.selectedScenario].id != thisjob.selectedScenarios[0].id)) {
                thisjob.selectedScenarios = [];
                thisjob.selectedScenarios.push(thisjob.scenarioList[thisjob.selectedScenario - 1]);
                thisjob.selectedid = thisjob.selectedScenarios[0].id;
                thisjob.selectedname = thisjob.selectedScenarios[0].name;
            }
            $.when(LaaS.request('myFiles:entities')).done(function(data) {
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

    var JobResultView = Marionette.ItemView.extend({
        initialize: function (options) {
            if(options.sync === true){
                this.sync = true;
                this.jobRunning = options.model.attributes;
            }
        },
        template: function(){
            var template = JST['app/handlebars/job/result'];
            var html = template();
            //debug
            return html;
        } ,
        serializeData: function(){
            return {};
        },
        onRender: function(){
            if(this.sync === true){
                this.$('#content-placeholder').html(this.jobRunning.desc).text();
            }else{
                this.$('#content-placeholder').html('<h2 class="ui header">Your job is running in the background, please check your inbox later</div>');
            }
//            this.$('#content-placeholder').html(decoded);
        }
    });

    var JobController = Marionette.Controller.extend({
        jobnew: function () {
            $.when(LaaS.request('job:new'), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function (job, scenario, file) {
                    LaaS.mainRegion.show(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                    //LaaS.Home.showViewFrame(new LaaS.Job.JobView({model: job, scenarioList: scenario.scenarios, fileList: file.files}));
                });
        },/*
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
                                LaaS.mainRegion.show(view);
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
        },*/
        showJob: function (id) {
            $.when(LaaS.request('job:entity', {'id':id}), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function(jobModel, scenarioList, fileList){
                    $.when(LaaS.request("scenario:entitiesByUrl", {"url":jobModel.attributes._links.scenarios.href}), LaaS.request("file:entitiesByUrl", {"url":jobModel.attributes._links.files.href}))
                        .done(function(selectedScenarios, selectedFiles) {
                            if (selectedScenarios) {
                                $.when(LaaS.request("parameterDefine:entitiesByUrl", {"url":selectedScenarios.scenarios[0]._links.parameterDefines.href})).done(function(selectedParameterDefines) {
                                    var view = new LaaS.Job.JobView({model:jobModel, job:jobModel.attributes, scenarioList:scenarioList.scenarios,
                                        fileList:fileList.files, selectedScenarios:selectedScenarios.scenarios, files:selectedFiles.files, selectedParameterDefines:selectedParameterDefines.parameterDefines});
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
        showJobResult: function(job_running_id){
            $.when(LaaS.request('jobRunning:entity',job_running_id))
                .done(function (jobRunningResult) {
                    var jobResultView = new JobResultView(jobRunningResult);
                    LaaS.mainRegion.show(jobResultView);
                });
            LaaS.navigate('/jobResults/'+job_running_id);
        }
    });


    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'jobnew(/)': 'jobnew',
                'jobs(/)': 'showJobs',
                'jobs/:id(/)': 'showJob',
                'jobResults/:id(/)': 'showJobResult'
            },
            controller: new JobController()
        });
    });
});
