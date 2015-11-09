LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+'/api/v1/fileTypes';

    Entities.FileTypeModel = Backbone.Model.extend({
        url:function() {
            var url = this.id ? baseUrl+"/" + this.id : baseUrl;
            return url;
        },
        isNew:function() {
            return this.id == null || this.id == undefined;
        },
        initialize:function(options) {
            if (options && options.id) {
                this.id = options.id;
            }
        }
    });

    LaaS.reqres.setHandler('fileType:entity', function(options) {
        var fileType = new LaaS.Entities.FileTypeModel(options);
        var defer = $.Deferred();
        fileType.fetch().then(function () {
            defer.resolve(fileType);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('fileType:entities', function(options) {
        var options = options || {page:0,size:10};
        var page = options.page || 0;
        var size = options.size || 10;
        var fileTypes = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size).done(function(data){
            fileTypes.resolve({fileTypes:data._embedded.fileTypes,page:data.page});
        });
        return fileTypes.promise();
    });

    LaaS.reqres.setHandler('fileType:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var fileTypes = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.fileTypes : [];
            fileTypes.resolve({fileTypes:list});
        });
        return fileTypes.promise();
    });
})
