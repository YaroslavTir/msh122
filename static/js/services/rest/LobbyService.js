define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'lobby/:lobbyId', {lobbyId: '@lobbyId'}, {
            update: {
                method: 'PUT',
                params: {
                    lobbyId: '@lobbyId'
                },
                isArray: false
            }
        });
    }
});