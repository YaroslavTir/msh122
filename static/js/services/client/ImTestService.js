define(['Console', 'Config', 'Underscore'], function (Console, Config, _) {
    return function ($http) {
        this.registration = function (data, cb) {
            $http({
                url: Config.imApiBaseUrl + 'registration',
                method: 'POST',
                data: data
            }).success(function (token) {
                if (cb) {
                    cb(token)
                }
            });
        };

        this.getContent = function (token, cb) {
            $http({
                url: Config.imApiBaseUrl + 'content',
                method: 'GET',
                headers: {
                    'X-Auth-Token': token
                }
            }).success(function (content) {
                if (_.isFunction(cb)) {
                    cb(content)
                }
            });
        };

        this.sendMessage = function (pm, token, cb) {
            $http({
                url: Config.imApiBaseUrl + 'passenger-message',
                method: 'POST',
                headers: {
                    'X-Auth-Token': token
                },
                data: pm
            }).success(function () {
                if (_.isFunction(cb)) {
                    cb()
                }
            });
        };

        this.sendStatistic = function(statistic, token, cb) {
            $http({
                url: Config.imApiBaseUrl + 'statistic',
                method: 'POST',
                headers: {
                    'X-Auth-Token': token
                },
                data: statistic
            }).success(function () {
                if (_.isFunction(cb)) {
                    cb()
                }
            });
        }
    }
});