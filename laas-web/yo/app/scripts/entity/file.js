LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.URLPREFIX+'/api/v1/files';

    Entities.FileModel = Backbone.Model.extend({
        url: function(){
            var url = this.id ? baseUrl+"/" + this.id : baseUrl/* + "?projection=" + this.projection*/;
            return url;
        },
        isNew: function () {
            return this.id == null || this.id == undefined;
        },
        initialize: function (options) {
            if (options && options.id) {
                this.id = options.id;
            }
            /*
            if(options && options.projection){
                this.projection = options.projection;
            }else{
                this.projection = 'fileSummary';
            }    */
        }
    });

    LaaS.reqres.setHandler('file:entity', function (options) {
        var file = new FileModel(options);
        var defer = $.Deferred();
        file.fetch().then(function () {
            defer.resolve(file);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('file:entities', function (options) {
        var options = options || {page:0,size:10/*,projection:'fileSummary'*/};
        var page = options.page || 0;
        var size = options.size || 10;
        //var projection = options.projection || 'fileSummary'
        var files = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size/* + "&projection=" + projection*/).done(function(data){
            files.resolve({files:data._embedded.files,page:data.page});
        });
        return files.promise();
    });
});
