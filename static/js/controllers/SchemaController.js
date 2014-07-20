define([
    'Console',
    'Bootbox',
    'Messages',
    'Config',
    'Underscore',
    'text!view/im/create/im-create-dialog.html',
    'text!view/lobby/create/lobby-create-dialog.html',
    'text!view/line/create/line-create-dialog.html',
    'text!view/station/create/station-create-dialog.html'
], function (Console, Bootbox, Messages, Config, _, imCreateDialogContent, lobbyCreateDialogContent, lineEditDialogContent, stationEditDialogContent) {
    return ['$scope',
        '$rootScope',
        '$compile',
        '$location',
        '$cookieStore',
        'SchemaService',
        'ImService',
        'ImStatusService',
        'LineService',
        'StationService',
        'LobbyService',
        'ValidationErrorProcessorService',
        function ($scope, $rootScope, $compile, $location, $cookieStore,
                  schemaService, imService, imStatusService, lineService, stationService, lobbyService, validationErrorProcessorService) {

            $scope.showPort = Config['testMode'];

            $scope.reload = function () {
                schemaService.list(function (schema) {
                    if (!schema.lines) {
                        return;
                    }

                    if (!$cookieStore.get('selectionModel')) {
                        var selectionModel = {};
                        selectionModel.selectinLineId = schema.lines[0].id;
                        selectionModel.selectinStationIds = [];
                        selectionModel.selectinLobbyIds = [];

                        schema.lines[0].isSelected = true;
                        schema.selectedIndex = 0;
                        for (var l in schema.lines) {
                            var line = schema.lines[l];
                            line.type = 'LINE';
                            if (!line.stations) {
                                continue;
                            }
                            line.stations[0].isSelected = true;
                            line.selectedIndex = 0;
                            selectionModel.selectinStationIds.push(line.stations[0].id);

                            var stations = line.stations;
                            for (var s in stations) {
                                stations[s].type = 'STATION';
                                if (!stations[s].lobbies) {
                                    continue;
                                }
                                stations[s].lobbies[0].isSelected = true;
                                stations[s].selectedIndex = 0;
                                selectionModel.selectinLobbyIds.push(stations[s].lobbies[0].id);
                            }
                        }

                        $cookieStore.put('selectionModel', selectionModel);
                    } else {
                        var selectionModel = $cookieStore.get('selectionModel');

                        var selectedLineFound = false;
                        for (var l in schema.lines) {
                            var line = schema.lines[l];
                            line.type = 'LINE';
                            if (line.id == selectionModel.selectinLineId) {
                                line.isSelected = true;
                                schema.selectedIndex = l;
                                selectedLineFound = true
                            }

                            if (!line.stations) {
                                continue;
                            }


                            var stations = line.stations;
                            var selectedStationFound = false;
                            for (var s in stations) {
                                stations[s].type = 'STATION';
                                if (selectionModel.selectinStationIds.indexOf(stations[s].id) >= 0) {
                                    selectedStationFound = true;
                                    line.stations[s].isSelected = true;
                                    line.selectedIndex = s;
                                }

                                if (!stations[s].lobbies) {
                                    continue;
                                }

                                var selectedLobbyFound = false;
                                for (var lb in stations[s].lobbies) {
                                    var lobby = stations[s].lobbies[lb];
                                    if (selectionModel.selectinLobbyIds.indexOf(lobby.id) >= 0) {
                                        selectedLobbyFound = true;
                                        lobby.isSelected = true;
                                        stations[s].selectedIndex = lb;
                                    }
                                }
                                if (!selectedLobbyFound) {
                                    stations[s].lobbies[0].isSelected = true;
                                    stations[s].selectedIndex = 0;
                                    selectionModel.selectinLobbyIds.push(stations[s].lobbies[0].id);
                                }

                            }
                            if (!selectedStationFound) {
                                line.stations[0].isSelected = true;
                                line.selectedIndex = 0;
                                selectionModel.selectinStationIds.push(line.stations[0].id);
                            }
                        }
                        if (!selectedLineFound) {
                            schema.lines[0].isSelected = true;
                            schema.selectedIndex = 0;
                        }
                    }

                    $scope.schema = schema;

                });
            };

            $scope.setSelected = function(item, items, parent) {
                item.isSelected = true;
                if (parent) {
                    var newSelectedIndex = items.indexOf(item);
                    if (newSelectedIndex != parent.selectedIndex) {
                        var selectionModel = $cookieStore.get('selectionModel');

                        if (item.type == 'LINE') {
                            selectionModel.selectinLineId = item.id;
                        } else if (item.type == 'STATION') {
                            selectionModel.selectinStationIds.push(item.id);
                            if (parent.selectedIndex || parent.selectedIndex == 0) {
                                var indexForRemove = selectionModel.selectinStationIds.indexOf(
                                    items[parent.selectedIndex].id);
                                selectionModel.selectinStationIds.splice(indexForRemove, 1);
                            }
                        } else {
                            selectionModel.selectinLobbyIds.push(item.id);
                            if (parent.selectedIndex || parent.selectedIndex == 0) {
                                var indexForRemove = selectionModel.selectinLobbyIds.indexOf(
                                    items[parent.selectedIndex].id);
                                selectionModel.selectinLobbyIds.splice(indexForRemove, 1);
                            }
                        }
                        if (parent.selectedIndex || parent.selectedIndex == 0) {
                            items[parent.selectedIndex].isSelected = false;
                        }
                        parent.selectedIndex = items.indexOf(item);
                        $cookieStore.put('selectionModel', selectionModel);
                    }
                }
            };

            $scope.$watch('authorized', function (authorized) {
                if (authorized) {
                    $scope.reload();
                }
            });

            $scope.addIm = function () {
                var $imScope = $scope.$new();
                $imScope.id = null;
                var d = Bootbox.dialog({
                    message: $compile(imCreateDialogContent)($imScope),
                    title: Messages['schema.im.create.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['users.user.edit.dialog.button.cancel']
                        },
                        save: {
                            label: Messages['users.user.edit.dialog.button.save'],
                            callback: function () {
                                var selectedLine = $scope.schema.lines[$scope.schema.selectedIndex];
                                imService.save({}, {
                                    id: $imScope.id,
                                    lobbyId: selectedLine.stations[selectedLine.selectedIndex].
                                        lobbies[selectedLine.stations[selectedLine.selectedIndex].selectedIndex].id,
                                    name: $imScope.name,
                                    imName: $imScope.imName,
                                    position : $imScope.position,
                                    port: $imScope.port
                                }, function (im) {
                                    d.modal('hide');
                                    $scope.reload()
                                }, function(error) {
                                    validationErrorProcessorService.process(error, function(errors) {
                                        $imScope.errors = errors
                                    })
                                });
                                return false;
                            }
                        }
                    }
                });
            };

            $scope.addLobby = function() {
                var $lobbyScope = $scope.$new();
                $lobbyScope.id = null;
                var d = Bootbox.dialog({
                    message: $compile(lobbyCreateDialogContent)($lobbyScope),
                    title: Messages['schema.lobby.create.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['default.button.cancel']
                        },
                        save: {
                            label: Messages['default.button.save'],
                            callback: function () {
                                var selectedLine = $scope.schema.lines[$scope.schema.selectedIndex];
                                lobbyService.save({}, {
                                    id: $lobbyScope.id,
                                    stationId: selectedLine.stations[selectedLine.selectedIndex].id,
                                    name: $lobbyScope.name
                                }, function (lobby) {
                                    d.modal('hide');
                                    lobby.ims = [];
                                    if (!selectedLine.stations[selectedLine.selectedIndex].lobbies) {
                                        selectedLine.stations[selectedLine.selectedIndex].lobbies = [];
                                    }
                                    selectedLine.stations[selectedLine.selectedIndex].lobbies.push(lobby);
                                    $scope.setSelected(
                                        lobby,
                                        selectedLine.stations[selectedLine.selectedIndex].lobbies,
                                        selectedLine.stations[selectedLine.selectedIndex])
                                    //$scope.reload();
                                }, function(error) {
                                    validationErrorProcessorService.process(error, function(errors) {
                                        $lobbyScope.errors = errors
                                    })
                                });
                                return false;
                            }
                        }
                    }
                });
            };

            $scope.addStation = function () {
                var $stationScope = $scope.$new();
                $stationScope.id = null;
                var d = Bootbox.dialog({
                    message: $compile(stationEditDialogContent)($stationScope),
                    title: Messages['schema.station.create.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['default.button.cancel']
                        },
                        save: {
                            label: Messages['default.button.save'],
                            callback: function () {
                                var selectedLine = $scope.schema.lines[$scope.schema.selectedIndex];
                                stationService.save({}, {
                                    id: $stationScope.id,
                                    lineId: selectedLine.id,
                                    name: $stationScope.name,
                                    enname : $stationScope.enname
                                }, function (station) {
                                    d.modal('hide');
                                    station.lobbies = [];
                                    station.type= 'STATION';
                                    selectedLine.stations.push(station);
                                    $scope.setSelected(
                                        station,
                                        selectedLine.stations,
                                        selectedLine)
                                   // $scope.reload();
                                }, function(error) {
                                    validationErrorProcessorService.process(error, function(errors) {
                                        $stationScope.errors = errors
                                    })
                                });
                                return false;
                            }
                        }
                    }
                });
            };

            $scope.addLine = function () {
                var $lineScope = $scope.$new();
                $lineScope.id = null;
                var d = Bootbox.dialog({
                    message: $compile(lineEditDialogContent)($lineScope),
                    title: Messages['schema.line.create.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['users.user.edit.dialog.button.cancel']
                        },
                        save: {
                            label: Messages['users.user.edit.dialog.button.save'],
                            callback: function () {
                                lineService.save({}, {
                                    id: $lineScope.id,
                                    schemaId: $scope.schema.id,
                                    name: $lineScope.name,
                                    number: $lineScope.number,
                                    picLink: '../img/schema/schema_' + $lineScope.number + '.png',
                                    color: $lineScope.color
                                }, function (line) {
                                    d.modal('hide');
                                    $scope.reload()
                                }, function(error) {
                                    validationErrorProcessorService.process(error, function(errors) {
                                        $lineScope.errors = errors
                                    })
                                });
                                return false;
                            }
                        }
                    }
                });
            };

            $scope.editIm = function (id) {
                $location.path('/schema/im/edit/' + id);
            };
            $scope.editLobby = function (id) {
                $location.path('/schema/lobby/edit/' + id);
            };
            $scope.editStation = function(id) {
                $location.path('/schema/station/edit/' + id);
            };
            $scope.editLine = function (id) {
                $location.path('/schema/line/edit/' + id);
            };
            $scope.editSchema = function()  {
                $location.path('/schema/schema/edit')
            };

            var runDelete = function(cb) {
                Bootbox.dialog({
                    message: Messages['default.object.remove.dialog.msg'],
                    title: Messages['default.object.remove.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['default.button.cancel']
                        },
                        remove: {
                            label: Messages['default.button.remove'],
                            callback: function() {
                                if (_.isFunction(cb)) {
                                    cb()
                                }
                            }
                        }
                    }
                });
            };

            var processDeleteResult = function(result) {
                Console.log(result);
                switch (result.status) {
                    case 'OK': {
                        Bootbox.alert(Messages['default.object.removed.message']);
                        $scope.reload();
                        break;
                    }
                    case 'REMOVE_CHILD': {
                        Bootbox.alert(Messages['default.object.remove.child.error.message']);
                        break;
                    }
                    case 'ERROR': {
                        Bootbox.alert(Messages['default.object.remove.error.message']);
                        break;
                    }
                }
            };

            $scope.deleteIm = function(id) {
                runDelete(function() {
                    imService.delete({imId: id}, {}, function(result) {
                        processDeleteResult(result);
                    });
                });
            };
            $scope.deleteLobby = function(id) {
                runDelete(function() {
                    lobbyService.delete({lobbyId: id}, {}, function(result) {
                        processDeleteResult(result);
                    });
                });
            };
            $scope.deleteStation = function(id) {
                runDelete(function() {
                    stationService.delete({stationId: id}, {}, function(result) {
                        processDeleteResult(result);
                    });
                });
            };
            $scope.deleteLine = function(id) {
                runDelete(function() {
                    lineService.delete({lineId: id}, {}, function(result) {
                        processDeleteResult(result);
                    });
                });
            };

            $scope.imStatus = function(imName) {
                imStatusService.get({imName: imName}, {}, function(status) {
                    Bootbox.dialog({
                        message:(status.success)?Messages['schema.im.status.dialog.success']:Messages['schema.im.status.dialog.error']+status.errorMessage,
                        title: Messages['schema.im.status.dialog.title'],
                        buttons: {
                            ok: { }
                        }
                    });
                })
            };
        }];
});