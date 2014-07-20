define([
    'Config',
    'Messages',
    'Console',
    'jQuery'
], function(Config, messages, Console, $) {
    return ['$scope', '$rootScope', '$location', '$cookieStore', 'LoginService', function($scope, $rootScope, $location, $cookieStore, loginService) {
        $scope.login = function() {
            loginService.login($.param({username: $scope.username, password: $scope.password}), function(result) {
                $rootScope.user = {
                    username: result.username,
                    roles: result.roles
                };
                $rootScope.authorized = true;
                $location.path('/schema')
            }, function(error) {
                if (error.status === 401) {
                    $scope.authError = messages['login.error']
                }
            });
        };
    }];
});