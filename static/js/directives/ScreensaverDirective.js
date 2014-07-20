define([
    'Angular',
    'Underscore',
    'Messages',
    'text!view/edit/screensaver-template.html'], function (angular, _, messages, screensaverTemplate) {
    return ['$compile', 'NewsService', function ($compile, newsService) {
        return {
            scope: {
                object: '=',
                type: '='
            },
            restrict: 'E',
            link: function ($scope, element, attrs) {
                $scope.messages = messages;

                var object;
                var parentScreensaverInfo;
                var currentPicked;

                var isCurrentOwner = function() {
                    return $scope.object.screensaverInfo.owner === $scope.type
                };

                var prepare = function() {
                    object = $scope.object;
                    if (!object.screensaverInfo) {
                        object.screensaverInfo = {
                            owner: $scope.type
                        };
                    } else {
                        if (object.screensaverInfo.owner !== $scope.type) {
                            parentScreensaverInfo = {
                                owner: object.screensaverInfo.owner,
                                url: object.screensaverInfo.url
                            }
                        } else if (object.screensaverInfo.parentUrl && object.screensaverInfo.parentType) {
                            parentScreensaverInfo = {
                                owner: object.screensaverInfo.parentType,
                                url: object.screensaverInfo.parentUrl
                            }
                        }
                    }
                    if (!angular.isDefined(object.enableScreensaverEdit)) {
                        object.enableScreensaverEdit = object.screensaverInfo.owner === $scope.type;
                    }
                };

                var trySetParent = function() {
                    if (parentScreensaverInfo) {
                        object.screensaverInfo.owner = parentScreensaverInfo.owner;
                        object.screensaverInfo.url = parentScreensaverInfo.url;
                    } else {
                        object.screensaverInfo.owner = null
                    }
                };

                var trySetCurrentPicked = function () {
                    if (currentPicked) {
                        object.screensaverInfo.owner = currentPicked.owner;
                        object.screensaverInfo.url = currentPicked.url;
                    }
                };

                $scope.onSelectMf = function (selectedMf) {
                    if ($scope.isScreensaverEditEnabled()) {
                        object.screensaverInfo.owner = $scope.type;
                        object.screensaverInfo.url = selectedMf.url;
                        currentPicked = {
                            owner: $scope.type,
                            url: selectedMf.url
                        }
                    }
                };

                $scope.revertScreensaverEdit = function () {
                    $scope.object.enableScreensaverEdit = !$scope.object.enableScreensaverEdit;
                    if (!$scope.object.enableScreensaverEdit) {
                        trySetParent();
                    } else {
                        trySetCurrentPicked();
                        $scope.object.screensaverInfo.owner=$scope.type;
                    }
                };
                $scope.isScreensaverEditEnabled = function () {
                    return $scope.object.enableScreensaverEdit;
                };

                $scope.showHierarchyInfo = function() {
                    return $scope.object.screensaverInfo.owner && !isCurrentOwner()
                };

                $scope.showCurrentSettings = function() {
                    return isCurrentOwner()
                };

                $scope.$watch('object', function(o) {
                    if (o) { prepare() }
                });

                prepare();

                element.append($compile(screensaverTemplate)($scope));
            }
        };
    }];
});