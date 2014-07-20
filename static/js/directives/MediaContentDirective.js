define([
    'Angular',
    'Underscore',
    'Messages',
    'Bootbox',
    'jQuery',
    'text!view/media-content/media-content-template.html'], function (angular, _, Messages, Bootbox, $, contentTemplate) {
    return ['$compile', 'MediaService', 'UploadService', 'MediaContentService', function ($compile, mediaService, uploadService, mediaContentService) {
        return {
            scope: {
                data: '=',
                imageType: '=',
                audioEnabled:'=',
                imageOnly:'='
            },
            restrict: 'E',
            link: function (scope, element, attrs) {
                scope.videoType = 'VIDEO';
                scope.audioType = 'AUDIO';
                scope.generateInProgress = false;
                scope.messages = Messages;
                scope.selectedImageUrl = scope.data.content.fileUrl;
                scope.selectedVideoUrl = scope.data.content.fileUrl;
                scope.selectedAudioUrl = scope.data.content.audioUrl;
                scope.area=scope.data.area;
                scope.selectImage = function(image) {
                    scope.data.content.fileUrl = image.url
                };
                scope.cleanImage = function() {
                    scope.data.content.fileUrl = null
                };
                scope.selectVideo = function(video) {
                    scope.data.content.fileUrl = video.url
                };
                scope.cleanVideo = function() {
                    scope.data.content.fileUrl = null
                };
                scope.selectAudio = function(audio) {
                    scope.data.content.audioUrl = audio.url
                };
                scope.cleanAudio = function() {
                    scope.data.content.audioUrl = null
                };

                scope.generateImageWithTextPreview = function() {
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

                scope.switchDataType = function(type) {
                    scope.enableText = false;
                    scope.enableImage = false;
                    scope.enableColor = false;
                    scope.enableVideo = false;
                    scope.enableAudio = false;
                    switch (type) {
                        case 'IMAGE': {
                            scope.enableImage = true;
                            scope.enableAudio = scope.audioEnabled;
                            if(!scope.imageOnly){
                                scope.enableText = true;
                                scope.enableColor = true;
                            }
                            break;
                        }
                        case 'VIDEO': {
                            scope.enableVideo = true;
                            break;
                        }
                        case 'AUDIO': {
                            scope.enableAudio = true;
                            break;
                        }
                    }
                };

                element.append($compile(contentTemplate)(scope));

                scope.switchDataType(scope.data.content.type);
            }
        };
    }];
});