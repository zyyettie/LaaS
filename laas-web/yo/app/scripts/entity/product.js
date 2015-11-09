LaaS.module('Entities', function(Entities, LaaS, Backbone, Marionette) {
    'use strict';

    var baseUrl = LaaS.Util.Constants.APPCONTEXT+'/api/v1/products';

    Entities.ProductModel = Backbone.Model.extend({
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

    LaaS.reqres.setHandler('product:entity', function(options) {
        var product = new LaaS.Entities.ProductModel(options);
        var defer = $.Deferred();
        product.fetch().then(function () {
            defer.resolve(product);
        });
        return defer.promise();
    });

    LaaS.reqres.setHandler('product:entities', function(options) {
        var options = options || {page:0,size:10};
        var page = options.page || 0;
        var size = options.size || 10;
        var products = $.Deferred();
        $.getJSON(baseUrl+"?&page=" + page + "&size=" + size).done(function(data){
            products.resolve({products:data._embedded ? data._embedded.products : [], page:data.page});
        });
        return products.promise();
    });

    LaaS.reqres.setHandler('product:entitiesByUrl', function(options) {
        var options = options || {url:baseUrl};
        var url = options.url || baseUrl;
        var products = $.Deferred();
        $.getJSON(url).done(function(data) {
            var list = data._embedded ? data._embedded.products : data;
            products.resolve({products:list});
        });
        return products.promise();
    });
})
