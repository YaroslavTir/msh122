define([
    'Console',
    'jQuery',
    'Underscore',
    'Angular',
    'services/services',
    'directives/directives',
    'controllers/controllers'
], function (Console, $, _, angular, services, directives, controllers) {
    "use strict";
    return {
        initialize: function () {

            var mainModule = angular.module('metroApp', [
                'ngRoute',
                'ngResource',
                'ngCookies',
                'xeditable',
                'google-maps',
                'textAngular',
                'colorpicker.module',
                'ngGrid',
                'angucomplete'
            ]);

            services.initialize(mainModule);
            controllers.initialize(mainModule);
            directives.initialize(mainModule);
            angular.bootstrap(window.document, ['metroApp']);
        }
    };
});