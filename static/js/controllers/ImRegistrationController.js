define([
    'Config',
    'Console',
    'jQuery'
], function(Config, Console, $) {
    return ['$scope', '$rootScope', '$location', '$cookieStore', 'ImTestService', function($scope, $rootScope, $location, $cookieStore, imTestService) {
        $scope.registration = function() {
            imTestService.registration($.param({name: $scope.imName}), function(token) {
                Console.log(token);
                $scope.token = token
            });
        };
        $scope.getContent = function() {
            imTestService.getContent($scope.token, function(content) {
                Console.log(content);
                $scope.content = JSON.stringify(content)
            })
        };
        $scope.pm = {};
        $scope.sendMessage = function() {
            $scope.messageResult = '';
            imTestService.sendMessage($scope.pm, $scope.token, function() {
                $scope.messageResult = 'OK';
            });
        };

        $scope.statistic = {
            events: [
                { event: 'MapActivated' },
                { event: 'MapSearch' },
                { event: 'MapUsing' },
                { event: 'MetroMapActivated' },
                { event: 'MetroMapUsing' },
                { event: 'StationSchemeActivated' },
                { event: 'ScreenSaverTakenOff' },
                { event: 'HelpActivated' },
                { event: 'FeedbackActivated' }
            ]
        };
        $scope.sendStatistic = function() {
            $scope.statisticSendResult = '';
            $scope.statistic.from = new Date().getTime();
            var toDate = new Date();
            toDate.setHours(toDate.getHours() + 1);
            $scope.statistic.to = toDate.getTime();

            imTestService.sendStatistic($scope.statistic, $scope.token, function() {
                $scope.statisticSendResult = 'OK';
            });
        }
    }];
});
