define([
    'Angular',
    'Underscore',
    'Messages',
    'Config',
    'text!view/validation/errors.html'], function (angular, _, messages, Config, errorsTemplate) {
    return function ($rootScope) {
        return {
            scope: {
                errors: '='
            },
            restrict: 'E',
            template: errorsTemplate,
            link: function (scope, element, attrs) {
                scope.serverErrors = [];
                scope.$watch('errors', function(errors) {
                    if (errors) {
                        scope.serverErrors = _.map(_.pairs(errors), function(val) {
                            return {
                                field: val[0],
                                text: val[1]
                            }
                        });
                    }
                })
            }
        };
    };
});