LaaS.module('Views', function (Views, LaaS, Backbone, Marionette) {
    'use strict';
    var appContext = LaaS.Util.Constants.APPCONTEXT;

    Views.FileUploader = Marionette.ItemView.extend({
        template: JST['app/handlebars/fileupload/upload'],
        initialize : function(options){
            this.url = options.url;
            this.fileTypes = options.fileTypes;
            this.uploadedFiles = [];
            var uploadedFiles = sessionStorage.getItem('uploadedFiles');
            if (uploadedFiles) {
                uploadedFiles = JSON.parse(uploadedFiles);
                $.extend(this.uploadedFiles, uploadedFiles);
            }
        },
        onRender: function () {
            this.total = 0;
            var that = this;
            this.$('select').dropdown();
            this.$('#progress').hide();
            this.$('#attaching').on('click', function () {
                $('form').append('<input type="file" multiple="multiple" name="files[]" style="display:none;">');
                $('input:last').on('change', function () {
                    console.log('selected files');
                    console.log(this.files);
                    var itemTempalte = JST['app/handlebars/fileupload/fileitem'];
                    var fileNumber = this.files.length;
                    that.total += fileNumber;
                    for (var i = 0; i < fileNumber; i++) {
                        var dislay = {name: this.files[i].name, size: LaaS.Util.getFileDisplaySize(this.files[i].size)};
                        $('.ui.list').append(itemTempalte(dislay));
                    }

                });
                $('input:last').click();
            });
            var uploadingCallback = function (e) {
                if (e.lengthComputable) {
                    var max = e.total;
                    var current = e.loaded;
                    var Percentage = Math.round((current * 100) / max); 
                    $('#progress').progress({percent:Percentage});
                    $('#progress .label').text(''+ Percentage + ' % uploaded');              
                }
            };
            this.$('#upload').click(function () {
                $('#progress').removeClass('error success');    
                $('#progress').addClass('active');
                $('#progress').progress({percent:0});
                $('#progress .label').text('uploading');
                $('#progress').show();
                $(this).toggleClass("disabled");
                var formData = new FormData($('form')[0]);
                $.ajax({
                    type: "POST",
                    url: appContext + "/api/v1/upload",
                    data: formData,
                    processData: false,
                    contentType: false,
                    xhr: function () {
                        var uploadingxhr = $.ajaxSettings.xhr();
                        if (uploadingxhr.upload) {
                            uploadingxhr.upload.addEventListener('progress', uploadingCallback, false);
                        }
                        return uploadingxhr;
                    }                 
                }).always(function(data,textStatus,error){
                    $('#progress').removeClass('active');
                    if(textStatus != 'success'){
                            $('#progress').addClass('error');
                            $('#progress').progress({percent:100});
                            $('#progress .label').text('upload failed');
        
                        }else{
                            $('#progress').progress({percent:100,text:{success:'total ' + that.total + ' uploaded' }});
                            $.extend(that.uploadedFiles, data);
                            sessionStorage.setItem('uploadedFiles', JSON.stringify(that.uploadedFiles));
                        }
                        $('#upload').removeClass('disabled');

                        LaaS.navigate(that.url, true);
                });

            });

        },
        serializeData:function(){
            return {fileTypes:this.fileTypes};
        },
        events: {
            'click #upload_cancel':'cancelUpload',
            'click #delete_my_files':'deleteMyFiles'
        },
        cancelUpload:function(){
            LaaS.navigate(this.url, true);
        }

    });
});
