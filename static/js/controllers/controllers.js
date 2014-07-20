define([
    'Config',
    'Console',
    'Underscore',
    'Angular',
    'routes/routes',
    'controllers/AppController',
    'controllers/LoginController',
    'controllers/SchemaController',
    'controllers/PassengerMessagesController',
    'controllers/ImStatisticController',
    'controllers/UsersController',
    'controllers/ImRegistrationController',
    'controllers/edit/ImEditController',
    'controllers/edit/LobbyEditController',
    'controllers/edit/StationEditController',
    'controllers/edit/LineEditController',
    'controllers/edit/SchemaEditController',
    'controllers/ContentController',
    'controllers/InternalErrorController',
    'controllers/edit/AbstractEditController'
], function (Config, Console, _, Angular, routes, AppController, LoginController, SchemaController, PassengerMessagesController, ImStatisticController, UsersController,
             ImRegistrationController, ImEditController, LobbyEditController, StationEditController, LineEditController, SchemaEditController, ContentController, InternalErrorController, AbstractEditController) {
    "use strict";
    var controllers = {
        app: AppController,
        login: LoginController,
        registration: ImRegistrationController,
        schema: SchemaController,
        passengerMessages: PassengerMessagesController,
        imStatistic: ImStatisticController,
        users: UsersController,
        AbstractEditController: AbstractEditController,
        imEdit: ImEditController,
        lobbyEdit: LobbyEditController,
        stationEdit: StationEditController,
        lineEdit: LineEditController,
        content: ContentController,
        internalError: InternalErrorController,
        schemaEdit: SchemaEditController
    };

    var setEditableOptions = function(module) {
        module.run(function(editableOptions) {
            editableOptions.theme = 'bs3';
        })
    };

    var setUp = function (module) {
        module.config(function ($routeProvider, $locationProvider, $httpProvider) {
            $locationProvider.html5Mode(true);
            $locationProvider.hashPrefix('!');
            _.each(routes, function (value, key) {
                $routeProvider.when(
                    value.route, {
                        template: value.template, controller: value.controller, title: value.title, isAllowed: value.isAllowed != undefined ? value.isAllowed : function () {
                            return true
                        }
                    }
                );
            });
            $routeProvider.otherwise({ redirectTo: "/login" });

            $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
                return {
                    responseError: function (rejection) {
                        var status = rejection.status;
                        var config = rejection.config;
                        var method = config.method;
                        var url = config.url;

                        if (status == 401) {
                            $location.path("/login");
                        } else if (status === Config['validation.error.code']) {
//                            $rootScope.serverError = {
//                                errors: rejection.data.errors
//                            };
                        } else if (status = 500) {
                            $location.path("/internal-error")
                        } else {
                            $rootScope.error = method + " on " + url + " failed with status " + status;
                        }

                        return $q.reject(rejection);
                    }
                }
            });
        });
    };

    var initialize = function (module) {
        _.each(controllers, function (controller, name) {
            module.controller(name, controller);
        });
        setUp(module);
        setEditableOptions(module);
        Console.info("Registered Controllers: ", controllers);
    };

    return {
        initialize: initialize
    };
});