define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'media/:mediaId', {},
            {
                update: {
                    method: 'PUT',
                    params: {
                        mediaId:'@mediaId'
                    }
                },
                delete:{
                    method: 'DELETE',
                    params: {
                        mediaId:'@mediaId'
                    }
                },
                info:{
                    method: 'GET',
                    params: {
                        mediaId:'@mediaId'
                    }
                },
                list:{
                    method: 'GET',
                    params: {
                    },
                    isArray:true
                }
            }
        )
    }
});