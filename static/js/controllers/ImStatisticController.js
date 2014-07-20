define([
    'Console',
    'Config',
    'Bootbox',
    'AngularGridDoubleClick',
    'Messages',
    'text!view/im-statistic/im-statistic-view-dialog.html'
], function (Console, Config, Bootbox, AngularGridDoubleClick, Messages, viewDialogTemplate) {
    return ['$scope', '$compile', 'ImStatisticService', function ($scope, $compile, imStatisticService) {

        $scope.eventTypes = [
            'MAP_ACTIVATED',
            'METRO_MAP_ACTIVATED',
            'STATION_SCHEME_ACTIVATED',
            'SCREEN_SAVER_TAKEN_OFF',
            'HELP_ACTIVATED',
            'FEEDBACK_ACTIVATED'
        ];

        $scope.imUrl = Config.apiBaseUrl + 'im?imName=';

        $scope.filter = {
            im: undefined,
            groupPeriod: '',
            periodDateFrom: '',
            periodDateTo: ''
        };

        var setData = function (data) {
            $scope.statisticData = data.data;
        };

        var getData = function (filter) {
            $scope.updateData = true;
            imStatisticService.get({
                imName: filter.im ? filter.im.title : undefined,
                groupPeriod: filter.groupPeriod,
                periodDateFrom: filter.periodDateFrom ? new Date(filter.periodDateFrom).getTime() : null,
                periodDateTo: filter.periodDateTo ? new Date(filter.periodDateTo).getTime() : null
            }, function (data) {
                $scope.updateData = false;
                setData(data)
            });
        };

        $scope.doFilter = function () {
            getData($scope.filter)
        };

        $scope.doClean = function () {
            $scope.filter.im = undefined;
            $scope.filter.groupPeriod = '';
            $scope.filter.periodDateFrom = '';
            $scope.filter.periodDateTo = '';
            $scope.doFilter();
        };

        $scope.doFilter();
    }];
});