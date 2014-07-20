define([
    'Angular',
    'Messages',
    'Underscore',
    'Config',
    'text!view/edit/edit-template.html'
], function (angular, messages, _, config, editTemplate) {
    return function ($compile, $location, $route) {
        return {
            scope: {
                mainTemplate: '=',
                newsTemplate: '=',
                helpTemplate: '=',
                mapTemplate: '=',
                bannerSettingsTemplate: '=',
                messagesTemplate: '=',
                screensaverTemplate: '=',
                stationPlanTemplate: '=',
                mapCreateCallback: '&',
                object: '=',
                type: '=',
                additionalData: '=',
                errors:'='
            },
            restrict: 'E',
            template: editTemplate,
            link: function (scope, element, attrs) {
                var urlParts = $location.url().split('#');
                var urlTab;
                if (urlParts.length === 2) {
                    urlTab = urlParts[1];
                }

                scope.messages = messages;

                var templatesMap = {};
                var mapCallback = scope.mapCreateCallback;

                scope.enable = {};

                scope.isTabActive = function (tab) {
                    return scope.activeTab == tab;
                };
                var lastSelectedRoute;
                var tabSelected;

                scope.selectTab = function(tab) {
                    tabSelected = true;
                    selectTab(tab)
                };

                var selectTab = function (tab) {
                    lastSelectedRoute = $route.current;
                    switch (tab) {
                        case 'main':
                        {
                            scope.activeTab = 'main';
                            scope.activeTabTemplate = templatesMap['main'];
                            scope.cb = '';
                            break;
                        }
                        case 'news':
                        {
                            scope.activeTab = 'news';
                            scope.activeTabTemplate = templatesMap['news'];
                            scope.cb = '';
                            break;
                        }
                        case 'help':
                        {
                            scope.activeTab = 'help';
                            scope.activeTabTemplate = templatesMap['help'];
                            scope.cb = '';
                            break;
                        }
                        case 'bannersettings':
                        {
                            scope.activeTab = 'bannersettings';
                            scope.activeTabTemplate = templatesMap['bannersettings'];
                            break;
                        }
                        case 'messages':
                        {
                            scope.activeTab = 'messages';
                            scope.activeTabTemplate = templatesMap['messages'];
                            break;
                        }
                        case 'map':
                        {
                            scope.activeTab = 'map';
                            scope.activeTabTemplate = templatesMap['map'];
                            scope.cb = function () {
                                if (_.isFunction(mapCallback)) {
                                    mapCallback();
                                }
                            };
                            break;
                        }
                        case 'screensaver':
                        {
                            scope.activeTab = 'screensaver';
                            scope.activeTabTemplate = templatesMap['screensaver'];
                            break;
                        }
                        case 'stationplan':
                        {
                            scope.activeTab = 'stationplan';
                            scope.activeTabTemplate = templatesMap['stationplan'];
                            break;
                        }
                    }
                    if (tabSelected) {
                        $location.hash(tab)
                    }
                };

                var processSwitchToTab = function(template, tab) {
                    templatesMap[tab] = template;
                    scope.enable[tab] = !_.isEmpty(template);
                    if (!urlTab) {
                        if (attrs.defaultTab === tab && scope.enable[tab]) {
                            selectTab(tab)
                        }
                    } else if (urlTab === tab) {
                        selectTab(tab)
                    }
                };

                scope.$watch('mainTemplate', function() {
                    processSwitchToTab(scope.mainTemplate, 'main');
                });

                scope.$watch('newsTemplate', function() {
                    processSwitchToTab(scope.newsTemplate, 'news');
                });

                scope.$watch('helpTemplate', function() {
                    processSwitchToTab(scope.helpTemplate, 'help');
                });

                scope.$watch('mapTemplate', function() {
                    if (scope.additionalData) {
                        scope.map = scope.additionalData.map;
                    }
                    processSwitchToTab(scope.mapTemplate, 'map');
                });

                scope.$watch('bannerSettingsTemplate', function() {
                    processSwitchToTab(scope.bannerSettingsTemplate, 'bannersettings');
                });

                scope.$watch('messagesTemplate', function() {
                    processSwitchToTab(scope.messagesTemplate, 'messages');
                });

                scope.$watch('stationPlanTemplate', function() {
                    processSwitchToTab(scope.stationPlanTemplate, 'stationplan');
                });

                scope.$watch('screensaverTemplate', function() {
                    processSwitchToTab(scope.screensaverTemplate, 'screensaver');
                });

                scope.$on('$locationChangeSuccess', function(event) {
                    if (lastSelectedRoute && tabSelected) {
                        tabSelected = false;
                        $route.current = lastSelectedRoute;
                    }
                });
            }
        };
    };
});