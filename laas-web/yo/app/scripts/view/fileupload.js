LaaS.module('Views', function (Views, LaaS, Backbone, Marionette) {
    'use strict';
    Views.FileUploader = Marionette.ItemView.extend({
        template: JST['app/handlebars/fileupload/upload'],
        onRender: function () {
            this.total = 0;
            var that = this;
            this.$('#progress').progress();
            this.$('#progress').hide();
            this.$('#attaching').on('click', function () {
                $('form').append('<input type="file" multiple="multiple" name="files[]" style="display:none;">');
                $('input').on('change', function () {
                    console.log('selected files');
                    console.log(this.files);
                    var itemTempalte = JST['app/handlebars/fileupload/fileitem'];
                    var fileNumber = this.files.length;
                    that.total += fileNumber;
                    for (var i = 0; i < fileNumber; i++) {
                        var dislay = {name: this.files[i].name, size: Math.round(this.files[i].size / 1000) + 'KB'};
                        $('.ui.list').append(itemTempalte(dislay));
                    }

                });
                $('input').last().click();
            });
            var uploadingCallback = function (e) {
                if (e.lengthComputable) {
                    var max = e.total;
                    var current = e.loaded;
                    var Percentage = Math.round((current * 100) / max);
                    $('#progress').progress({
                        percent: Percentage, text: {
                            active: Percentage + ' % Uploaded',
                            success: 'Total ' + that.total + ' log files uploaded'
                        }
                    });
                    if (Percentage >= 100) {
                        $('#upload').toggleClass("disabled");
                    }
                }
            };
            this.$('#upload').click(function () {
                $('#progress').show();
                $(this).toggleClass("disabled");
                var formData = new FormData($('form')[0]);
                $.ajax({
                    type: "POST",
                    url: "/api/v1/upload",
                    data: formData,
                    processData: false,
                    contentType: false,
                    xhr: function () {
                        var uploadingxhr = $.ajaxSettings.xhr();
                        if (uploadingxhr.upload) {
                            uploadingxhr.upload.addEventListener('progress', uploadingCallback, false);
                        }
                        return uploadingxhr;
                    },
                    success: function (data) {
                        console.log('succeed');
                    }
                });
            });

        },

    });
});
