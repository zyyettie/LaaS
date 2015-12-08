LaaS.module('ParameterDef', function (ParameterDef, LaaS, Backbone, Marionette) {
    'use strict';


    var InputParameterDefView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.inputParameterDef = options.attributes;
        },
        template: function (data) {
            var template = JST['app/handlebars/inputParameterDef/detail'];
            var html = template(data.inputParameterDef);
            return html;
        },
        serializeData: function () {
            return {inputParameterDef: this.inputParameterDef};
        },
        onRender: function() {
            this.$('select').dropdown();
        }
    });

    var InputParameterDefListView = Marionette.ItemView.extend({
        initialize: function (options) {
            this.inputParameterDefs = options.inputParameterDefs;
        },
        template: function(data) {
            var template = JST['app/handlebars/inputParameterDef/list'];
            var html = template(data);
            return html;
        },
        serializeData: function() {
            return {inputParameterDefs: this.inputParameterDefs};
        },
        events:{
            'click button.inputParameterDef-show': "showClicked"
        },
        showClicked: function(event){
            var inputParameterDefId = event.target.dataset["id"];
            LaaS.navigate('/inputParameterDefs/'+inputParameterDefId,true);
        }
    });

    var InputParameterDefController = Marionette.Controller.extend({
        showInputParameterDef: function (id) {
            $.when(LaaS.request('inputParameterDef:entity', {'id': id})).done(function (data) {
                var view = new InputParameterDefView(data);
                LaaS.mainRegion.show(view);
            });
        },
        showInputParameterDefs: function () {
            $.when(LaaS.request('inputParameterDef:entities')).done(function (data) {
                var view = new InputParameterDefListView(data);
                LaaS.mainRegion.show(view);
            });
        }
    });

    LaaS.addInitializer(function () {
        new Marionette.AppRouter({
            appRoutes: {
                'inputParameterDefs(/)': 'showInputParameterDefs',
                'inputParameterDefs/:id(/)': 'showInputParameterDef'
            },
            controller: new InputParameterDefController()
        });
    });
});
