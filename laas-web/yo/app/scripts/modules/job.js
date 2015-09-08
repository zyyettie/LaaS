LaaS.module('Job', function (Job, LaaS, Backbone, Marionette) {
    'use strict';

    var Job = Backbone.Model.extend({
        urlRoot: "/api/v1/jobs",
        defaults: {
            name: '',
            description: ''
        },
        setName: function (name) {
            this.set({name: name});
        }
    });

    var myJob = new Job();

    var JobView = Marionette.ItemView.extend({
        initialize: function () {
            this.render();
        },
        render: function () {
            $('.ui.dropdown').dropdown();
            this.$el.html(this.template(this.model.attributes));
            return this;
        },
        template: JST['app/handlebars/job/add'],
        model: myJob,

        events: {
            'click #job_save': 'saveJob',
            'click #job_run': 'runJob',
            'click .dropdown': 'switchscenario'
        },
        saveJob: function () {
            console.log('save job data!');
            myJob.save();
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
            LaaS.mainRegion.show(new JobView());
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