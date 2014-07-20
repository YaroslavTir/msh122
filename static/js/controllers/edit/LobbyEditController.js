define([
    'Console',
    'text!view/edit/station-plan.html'
], function (Console, stationPlanTemplate) {
    return ['$scope', '$controller', '$routeParams', 'LobbyService',
        function ($scope, $controller, $routeParams, lobbyService) {

            $controller('AbstractEditController', {$scope: $scope});

            $scope.getService = function() {
                return lobbyService
            };

            $scope.getType = function() {
                return 'LOBBY';
            };

            $scope.getLoadParams = function(id) {
                return {
                    lobbyId: id
                }
            };

            $scope.getSaveObject = function () {
                return $scope.lobby
            };

            $scope.beforeProcessCb = function(lobby) {
                $scope.lobby = lobby;
            };

            $scope.prepareAdditionalParams = function (o) {
                var lobby = $scope.lobby;
                o.stationId = lobby.stationId;
                o.stationPlanSettings = lobby.stationPlanSettings;
                return o;
            };

            $scope.initAdditionalTemplates = function() {
                $scope.stationPlanTemplate = stationPlanTemplate;
            };

            $scope.loadObject($routeParams.id);
        }];
});