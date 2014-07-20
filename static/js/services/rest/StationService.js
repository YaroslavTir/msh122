define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'station/:stationId', {stationId: '@stationId'}, {
            update: {
                method: 'PUT',
                params: {
                    stationId: '@stationId'
                },
                isArray: false
            }
        });
    }
});