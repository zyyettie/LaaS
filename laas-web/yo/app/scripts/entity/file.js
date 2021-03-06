LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+LaaS.Util.Constants.APIVERSION+'/files';

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
        var file = new LaaS.Entities.FileModel(options);
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
            files.resolve({files:data._embedded ? data._embedded.files : [], page:data.page});
        });
        return files.promise();
    });

    LaaS.reqres.setHandler('file:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var files = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.files : [];
            files.resolve({files:list});
        });
        return files.promise();
    });

    LaaS.reqres.setHandler('myFiles:entities', function (options) {
        var options = options || {page:0,size:10,projection:'filesummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'filesummary';
        var files = $.Deferred();
        var userName = sessionStorage.getItem("username");

        $.getJSON(baseUrl+"/search/findFilesOwnedBy?userName=" + userName+"&page=" + page + "&size=" + size + "&projection=" + projection).done(function(data){
            var list = data._embedded ? data._embedded.files : [];
            files.resolve({files:list,page:data.page});
        });
        return files.promise();
    });

    LaaS.reqres.setHandler('file:myEntitiesByTypes', function (options) {
        var options = options || {page:0,size:10,projection:'filesummary'};
        var page = options.page || 0;
        var size = options.size || 10;
        var projection = options.projection || 'filesummary';
        var fileTypes = options.fileTypes || [];
        var types = "";
        for (var i=0; i<fileTypes.length; i++) {
            if (i > 0) {
                types += ", ";
            }
            types += fileTypes[i].type;
        }
        var files = $.Deferred();
        var userName = sessionStorage.getItem("username");

        var url = baseUrl+"/search/findFilesOwnedAndTypes?userName=" + userName+"&fileTypes="+types+"&page=" + page + "&size=" + size + "&projection=" + projection;
        $.getJSON(url).done(function(data){
            var list = data._embedded ? data._embedded.files : [];
            files.resolve({files:list,page:data.page});
        });
        return files.promise();
    });

    LaaS.reqres.setHandler('fileTypes:entities', function (options) {
        var options = options || {page:0,size:10};
        var page = options.page || 0;
        var size = options.size || 10;
        var files = $.Deferred();
        var userName = sessionStorage.getItem("username");

        $.getJSON(baseUrl+"/search/findFilesOwnedBy?userName=" + userName+"&page=" + page + "&size=" + size).done(function(data){
            var list = data._embedded ? data._embedded.files : [];
            files.resolve({files:list,page:data.page});
        });
        return files.promise();
    });
});
