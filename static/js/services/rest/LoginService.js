define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'login/:action', {},
            {
                login: {
                    method: 'POST',
                    params: {
                        action: 'login'
                    }
                },
                logout: {
                    method: 'POST',
                    params: {
                        action: 'logout'
                    }
                },
                info: {
                    method: 'GET',
                    params: {
                        action: 'info'
                    }
                }
            }
        )
    }
});