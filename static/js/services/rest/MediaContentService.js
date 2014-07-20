define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'mediacontent/:id',{},{
            delete:{
                method: 'DELETE',
                params: {
                    id:'@id'
                },
                isArray:false
            }
        });
    }
});