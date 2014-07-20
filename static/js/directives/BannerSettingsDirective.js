define([
    'Angular',
    'Bootbox',
    'Underscore',
    'Messages',
    'text!view/edit/banner-settings-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, Bootbox, _, messages, bannerSettingsTemplate, contentView) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                ownerType:'=',
                owner:'=',
                object: '=',
                error:'='
            },
            restrict: 'E',
            template: bannerSettingsTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;
                var oldOwnerType=scope.object.ownerType;
                var oldOwnerId=scope.object.ownerId;


                if (angular.isDefined(obj)) {

                    scope.setOwn=function(){
                        if(obj.own){
                            obj.ownerType=scope.ownerType;
                            obj.ownerId=scope.owner.id;
                        }else{
                            obj.ownerType=oldOwnerType;
                            obj.ownerId=oldOwnerId;
                        }
                    }

                    scope.setImage = function () {
                        var $contentEditScope = scope.$new();
                        $contentEditScope.content = {
                            content: {
                                type:'IMAGE',
                                fileUrl:obj.imageUrl
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
                                        var c = $contentEditScope.content;
                                        obj.imageUrl= c.content.fileUrl;
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