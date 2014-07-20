define([
    'Angular',
    'Underscore',
    'Messages',
    'jQuery',
    'text!view/edit/content-settings-template.html'], function (angular, _, messages, $, settingsTemplate) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                object: '='
            },
            restrict: 'E',
            template: settingsTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;

                scope.override=function(){
                    var $overrideFlag = $('#override_flag');
                    var on=$overrideFlag.is(':checked');
                    enableSettings(on);
                }

                var enableSettings=function(on){
                    var $cb = $('#show_cur');
                    $cb.prop("disabled", !on);
                    $cb=$('#show_weather');
                    $cb.prop("disabled", !on);
                    $cb=$('#show_time');
                    $cb.prop("disabled", !on);
                    $cb=$('#show_lang');
                    $cb.prop("disabled", !on);
                    $cb=$('#sos_number');
                    $cb.prop("disabled", !on);
                }

                if (angular.isDefined(obj)) {
                    enableSettings(obj.own);
                }
            }
        };
    }];
});