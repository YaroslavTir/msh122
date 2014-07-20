define([
    'Console',
    'Config',
    'text!view/im/edit/map.html',
    'text!view/edit/station-plan.html'
], function (Console, Config, editMapTemplate, stationPlanTemplate) {
    return ['$controller', '$scope', '$routeParams', 'ImService',
        function ($controller, $scope, $routeParams, imService) {
            $controller('AbstractEditController', {$scope: $scope});

            $scope.getService = function() {
                return imService
            };

            $scope.getType = function() {
                return 'IM';
            };

            $scope.getLoadParams = function(id) {
                return {
                    imId: id
                }
            };

            $scope.getSaveObject = function () {
                return $scope.im
            };

            $scope.beforeProcessCb = function(im) {
                $scope.im = im;
            };

            var createMap = function (scope, coord) {
                return {
                    center: {
                        latitude: coord && coord.latitude ? coord.latitude : Config['google.maps'].defaults.center.latitude,
                        longitude: coord && coord.longitude ? coord.longitude : Config['google.maps'].defaults.center.longitude
                    },
                    zoom: Config['google.maps'].defaults.zoom,
                    control: {

                    },
                    options: {
                        minZoom: Config['google.maps'].minZoom,
                        keyboardShortcuts: false,
                        streetViewControl: false
                    },
                    bounds: {
                        northeast: {
                            latitude: Config['google.maps'].bounds.northeast.latitude,
                            longitude: Config['google.maps'].bounds.northeast.longitude
                        },
                        southwest: {
                            latitude: Config['google.maps'].bounds.southwest.latitude,
                            longitude: Config['google.maps'].bounds.southwest.longitude
                        }
                    },
                    clickedMarker: {
                        latitude: coord && coord.latitude ? coord.latitude : null,
                        longitude: coord && coord.longitude ? coord.longitude : null
                    },
                    events: {
                        click: function (mapModel, eventName, originalEventArgs) {
                            var e = originalEventArgs[0];

                            if (!scope.map.clickedMarker) {
                                scope.map.clickedMarker = {
                                    latitude: e.latLng.lat(),
                                    longitude: e.latLng.lng()
                                };
                            }
                            else {
                                scope.map.clickedMarker.latitude = e.latLng.lat();
                                scope.map.clickedMarker.longitude = e.latLng.lng();
                            }

                            scope.$apply();
                        }
                    }
                };
            };

            $scope.initAdditionalTemplates = function() {
                $scope.mapTemplate = editMapTemplate;
                $scope.stationPlanTemplate = stationPlanTemplate;
            };

            var computeLatitude = function (l) {
                if (Config['google.maps'].bounds.northeast.latitude <= l) {
                    return Config['google.maps'].bounds.northeast.latitude;
                }
                if (Config['google.maps'].bounds.southwest.latitude >= l) {
                    return Config['google.maps'].bounds.southwest.latitude;
                }
                return l
            };

            var computeLongitude = function (l) {
                if (Config['google.maps'].bounds.northeast.longitude <= l) {
                    return Config['google.maps'].bounds.northeast.longitude;
                }
                if (Config['google.maps'].bounds.southwest.longitude >= l) {
                    return Config['google.maps'].bounds.southwest.longitude;
                }
                return l;
            };

            $scope.afterProcessCb = function(im) {
                $scope.showPort = Config['testMode'];

                if (!$scope.map) {
                    $scope.map = createMap($scope, {
                        latitude: im.latitude,
                        longitude: im.longitude
                    });
                    $scope.additionalData = {};
                    $scope.additionalData.map = $scope.map;
                    $scope.mapCallback = function () {
                        var coord = null;
                        if ($scope.map.clickedMarker && $scope.map.clickedMarker.latitude && $scope.map.clickedMarker.longitude) {
                            coord = $scope.map.clickedMarker;
                        } else {
                            coord = {
                                latitude: Config['google.maps'].defaults.center.latitude,
                                longitude: Config['google.maps'].defaults.center.longitude
                            }
                        }

                        $scope.map.control.refresh(coord);
                    };
                }

                $scope.$watch('map.center.latitude', function (newValue, oldValue) {
                    if (newValue && newValue !== oldValue) {
                        var l = computeLatitude(newValue);
                        if (l !== newValue) {
                            $scope.map.center.latitude = l;
                        }
                    }
                });
                $scope.$watch('map.center.longitude', function (newValue, oldValue) {
                    if (newValue && newValue !== oldValue) {
                        var l = computeLongitude(newValue);
                        if (l !== newValue) {
                            $scope.map.center.longitude = l;
                        }
                    }
                });
            };

            $scope.prepareAdditionalParams = function(o) {
                var im = $scope.im;
                var coord = $scope.map.clickedMarker;
                o.imName = im.imName;
                o.ip = im.ip;
                o.port = im.port;
                o.lobbyId = im.lobbyId;
                o.position = im.position;
                o.latitude = coord ? coord.latitude : null;
                o.longitude = coord ? coord.longitude : null;
                o.stationPlanSettings = im.stationPlanSettings;
                return o;
            };

            $scope.loadObject($routeParams.id);
        }];
});