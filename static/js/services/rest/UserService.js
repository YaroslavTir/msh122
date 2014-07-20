define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'user/:userId', {userId: '@userId'}, {
            update: {
                method: 'PUT',
                params: {
                    userId: '@userId'
                },
                isArray: false
            }
        });
    }
});