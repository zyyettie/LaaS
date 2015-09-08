LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';

    var JobView = Marionette.ItemView.extend({
        onRender: function () {
            this.$('select').dropdown();
        },
        template: JST['app/handlebars/job/add'],
        events: {
            'click #job_save': 'saveJob',
            'click #job_run': 'runJob'
            //'click .dropdown': 'switchscenario'
        },
        saveJob: function () {
            var json = Backbone.Syphon.serialize(this);
            if(json.name == '' || json.scenario == ''){
                toastr.error('Please input name and select scenario');
                return;
            }
            this.model.save(json);
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
            $.when(LaaS.request('job:new')).done(function(job){
                LaaS.mainRegion.show(new JobView({model:job}));
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