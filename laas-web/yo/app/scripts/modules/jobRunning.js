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
            this.selectedParameterDefines = options.selectedParameterDefines;
        },
        template : function(data){
            var template = JST['app/handlebars/jobRunning/detail'];
            var html = template(data.jobRunning);

            var json = data.jobRunning.parameters ? JSON.parse(data.jobRunning.parameters) : {};
            var subHtml = LaaS.Form.generateParameterSubForm(data.jobRunning.selectedParameterDefines, json, true);

            html = html.replace('<div id="parameters"></div>', '<div id="parameters">'+subHtml+'</div>');
            return html;
        },
        serializeData:function(){
            var data =  {jobRunning:this.jobRunning};
            data.jobRunning.job = this.job;
            data.jobRunning.scenario = this.scenarios && this.scenarios.length ? this.scenarios[0].name : "";
            data.jobRunning.files = this.files;
            data.jobRunning.selectedParameterDefines = this.selectedParameterDefines ? this.selectedParameterDefines : [];
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
            if(this.jobRunning.status == "SUCCESS"){
                $.when(LaaS.request('jobResult:entity',{id:this.jobRunning.id}))
                    .done(function (jobRunningResult) {
                        var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult, sync:true});
                        LaaS.mainRegion.show(jobResultView);
                        LaaS.navigate('/jobResults/'+this.jobRunning.id);
                    });
            }else if (this.jobRunning.status == "RUNNING") {
                var jobResultView = new LaaS.JobResult.JobResultView({sync:false});
                LaaS.mainRegion.show(jobResultView);
                LaaS.navigate('/jobResults/'+this.jobRunning.id);
            }else {

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
            if(jobRunning.status == "SUCCESS"){
                $.when(LaaS.request('jobResult:entity',{id:jobRunningId}))
                    .done(function (jobRunningResult) {
                        var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult, sync:true});
                        LaaS.mainRegion.show(jobResultView);
                        LaaS.navigate('/jobResults/'+jobRunningId);
                    });
            }else if (jobRunning.status == "RUNNING") {
                var jobResultView = new LaaS.JobResult.JobResultView({sync:false});
                LaaS.mainRegion.show(jobResultView);
                LaaS.navigate('/jobResults/'+jobRunningId);
            }else {

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
                            $.when(LaaS.request("parameterDefine:entitiesByUrl", {"url":selectedScenarios.scenarios[0]._links.parameterDefines.href})).done(function(selectedParameterDefines) {
                                var view = new JobRunningView({model:jobRunningModel, scenarios:selectedScenarios.scenarios,
                                    files:selectedFiles.files, selectedParameterDefines:selectedParameterDefines.parameterDefines});
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