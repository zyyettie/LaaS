LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';

    var JobView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.scenarios = options.scenarios;
        },
        serializeData:function(){
            return {scenarios:this.scenarios};
        },
        onRender: function () {
            this.$('select').dropdown();
        },
        template: function (data) {
            var template = JST['app/handlebars/job/add'];
            var html = template(data);
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

    var JobController = Marionette.Controller.extend({
        jobnew: function () {
            $.when(LaaS.request('job:new'), LaaS.request('scenario:entities')).done(function(job, data){
                LaaS.mainRegion.show(new JobView({model:job, scenarios:data.scenarios}));
            });
        }
    });


    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'jobnew(/)': 'jobnew'
            },
            controller: new JobController()
        });
    });
});
