define([
    'Angular',
    'Bootbox',
    'Underscore',
    'Messages',
    'Config',
    'Console',
    'text!view/build-info.html'
], function (angular, Bootbox, _, messages, Config, Console, buildInfoTemplate) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                buildInfoObject: '='
            },
            template: buildInfoTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                scope.showInfo = Config['testMode']
            }
        };
    }];
});