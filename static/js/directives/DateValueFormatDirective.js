define([
    'Angular',
    'Messages',
    'Underscore',
    'Config'
], function (angular, messages, _, config) {
    return function ($parse,$filter) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, element, attr, ctrl) {

                ctrl.$formatters.push(function(modelValue) {
                    if(modelValue){
                        var format='dd.MM.yyyy';
                        if(attr.dateFormat){
                            format=attr.dateFormat
                        }
                        return $filter('date')(modelValue,format);
                    }
                });

                /*ctrl.$parsers.push(function(viewValue) {
                    return parseFloat(viewValue.replace(new RegExp(",", "g"), ''));
                });*/
            }
        }
    };
});