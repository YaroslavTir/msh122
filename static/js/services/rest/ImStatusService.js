define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'im/status/:imName', {imName: '@imName'});
    }
});