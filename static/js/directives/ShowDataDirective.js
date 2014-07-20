define(['Angular', 'Underscore'], function (angular, _) {
    return function ($compile) {
        return {
            scope: true,
            link: function (scope, element, attrs) {
                var el;
                var cb = attrs.callback;
                attrs.$observe('template', function (tpl) {
                    if (angular.isDefined(tpl)) {
                        el = $compile(tpl)(scope);
                        element.html("");
                        element.append(el);
                        if (_.isFunction(scope[cb])) {
                            scope[cb]();
                        }
                    }
                });
            }
        };
    };
});