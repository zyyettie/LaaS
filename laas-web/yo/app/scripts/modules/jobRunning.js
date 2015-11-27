LaaS.module('JobRunning', function(JobRunning, LaaS, Backbone, Marionette) {
    'use strict';

    var appContext = LaaS.Util.Constants.APPCONTEXT;
    var controllers = LaaS.Util.Constants.CONTROLLERS;

    var JobRunningView = Marionette.ItemView.extend({
        initialize : function(options){
            this.jobRunning = options.model.attributes;
            this.job = options.job;
            this.scenarios = options.scenarios;
            this.files = options.files;
            this.inputParameterDefs = options.inputParameterDefs;
        },
        template : function(data){
            var template = JST['app/handlebars/jobRunning/detail'];
            var html = template(data.jobRunning);

            var json = data.jobRunning.parameters ? JSON.parse(data.jobRunning.parameters) : {};
            var subHtml = LaaS.Form.generateParameterSubForm(data.jobRunning.inputParameterDefs, json, true);

            html = html.replace('<div id="inputParameters"></div>', '<div id="inputParameters">'+subHtml+'</div>');
            return html;
        },
        serializeData:function(){
            var data =  {jobRunning:this.jobRunning};
            data.jobRunning.job = this.job;
            data.jobRunning.scenario = this.scenarios && this.scenarios.length ? this.scenarios[0].name : "";
            data.jobRunning.files = this.files;
            data.jobRunning.inputParameterDefs = this.inputParameterDefs ? this.inputParameterDefs : [];
            return data;
        },
        events: {
            'click #jobRunning_ok': 'backJobRunning',
            'click #jobRunning_viewresult': 'viewResult',
            'click #jobRunning_rerun': 'rerunJobRunning'
        },
        backJobRunning: function() {
            window.history.back();
        },
        viewResult: function() {
            var that = this;
            switch(this.jobRunning.status) {
                case "SUCCESS":
                    $.when(LaaS.request('jobResult:entity',{id:this.jobRunning.id}))
                        .done(function (jobRunningResult) {
                            var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult, sync:true});
                            LaaS.mainRegion.show(jobResultView);
                            LaaS.navigate('/jobResults/'+that.jobRunning.id);
                        });
                    break;

                case "RUNNING":
                    var jobResultView = new LaaS.JobResult.JobResultView({sync:false});
                    LaaS.mainRegion.show(jobResultView);
                    LaaS.navigate('/jobResults/'+that.jobRunning.id);
                    break;
                case "FAILED":
                    $.when(LaaS.request('taskRunning:entitiesByUrl', {url:this.jobRunning._links.taskRunnings.href}))
                        .done(function(taskRunnings) {
                        if (taskRunnings != undefined) {
                            var rootcauses = [];
                            for (var i=0; i<taskRunnings.length; i++) {
                                if (taskRunnings[i].rootcauses) {
                                    rootcauses.push(taskRunnings[i].rootcauses);
                                }
                            }

                            var jobResultView = new LaaS.JobResult.JobResultView({success:false, rootcauses:rootcauses});
                            LaaS.mainRegion.show(jobResultView);
                            LaaS.navigate('/jobResults/'+that.jobRunning.id);
                        }
                    });
                    break;
                default:
                    break;
            }
        },
        rerunJobRunning: function() {
            $.getJSON(appContext+controllers+"/jobRunnings/" + this.jobRunning.id).done(function (json) {
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
                LaaS.navigate('/jobResults/'+json.job_running_id);
            });
        }
    });

    var JobRunningListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.jobRunnings = options.jobRunnings;
        },
        template : function(data){
            var template = JST['app/handlebars/jobRunning/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {jobRunnings:this.jobRunnings};
        },
        events:{
            'click button.jobRunning-show': "showClicked",
            'click button.jobRunning-viewResult': 'viewResult',
            'click button.jobRunning-rerun': "rerunClicked"
        },
        showClicked: function(event){
            var jobRunningId = event.target.dataset["id"];
            LaaS.navigate('/jobHistory/'+jobRunningId, true);
        },
        viewResult: function(event) {
            var jobRunningId = event.target.dataset["id"];
            var jobRunning;
            for (var i=0; i<this.jobRunnings.length; i++) {
                if (jobRunningId == this.jobRunnings[i].id) {
                    jobRunning = this.jobRunnings[i];
                    break;
                }
            }
            switch(jobRunning.status) {
                case "SUCCESS":
                    $.when(LaaS.request('jobResult:entity',{id:jobRunningId}))
                        .done(function (jobRunningResult) {
                            var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult, sync:true});
                            LaaS.mainRegion.show(jobResultView);
                            LaaS.navigate('/jobResults/'+jobRunningId);
                    });
                    break;
                case "RUNNING":
                    var jobResultView = new LaaS.JobResult.JobResultView({sync:false});
                    LaaS.mainRegion.show(jobResultView);
                    LaaS.navigate('/jobResults/'+jobRunningId);
                    break;
                case "FAILED":
                    $.when(LaaS.request('jobRunning:entity', {id:jobRunningId})).done(function(jobRunning) {
                        $.when(LaaS.request('taskRunning:entitiesByUrl', {url:jobRunning.attributes._links.taskRunnings.href}))
                            .done(function(taskRunnings) {
                                if (taskRunnings != undefined) {
                                    var rootcauses = [];
                                    for (var i=0; i<taskRunnings.length; i++) {
                                        if (taskRunnings[i].rootcauses) {
                                            rootcauses.push(taskRunnings[i].rootcauses);
                                        }
                                    }

                                    var jobResultView = new LaaS.JobResult.JobResultView({success:false, rootcauses:rootcauses});
                                    LaaS.mainRegion.show(jobResultView);
                                    LaaS.navigate('/jobResults/'+jobRunning.id);
                                }
                        });
                    });
                    break;
                default:
                    break;
            }
        },
        rerunClicked: function(event) {
            var jobRunningId = event.target.dataset["id"];
            $.getJSON(appContext+controllers+"/jobRunnings/" + jobRunningId).done(function (json) {
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
                LaaS.navigate('/jobResults/'+json.job_running_id);
            });
        }
    });

    var JobRunningController = Marionette.Controller.extend({
        showJobRunnings: function() {
            $.when(LaaS.request('jobRunning:entities')).done(function(data){
                var view = new JobRunningListView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showJobRunning: function(id){
            $.when(LaaS.request('jobRunning:entity', {'id':id})).done(function(jobRunningModel){
                $.when(LaaS.request('job:entityByUrl', {'url': jobRunningModel.attributes._links.job.href})).done(function(job) {
                    $.when(LaaS.request('scenario:entitiesByUrl', {'url':job._links.scenarios.href}),
                        LaaS.request('file:entitiesByUrl', {'url':job._links.files.href})).done(function(selectedScenarios, selectedFiles) {
                            $.when(LaaS.request("inputParameterDef:entitiesByUrl", {"url":selectedScenarios.scenarios[0]._links.inputParameterDefs.href})).done(function(inputParameterDefs) {
                                var view = new JobRunningView({model:jobRunningModel, scenarios:selectedScenarios.scenarios,
                                    files:selectedFiles.files, inputParameterDefs:inputParameterDefs.inputParameterDefs});
                                LaaS.mainRegion.show(view);
                            });
                    });

                })
            });
        }
    });


    LaaS.addInitializer(function() {
        new Marionette.AppRouter({
            appRoutes : {
                'jobHistory(/)': 'showJobRunnings',
                'jobHistory/:id(/)' : 'showJobRunning'
            },
            controller: new JobRunningController()
        });
    });
});