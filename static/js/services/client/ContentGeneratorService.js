define([
    'Console',
    'Config',
    'Underscore',
    'jQuery',
    'Messages'],
    function (Console, Config, _, $, Messages) {
        return function ($rootScope, $resource) {

            this.generateImageWithTextPreview = function() {
                var content = scope.data.content;
                scope.generateInProgress = true;
                if(scope.data.contentExt.id){
                    mediaContentService.delete({
                        id:scope.data.contentExt.id
                    });
                }
                mediaContentService.save({}, {
                    content: {
                        type: content.type,
                        size:content.size,
                        infoText: content.infoText,
                        fileUrl: content.fileUrl,
                        bgColor: content.bgColor
                    },
                    area: scope.area
                }, function (generated) {
                    scope.data.contentExt = generated;
                    scope.generateInProgress = false;
                });
            };
        }
    });