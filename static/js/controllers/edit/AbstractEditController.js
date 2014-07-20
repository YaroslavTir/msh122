define([
    'Console',
    'Underscore',
    'Messages',
    'Bootbox',
    'EditControllerUtils',
    'text!view/edit/news.html',
    'text!view/edit/help-info.html',
    'text!view/edit/banner-settings.html',
    'text!view/edit/screensaver.html',
    'text!view/edit/messages.html'
], function (Console, _, Messages, Bootbox, editControllerUtils,
             editNewsTemplate, editHelpTemplate, bannerSettingsTemplate, editScreensaverTemplate, messagesTemplate) {
    return ['$scope', 'ValidationErrorProcessorService', 'NewsService',
        function ($scope, validationErrorProcessorService, newsService) {
            $scope.loadObject = function(id) {

                var getScreensaver = function (o) {
                    return o.screensaverInfo.owner === $scope.getType() ? o.screensaverInfo.url : null
                };

                var prepareForSave = function() {
                    var o = $scope.getSaveObject();
                    var result = {
                        id: o.id,
                        name: o.name,
                        news: newsService.preparedNews(o.currentNews),
                        messageSchedule: editControllerUtils.prepareMessageScheduleForSave(o.messageSchedule),
                        screensaverUrl: getScreensaver(o),
                        bannerSettings: editControllerUtils.prepareBannerSettingsForSave(o.bannerSettings),
                        helpInfos: o.helpInfos
                    };
                    if (_.isFunction($scope.prepareAdditionalParams)) {
                        result = $scope.prepareAdditionalParams(result);
                    }
                    return result;
                };

                $scope.runProcess = function(o) {
                    if (_.isFunction($scope.beforeProcessCb)) {
                        $scope.beforeProcessCb(o)
                    }
                    o.messageSchedule = editControllerUtils.prepareMessageScheduleForEditing(o.messageSchedule);
                    o.bannerSettings = editControllerUtils.prepareBannerSettingsForEditing(o.bannerSettings);
                    $scope.object = o;
                    $scope.type = $scope.getType();
                    $scope.editErrors = editControllerUtils.createEditErrors();
                    $scope.hasError = function () {
                        return editControllerUtils.hasError($scope.editErrors);
                    };
                    $scope.editErrorMessage = function () {
                        return editControllerUtils.getEditErrorMessage($scope.editErrors, Messages);
                    };

                    $scope.onSaveClick = function () {
                        $scope.updateData = true;
                        $scope.getService().update({}, prepareForSave(), function (o) {
                            $scope.updateData = false;
                            $scope.errors = [];
                            Bootbox.alert(Messages['default.object.saved.message']);
                            $scope.loadObject(o.id);
                        }, function (error) {
                            validationErrorProcessorService.process(error, function (errors) {
                                $scope.errors = errors
                            });
                            $scope.updateData = false;
                        })
                    };

                    if (_.isFunction($scope.afterProcessCb)) {
                        $scope.afterProcessCb(o);
                    }
                    $scope.newsTemplate = editNewsTemplate;
                    $scope.helpTemplate = editHelpTemplate;
                    $scope.bannerSettingsTemplate = bannerSettingsTemplate;
                    $scope.screensaverTemplate = editScreensaverTemplate;
                    $scope.messagesTemplate = messagesTemplate;
                    if (_.isFunction($scope.initAdditionalTemplates)) {
                        $scope.initAdditionalTemplates(o);
                    }
                    $scope.loadData = false;
                };

                $scope.loadData = true;
                if ($scope.getLoadFn) {
                    $scope.getLoadFn(id)
                } else {
                    $scope.getService().get($scope.getLoadParams(id), {}, function(o) {
                        $scope.runProcess(o);
                    })
                }
            }
        }];
});