define([
    'Angular',
    'Bootbox',
    'Underscore',
    'Messages',
    'text!view/edit/select-image-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, Bootbox, _, messages, selectImageTemplate, contentView) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                title:'@',
                url: '=',
                area:'@',
                pwidth: '@',
                pheight:'@'
            },
            restrict: 'E',
            template: selectImageTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;

                scope.setImage = function () {
                    var $contentEditScope = scope.$new();
                    $contentEditScope.content = {
                        content: {
                            type:'IMAGE',
                            size:'FULLSIZE',
                            fileUrl:scope.url,
                            infoText:''
                        },
                        contentExt: {
                            fileUrl:null
                        },
                        area:scope.area
                    };
                    $contentEditScope.imageType = attrs.imageType;
                    $contentEditScope.imageOnly = true;
                    $contentEditScope.audioEnabled = false;
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
                                    if(c.contentExt.fileUrl){
                                        scope.url= c.contentExt.fileUrl;
                                    }else{
                                        scope.url= c.content.fileUrl;
                                    }
                                    scope.$apply()
                                }
                            }
                        }
                    });


                }
            }
        };
    }];
});