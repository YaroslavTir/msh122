define([
    'Console',
    'Bootbox',
    'Messages',
    'Config',
    'Underscore',
    'text!view/users/dialog/user-edit-dialog.html'
], function (Console, Bootbox, Messages, Config, _, userEditDialogContent) {
    return ['$scope', '$compile', 'UserService', 'ValidationErrorProcessorService',
        function ($scope, $compile, userService, validationErrorProcessorService) {
            var rolesMap = _.object(_.collect(Config.roles, function (role) {
                return [role.value, role.name];
            }));

            $scope.reload = function () {
                userService.query(function (users) {
                    _.each(users, function (user) {
                        user.roles = _.collect(user.roles, function (role) {
                            return rolesMap[role];
                        }).join(', ');
                    });
                    $scope.users = users;
                });
            };

            $scope.$watch('authorized', function (authorized) {
                if (authorized) {
                    $scope.reload();
                }
            });

            $scope.roles = Config.roles;
            var $dialogScope;
            $scope.toggleSelection = function (value) {
                var idx = $dialogScope.selectedRoles.indexOf(value);
                if (idx > -1) {
                    $dialogScope.selectedRoles.splice(idx, 1);
                }
                else {
                    $dialogScope.selectedRoles.push(value);
                }
            };

            $scope.addUser = function () {
                $dialogScope = $scope.$new();
                $dialogScope.selectedRoles = [];
                var d = Bootbox.dialog({
                    message: $compile(userEditDialogContent)($dialogScope),
                    title: Messages['users.user.edit.dialog.title.add'],
                    buttons: {
                        cancel: {
                            label: Messages['users.user.edit.dialog.button.cancel']
                        },
                        save: {
                            label: Messages['users.user.edit.dialog.button.save'],
                            callback: function () {
                                userService.save({}, {
                                    id: $dialogScope.id,
                                    username: $dialogScope.username,
                                    password: $dialogScope.password,
                                    fio: $dialogScope.fio
                                }, function (user) {
                                    d.modal('hide');
                                    $scope.reload()
                                }, function (error) {
                                    validationErrorProcessorService.process(error, function (errors) {
                                        $dialogScope.errors = errors
                                    });
                                });
                                return false;
                            }
                        }
                    }
                });
            };

            $scope.editUser = function (user) {
                userService.get({userId: user.id}, {}, function (user) {
                    Console.log(user);
                    $dialogScope = $scope.$new();
                    $dialogScope.selectedRoles = user.roles;
                    $dialogScope.id = user.id;
                    $dialogScope.username = user.username;
                    $dialogScope.password = user.password;
                    $dialogScope.fio = user.fio;
                    var d = Bootbox.dialog({
                        message: $compile(userEditDialogContent)($dialogScope),
                        title: Messages['users.user.edit.dialog.title.edit'],
                        buttons: {
                            cancel: {
                                label: Messages['users.user.edit.dialog.button.cancel']
                            },
                            save: {
                                label: Messages['users.user.edit.dialog.button.save'],
                                callback: function () {
                                    userService.update({}, {
                                        id: user.id,
                                        username: $dialogScope.username,
                                        password: $dialogScope.password,
                                        roles: $dialogScope.selectedRoles,
                                        fio: $dialogScope.fio
                                    }, function (user) {
                                        d.modal('hide');
                                        $scope.reload()
                                    }, function (error) {
                                        validationErrorProcessorService.process(error, function (errors) {
                                            $dialogScope.errors = errors
                                        });
                                    });
                                    return false;
                                }
                            }
                        }
                    });
                });
            };

            $scope.deleteUser = function (user) {
                Bootbox.dialog({
                    message: Messages['users.user.remove.dialog.message'],
                    title: Messages['users.user.remove.dialog.title'],
                    buttons: {
                        cancel: {
                            label: Messages['users.user.remove.dialog.button.cancel']
                        },
                        ok: {
                            label: Messages['users.user.remove.dialog.button.ok'],
                            callback: function () {
                                userService.delete({userId: user.id}, {}, function (response) {
                                    Bootbox.alert(Messages['users.user.remove.result.msg.' + response.status]);
                                    $scope.reload();
                                });
                            }
                        }
                    }
                });
            }
        }];
});