define([
    'Angular',
    'Underscore',
    'Messages',
    'text!view/edit/sos-number-template.html'], function (angular, _, messages, sosNumberTemplate) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                object: '='
            },
            restrict: 'E',
            template: sosNumberTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;
                if (angular.isDefined(obj)) {
                }
            }
        };
    }];
});