LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';

    var JobView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.job = options.model.attributes;
            this.scenarioList = options.scenarioList;
            this.fileList = options.fileList;
            this.selected = options.selected;
        },
        serializeData:function(){
            return {job: this.job, scenarioList:this.scenarioList, fileList:this.fileList, selected:this.selected};
        },
        //onRender: function () {
        //    this.$('select').dropdown(//{
                /*action: 'select',
                onChange: function(value, text, $selectedItem) {
                    //this.showParameters(value);
                //}
            });
        },*/
        template: function (data) {
            var template;
            var html;
            if (data.job.id == undefined) {
                template = JST['app/handlebars/job/add'];
                html = template(data);
            } else {
                template = JST['app/handlebars/job/detail'];
                var json = JSON.parse(data.job.parameters);
                data.job.N = json["N"];
                data.job.scenarioList = data.scenarioList;
                data.job.selectedid = data.selected[0].id;
                data.job.selectedname = data.selected[0].name;
                html = template(data.job);
            }
            return html;
        },
        events: {
            'click #job_save': 'saveJob',
            'click #job_run': 'runJob'
            //'click .dropdown': 'switchscenario'
        },
        saveJob: function () {
            var that = this;
            var json = Backbone.Syphon.serialize(this);
            if(json.name == '' || json.scenario == ''){
                toastr.error('Please input name and select scenario.');
                return;
            }
            json.parameters = JSON.stringify({N:json['N']});
            json.scenarios = [];
            json.scenarios.push("/api/v1/scenarios/"+json.selectedScenario);

            this.model.save(json,{patch:true,success:function(){
                toastr.info('Save Job successfully.');
                LaaS.navigate('/jobs/' + that.model.id + '/edit');
                $('#title').text('Editing Job');

            },error:function(){
                toastr.error('Save Job failed.');
            }});
        },
        runJob: function () {
            console.log("run the job");
            //myJob.save();
        },
        switchscenario: function () {
            $('.dropdown')
                .dropdown({
                    action: 'activate'
                });
//            console.log("click switchscenario");
            var tt = $('select').dropdown('get text');
//            console.log(tt);
//            var topNInput = $( '<input type="text"/>' );
//            $('select').append(topNInput);
            var myNewElement = $( "<p>New element</p>" );
            $('.newArea').toggle(myNewElement);

        }
    });

    var JobListView = Marionette.ItemView.extend({
        initialize : function(options){
            this.jobs = options.jobs;
        },
        template : function(data){
            var template = JST['app/handlebars/job/list'];
            var html = template(data);
            return html;
        },
        serializeData:function(){
            return {jobs:this.jobs};
        }
    });

    var JobController = Marionette.Controller.extend({
        jobnew: function () {
            $.when(LaaS.request('job:new'), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function(job, scenario, file){
                LaaS.mainRegion.show(new JobView({model:job, scenarioList:scenario.scenarios, fileList:file.files}));
            });
        },
        showJob: function (id) {
            $.when(LaaS.request('job:entity', {'id':id}), LaaS.request('scenario:entities'), LaaS.request('file:entities'))
                .done(function(job, scenario, file){
                var scenarioModel = new LaaS.ScenarioModel();
                scenarioModel.url = job.attributes._links.scenarios.href;
                scenarioModel.fetch({
                    success: function(model, response) {
                        var view = new JobView({model:job, scenarioList:scenario.scenarios, fileList:file.files, selected:response._embedded.scenarios});
                        LaaS.mainRegion.show(view);
                    },
                    error: function(err) {
                        console.log(err);
                    }
                })
            });
        },
        showJobs: function() {
            $.when(LaaS.request('job:entities')).done(function(data){
                var view = new JobListView(data);
                LaaS.mainRegion.show(view);
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
