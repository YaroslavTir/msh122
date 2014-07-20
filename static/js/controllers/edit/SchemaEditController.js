define([
    'Console',
    'text!view/schema/edit/main.html'
], function (Console, editMainTemplate) {
    return ['$scope', '$routeParams', '$controller', 'SchemaService',
        function ($scope, $routeParams, $controller, schemaService) {

            $controller('AbstractEditController', {$scope: $scope});

            $scope.getService = function() {
                return schemaService
            };

            $scope.getType = function() {
                return 'SCHEMA';
            };

            $scope.getSaveObject = function () {
                return $scope.schema
            };

            $scope.beforeProcessCb = function(schema) {
                $scope.schema = schema;
            };

            $scope.getLoadFn = function(id) {
                schemaService.list(function (schema) {
                    $scope.runProcess(schema);
                });
            };

            $scope.prepareAdditionalParams = function (o) {
                var schema = $scope.schema;
                o.settings = schema.settings;
                return o;
            };

            $scope.initAdditionalTemplates = function() {
                $scope.mainTemplate = editMainTemplate;
            };

            $scope.loadObject();
        }];
});