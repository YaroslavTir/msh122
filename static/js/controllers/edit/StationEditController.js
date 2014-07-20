define([
    'Console',
    'text!view/edit/station-plan.html'
], function (Console, stationPlanTemplate) {
    return ['$scope', '$routeParams', '$controller', 'StationService',
        function ($scope, $routeParams, $controller, stationService) {

            $controller('AbstractEditController', {$scope: $scope});

            $scope.getService = function() {
                return stationService
            };

            $scope.getType = function() {
                return 'STATION';
            };

            $scope.getLoadParams = function(id) {
                return {
                    stationId: id
                }
            };

            $scope.getSaveObject = function () {
                return $scope.station
            };

            $scope.beforeProcessCb = function(station) {
                $scope.station = station;
            };

            $scope.prepareAdditionalParams = function(o) {
                var station = $scope.station;
                o.lineId = station.lineId;
                o.enname = station.enname;
                o.stationPlanSettings = station.stationPlanSettings;
                return o;
            };

            $scope.initAdditionalTemplates = function() {
                $scope.stationPlanTemplate = stationPlanTemplate;
            };

            $scope.loadObject($routeParams.id)
        }];
});