define(['Console', 'Config'], function(Console, Config) {
    return function($rootScope, $resource) {
        return $resource(Config.apiBaseUrl + 'message/:messageId/:run', {},
            {
                save: {
                    method: 'POST',
                    params: {

                    }
                },
                delete:{
                    method: 'DELETE',
                    params: {
                        messageId:'@messageId'
                    }
                },
                info:{
                    method: 'GET',
                    params: {
                        messageId:'@messageId'
                    }
                },
                play:{
                    method:'PUT',
                    params:{
                        messageId:'@messageId',
                        run:'play'
                    }
                },
                stop:{
                    method:'PUT',
                    params:{
                        messageId:'@messageId',
                        run:'stop'
                    }
                }
            }
        )
    }
});