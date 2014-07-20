define([
    'Angular',
    'Underscore',
    'Messages',
    'Bootbox',
    'text!view/edit/immediate-message-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, _, messages, Bootbox, immTemplate, contentView) {
    return ['$compile','MessageService','MessageListService', 'MediaContentService', function ($compile, messageService, messageListService, mediaContentService) {
        return {
            scope: {
                object:'=',
                id:'=',
                type:'=',
                area:'@'
            },
            restrict: 'E',
            template: immTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;

                scope.messageList=[];
                scope.playing={
                    hierarchyInfo:scope.object.hierarchyInfo
                };
                scope.playing.hierarchyInfo[scope.type]=scope.object;

                scope.reload=function() {
                    messageListService.get({},{
                        ownerType: scope.type,
                        ownerId: scope.id
                    }, function(res){
                        scope.playing=res;
                        scope.playing.hierarchyInfo=scope.object.hierarchyInfo;
                        scope.playing.hierarchyInfo[scope.type]=scope.object;
                    });

                    messageListService.query({}, {
                        ownerType: scope.type,
                        ownerId: scope.id
                    }, function(list){
                        scope.messageList=list;
                    });
                }

                scope.contentTypes = [
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


                if (angular.isDefined(scope.messageList)) {
                    scope.add = function () {
                        scope.inserted = {
                            updateDate:null,
                            startDate:null,
                            stopDate:null,
                            status:'STOPPED',
                            isNew: true,
                            content:{
                                type:null,
                                size:'FULLSIZE',
                                infoText:'',
                                fileUrl:null,
                                audioUrl:null,
                                bgColor:'#FFFFFF'
                            },
                            contentExt:{

                            }
                        };
                        scope.messageList.push(scope.inserted);
                        //scope.saveMessage(scope.inserted);
                    };
                    scope.addPicture = function () {
                        scope.inserted = {
                            updateDate:null,
                            startDate:null,
                            stopDate:null,
                            status:'STOPPED',
                            isNew: true,
                            content:{
                                type:'IMAGE',
                                size:'FULLSIZE',
                                infoText:'',
                                fileUrl:null,
                                audioUrl:null,
                                bgColor:'#FFFFFF'
                            },
                            contentExt:{

                            }
                        };
                        scope.messageList.push(scope.inserted);
                        scope.setContent(scope.inserted);
                        //scope.saveMessage(scope.inserted);
                    };
                    scope.addVideo = function () {
                        scope.inserted = {
                            updateDate:null,
                            startDate:null,
                            stopDate:null,
                            status:'STOPPED',
                            isNew: true,
                            content:{
                                type:'VIDEO',
                                size:'FULLSIZE',
                                infoText:'',
                                fileUrl:null,
                                audioUrl:null,
                                bgColor:'#FFFFFF'
                            },
                            contentExt:{

                            }
                        };
                        scope.messageList.push(scope.inserted);
                        scope.setContent(scope.inserted);
                        //scope.saveMessage(scope.inserted);
                    };

                    scope.cancel=function(rowform, v, index){
                        rowform.$cancel();
                        if(v==scope.inserted && !v.content.type){
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
                        var mId=scope.messageList[index].id;
                        if(mId){
                            messageService.delete({},{messageId:mId}, function(){
                                scope.reload();
                            });
                        }else{
                            scope.messageList.splice(index, 1);
                        }
                    };
                    scope.printUrl = function (s) {
                        if(!s || !s.content){
                            return '';
                        }
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

                    scope.editContent = function (s) {
                        s.isNew = false;
                        scope.setContent(s);
                    }

                    scope.setContent = function (s) {
                        var $contentEditScope = scope.$new();
                        $contentEditScope.content = {
                            content: s.content,
                            contentExt: s.contentExt,
                            area:scope.area
                        };
                        $contentEditScope.imageType = 'IMAGE_MESSAGE';
                        $contentEditScope.audioEnabled = true;
                        Bootbox.dialog({
                            message: $compile(contentView)($contentEditScope),
                            title: messages['media.content.edit.dialog.title'],
                            className: 'content-edit-modal',
                            onEscape: function() {
                                if (s.isNew) {
                                    scope.remove(scope.messageList.indexOf(s));
                                    scope.$apply();
                                }
                            },
                            buttons: {
                                cancel: {
                                    label: messages['default.button.cancel'],
                                    callback: function () {
                                        if (s.isNew) {
                                            scope.remove(scope.messageList.indexOf(s));
                                            scope.$apply();
                                        }
                                    }
                                },
                                save: {
                                    label: messages['default.button.save'],
                                    callback: function () {
                                        scope.generateImageWithTextPreview($contentEditScope);
                                        var c = $contentEditScope.content.contentExt;
                                        s.contentExt = {
                                            id: c.id,
                                            type: c.type,
                                            generated: c.generated,
                                            fileUrl: c.fileUrl,
                                            audioUrl: c.audioUrl,
                                            updateDate: c.updateDate
                                        };
                                        scope.$apply();
                                        scope.saveMessage(s);
                                    }
                                }
                            }
                        });
                    }

                    function cleanContent(c){
                        if(c.type=='VIDEO'){
                            c.audioUrl=null;
                            c.infoText=null;
                        }else if (c.type=='AUDIO'){
                            c.fileUrl=null;
                            c.infoText=null;
                        }

                    }

                    scope.generateImageWithTextPreview = function(scopeEdit) {
                        var content = scopeEdit.content.content;
                        scopeEdit.generateInProgress = true;
                        if(scopeEdit.content.contentExt.id){
                            mediaContentService.delete({
                                id: scopeEdit.content.contentExt.id
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
                            area: scopeEdit.area
                        }, function (generated) {
                            scopeEdit.content.contentExt = generated;
                            scopeEdit.generateInProgress = false;
                        });
                    };

                    scope.saveMessage=function(m){
                        m.ownerId=scope.id;
                        m.ownerType=scope.type;
                        cleanContent(m.content);
                        messageService.save({},m, function(saved){
                            scope.reload();
                        });
                    }

                    scope.startStopDisabled=function(v){
                        if(!v.id){
                            return true;
                        }
                        if(!scope.playing){
                            return false;
                        }
                        if(scope.playing.id && v.id!=scope.playing.id && v.ownerType==scope.playing.ownerType){
                            return true;
                        }
                        return false;
                    }

                    scope.cantEdit=function(v){
                        if(!v.id){
                            return false;
                        }
                        if(v.status=='STARTED'){
                            return true;
                        }
                        return false;
                    }

                    scope.startstop=function(m){
                        if(m.status=='STOPPED'){
                            messageService.play({},{messageId: m.id}, function(result){
                                scope.reload();
                            });
                        }else{
                            messageService.stop({},{messageId: m.id}, function(result){
                                scope.reload();
                            });
                        }
                    }
                }

                scope.reload();

            }
        };
    }];
});