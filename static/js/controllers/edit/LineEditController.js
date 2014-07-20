define([
    'Console'
], function (Console) {
    return ['$scope', '$routeParams', '$controller', 'LineService',
        function ($scope, $routeParams, $controller, lineService) {

            $controller('AbstractEditController', {$scope: $scope});

            $scope.getService = function() {
                return lineService
            };

            $scope.getType = function() {
                return 'LINE';
            };

            $scope.getLoadParams = function(id) {
                return {
                    lineId: id
                }
            };

            $scope.getSaveObject = function () {
                return $scope.line
            };

            $scope.beforeProcessCb = function(line) {
                $scope.line = line;
            };

            $scope.prepareAdditionalParams = function(o) {
                var line = $scope.line;
                o.schemaId = line.schemaId;
                o.number = line.number;
                o.color = line.color;
                o.enname = line.enname;
                o.picLink = '../img/schema/schema_' + line.number + '.png';
                return o;
            };

            $scope.loadObject($routeParams.id)
        }];
});