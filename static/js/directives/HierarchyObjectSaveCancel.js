define([
    'Angular',
    'Messages',
    'Underscore',
    'Config',
    'Bootbox',
    'text!view/edit/save-cancel-template.html'
], function (angular, messages, _, config, Bootbox, saveCancelTemplate) {
    return function ($location) {
        return {
            scope: {
                onSave: '&',
                hasError:'&'
            },
            restrict: 'E',
            template: saveCancelTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                scope.save = function() {
                    Bootbox.dialog({
                        message: messages['schema.im.edit.dialog.save.confirm.msg'],
                        title: messages['schema.im.edit.dialog.save.confirm.title'],
                        buttons: {
                            cancel: {
                                label: messages['default.button.cancel']
                            },
                            save: {
                                label: messages['default.button.save'],
                                callback: function() {
                                    scope.onSave()
                                }
                            }
                        }
                    });
                };
                scope.cancel = function () {
                    $location.path('/schema');
                };
                scope.checkError=function(){
                    return scope.hasError();
                }

            }
        };
    };
});