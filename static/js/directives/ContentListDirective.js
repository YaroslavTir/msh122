define([
    'Angular',
    'Underscore',
    'Messages',
    'Bootbox',
    'text!view/edit/content-list-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, _, messages, Bootbox, contentListTemplate, contentView) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                object: '=',
                title:'@title',
                types:'='
            },
            restrict: 'E',
            template: contentListTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var contentList = scope.object;
                if(!contentList){
                    contentList=[]
                }
                var allowedTypeNames=scope.types;

                scope.$watch('object', function(newValue){
                    contentList=newValue;
                });

                var allContentTypes = [
                    {
                        value: 'IMAGE',
                        text: messages['media.content.type.IMAGE'],
                        getUrl: function(c, cext) {
                                if(cext.fileUrl){
                                    return cext.fileUrl;
                                }else{
                                    return c.fileUrl;
                                }
                        }
                    },
                    {
                        value: 'VIDEO',
                        text: messages['media.content.type.VIDEO'],
                        getUrl: function(c, cext) {
                            return c.fileUrl;
                        }
                    }
                ];

                scope.contentTypes= _.filter(allContentTypes, function(type){return _.contains(allowedTypeNames, type.value)})

                if (angular.isDefined(contentList)) {
                    scope.add = function () {
                        scope.inserted = {
                            content:{
                                type:(scope.contentTypes.length==1)?scope.contentTypes[0].value:null,
                                infoText:null,
                                fileUrl:null,
                                audioUrl:null,
                                bgColor:'#FFFFFF'
                            },
                            contentExt:{

                            }
                        };
                        contentList.push(scope.inserted);
                    };

                    scope.cancel=function(rowform, v, index){
                        rowform.$cancel();
                        if(v==scope.inserted && !v.content.content){
                            scope.remove(index);
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
                        contentList.splice(index, 1);
                    };
                    scope.printUrl = function (s) {
                        var ct = _.find(scope.contentTypes, function(v) {
                            return v.value === s.content.type;
                        });
                        if (!ct) {
                            return null;
                        }
                        var res = ct.getUrl(s.content, s.contentExt);
                        if (!res) {
                            return '';
                        }
                        return res;
                    };

                    scope.setContent = function (s) {
                        var $contentEditScope = scope.$new();
                        $contentEditScope.content = {
                            content: s.content,
                            contentExt: s.contentExt
                        };
                        $contentEditScope.imageType = attrs.imageType;
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
                                        var c = $contentEditScope.content.contentExt;
                                        s.contentExt = {
                                            id: c.id,
                                            type: c.type,
                                            generated: c.generated,
                                            fileUrl: c.fileUrl,
                                            audioUrl: c.audioUrl,
                                            updateDate: c.updateDate
                                        };
                                        scope.$apply()
                                    }
                                }
                            }
                        });
                    }
                }
            }
        };
    }];
});