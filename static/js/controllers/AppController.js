define(['Config', 'Console', 'Messages', 'Underscore', 'jQuery'], function (Config, Console, Messages, _, $) {
    return ['$scope', '$rootScope', '$location', '$cookieStore', 'LoginService', 'BuildInfoService', function($scope, $rootScope, $location,$cookieStore, loginService, buildInfoService) {
        $rootScope.messages = Messages;
        $rootScope.serverError = {};
        if (Config['testMode']) {
            $rootScope.buildInfo = {};
            buildInfoService.get(function(buildInfo) {
                $rootScope.buildInfo = buildInfo;
            });
        }

        var beforeLoginController = undefined;
        var beforeLoginPath = undefined;
        $scope.$on('$routeChangeStart', function(e, curr, prev) {
            if (!$rootScope.authorized) {
                loginService.info(function(result) {
                    if (!result.username){
                        if (curr.originalPath !== '/login' && curr.isAllowed && !curr.isAllowed($rootScope)) {
                            beforeLoginController = curr;
                            beforeLoginPath = $location.path() === '/login' ? '/schema' : $location.path();
                            $location.path('/login');
                        }
                    } else {
                        $rootScope.user = {
                            username: result.username,
                            roles: result.roles
                        };
                        $rootScope.authorized = true;
                        if (curr.isAllowed && !curr.isAllowed($rootScope)) {
                            beforeLoginController = curr;
                            beforeLoginPath = $location.path() === '/login' ? '/schema' : $location.path();
                            $location.path('/login');
                        }
                    }
                });
            } else if (curr.isAllowed && !curr.isAllowed($rootScope)) {
                beforeLoginController = curr;
                beforeLoginPath = $location.path() === '/login' ? '/schema' : $location.path();
                $location.path('/login');
            }
         });

        $scope.$watch('authorized', function() {
            if ($rootScope.authorized) {
                if (beforeLoginController && (!beforeLoginController.isAllowed || beforeLoginController.isAllowed($rootScope))) {
                    var url = beforeLoginPath;
                    beforeLoginController = undefined;
                    beforeLoginPath = undefined;
                    $location.path(url)
                }
            }
            if ($rootScope.authorized && $location.path() === '/login' && !beforeLoginController) {
                $location.path('/')
            }
         });

        $rootScope.$on('$viewContentLoaded', function() {
            $rootScope.error = undefined;
        });

        $rootScope.isMenuActive = function(menu) {
            return $location.path().search(menu) !== -1
        };

        $rootScope.hasRole = function(role) {
            if (!$rootScope.user) {
                return false;
            }

            return _.contains($rootScope.user.roles, role);
        };

        $rootScope.isTestMode = function() {
            return Config['testMode']
        };

        $scope.logout = function() {
            loginService.logout(function(result) {
                $rootScope.user = undefined;
                $rootScope.authorized = undefined;
                $location.path('/login');
            });
        };

        $rootScope.applicationInitialized = true;
        $('#main-menu').show();
        if (Config['testMode']) {
            $('#build-info').show();
        }
    }];
});