define([
    'Angular',
    'Underscore',
    'Messages',
    'Bootbox',
    'text!view/edit/helpinfo-media-list-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, _, messages, Bootbox, infoHelpVideoListTemplate, contentView) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                object: '=',
                index:'=',
                mediaType:'@'
            },
            restrict: 'E',
            template: infoHelpVideoListTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;

                scope.contentTypes=[

                    {
                        value: 'VIDEO',
                        text: messages['media.content.type.VIDEO']
                    },
                    {
                        value: 'AUDIO',
                        text: messages['media.content.type.AUDIO']
                    }

                ]


                if (angular.isDefined(scope.index) && angular.isDefined(scope.object[scope.index])) {
                    if(scope.mediaType=="VIDEO"){
                        scope.contentList=scope.object[index].video;
                    }else{
                        scope.contentList=scope.object[index].audio;
                    }
                }else{
                    scope.contentList=[];
                }

                    scope.add = function () {
                        scope.inserted = {
                               type:scope.mediaType,
                               title:null,
                               link:null
                        };
                        scope.contentList.push(scope.inserted);
                    };

                    scope.cancel=function(rowform, v, index){
                        rowform.$cancel();
                        if(v==scope.inserted && !v.title && !v.link){
                            scope.remove(index);
                        }
                    }

                scope.moveUp=function(media){
                    var index = scope.contentList.indexOf(media);
                    if (index > 0) {
                        scope.contentList.splice(index, 1);
                        scope.contentList.splice(index-1, 0, media);
                    }
                }

                scope.moveDown=function(media){
                    var index = scope.contentList.indexOf(media);
                    if (index !=-1 && index< scope.contentList.length-1) {
                        scope.contentList.splice(index, 1);
                        scope.contentList.splice(index+1, 0, media);
                    }
                }

                    scope.removeWithConfirmation=function(index){
                        Bootbox.dialog({
                            message: messages['default.confirm.delete'],
                            title: messages['default.confirm.delete.title'],
                            buttons: {
                                delete: {
                                    label: messages['default.button.remove'],
                                        callback: function() {
                                            scope.remove(index);
                                            scope.$apply();
                                        },
                                    className:"btn-primary"
                                },
                                cancel: {
                                    label: messages['default.button.cancel'],
                                    className:"btn-default"
                                }
                            }
                        });
                    }

                    scope.remove = function (index) {
                        scope.contentList.splice(index, 1);
                    };

                    scope.$watch('index', function(newValue){
                        if(newValue>=0 && scope.object[newValue]){
                            if(scope.mediaType=="VIDEO"){
                                scope.contentList=scope.object[newValue].video;
                            }else{
                                scope.contentList=scope.object[newValue].audio;
                            }
                        }else{
                            scope.contentList=[];
                        }
                    });

                    scope.setContent = function (s) {
                        var $contentEditScope = scope.$new();
                        $contentEditScope.content = {
                            content: {
                                type: s.type,
                                fileUrl: s.link,
                                audioUrl: s.link
                            },
                            contentExt: {

                            }
                        };
                        Bootbox.dialog({
                            message: $compile(contentView)($contentEditScope),
                            title: messages['media.content.edit.dialog.title'],
                            className: 'content-edit-modal',
                            buttons: {
                                cancel: {
                                    label: messages['default.button.cancel']
                                },
                                save: {
                                    label: messages['default.button.save'],
                                    callback: function () {
                                        var c = $contentEditScope.content.content;
                                        if(s.type=='VIDEO'){
                                            s.link= c.fileUrl;
                                        }else{
                                            s.link= c.audioUrl;
                                        }
                                        scope.$apply()
                                    }
                                }
                            }
                        });
                    //}
                }
            }
        };
    }];
});