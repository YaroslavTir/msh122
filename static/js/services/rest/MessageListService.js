define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'message/:ownerType/:ownerId/:playing', {},
            {
                query:{
                    method: 'GET',
                    params: {
                        ownerType:'@ownerType',
                        ownerId:'@ownerId'
                    },
                    isArray:true
                } ,
                get: {
                    method: 'GET',
                    params: {
                        ownerType:'@ownerType',
                        ownerId:'@ownerId',
                        playing:'playing'
                    },
                    isArray:false
                }
            }
        )
    }
});