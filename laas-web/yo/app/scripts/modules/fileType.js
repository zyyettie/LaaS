LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+'/api/v1/fileTypes';

    LaaS.reqres.setHandler('fileTypes:entities', function (options) {
        var files = $.Deferred();

        $.getJSON(baseUrl).done(function(data){
            var list = data._embedded ? data._embedded.fileTypes : [];
            files.resolve({fileTypes:list});
        });
        return files.promise();
    });
});
