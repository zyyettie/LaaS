LaaS.module('JobResult', function(JobResult, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;
    JobResult.JobResultView = Marionette.ItemView.extend({
        initialize: function (options) {
            if(options.sync === true){
                this.sync = true;
                this.jobRunning = options.model.attributes;
            }
            if(options.success === false){
                this.success = false;
                this.rootcauses = options.rootcauses;
            }
        },
        template: function(){
            var template = JST['app/handlebars/job/result'];
            var html = template();
            //debug
            return html;
        },
        serializeData: function(){
            return {};
        },
        onRender: function(){
            if(this.success === false){
                var rootCausesStr = "<h2 class=\"ui header\">Oops! There's something wrong with your job, the error is like below:</div>";
                for(var i in this.rootcauses){
                    rootCausesStr += "<p>"+this.rootcauses[i]+"</p>";
                }
                this.$('#content-placeholder').html(rootCausesStr);
            }else{
                if(this.sync === true){
                    this.$('#content-placeholder').html(this.jobRunning.desc).text();
                    this.$('#tree').jstree();
                    this.$('#context').html("<object>this is a testing inner html</object>");
                }else{
                    this.$('#content-placeholder').html('<h2 class="ui header">Your job is running in the background, please check your inbox later</div>');
                }
            }
            this.$('a.downloadlink').click(function(event){
                event.preventDefault();
                event.stopPropagation();
                $('#downloadingIFrame').attr('src',appContext+$(this).attr('href'));
            });
        }
    });

    var JobResultController = Marionette.Controller.extend({
        showJobResult: function(job_running_id){
            $.when(LaaS.request('jobResult:entity',{id:job_running_id}))
                .done(function (jobRunningResult) {
                    var jobResultView = new LaaS.JobResult.JobResultView({model:jobRunningResult,sync:true});
                    LaaS.mainRegion.show(jobResultView);
                });
        }
    });

    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'jobResults/:id(/)': 'showJobResult',
                'jobRunnings/:id/result':'showJobResult'
            },
            controller: new JobResultController()
        });
    });
});
