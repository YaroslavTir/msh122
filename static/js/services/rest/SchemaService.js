define(['Console', 'Config'], function (Console, Config) {
    return function ($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'schema/:schemaId', {schemaId: '@schemaId'}, {
            list: {
                method: 'GET',
                isArray: false
            },
            update: {
                method: 'PUT',
                params: {
                    imId: '@schemaId'
                },
                isArray: false
            }
        });
    }
});