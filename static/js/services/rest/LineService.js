define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'line/:lineId', {lineId: '@lineId'}, {
            update: {
                method: 'PUT',
                params: {
                    lineId: '@lineId'
                },
                isArray: false
            }
        });
    }
});