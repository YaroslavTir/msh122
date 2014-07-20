define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'im/:imId', {imId: '@imId'}, {
            update: {
                method: 'PUT',
                params: {
                    imId: '@imId'
                },
                isArray: false
            }
        });
    }
});