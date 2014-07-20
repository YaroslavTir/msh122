define([
    'Angular',
    'Underscore',
    'Messages',
    'text!view/edit/news-template.html'], function (angular, _, messages, newsTemplate) {
    return ['$compile', 'NewsService', function ($compile, newsService) {
        return {
            scope: {
                object: '=',
                type: '='
            },
            restrict: 'E',
            template: newsTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;
                var type = scope.type;
                if (angular.isDefined(obj)) {
                    var createEmptyNews = function () {
                        return {
                            id: null,
                            title: ''
                        }
                    };

                    var refreshNewsMin = function () {
                        $('#news_total').attr('min', _.filter(scope.o.currentNews,function (news) {
                            return !newsService.isNewsEmpty(news);
                        }).length + scope.o.otherNews.length);
                    };

                    var refreshNewsTotal = function () {
                        scope.o.newsTotal = scope.o.currentNews.length + scope.o.otherNews.length;
                    };

                    var isCurrentType = function (news) {
                        return !news || news.objectType === type;
                    };

                    if (!obj.news) {
                        obj.news = [];
                    }

                    var refreshCurrentNews = function() {
                        if (!obj.currentNews) {
                            obj.currentNews = _.filter(obj.news, function (news) {
                                return isCurrentType(news)
                            });
                        }
                    };

                    var refreshOtherNews = function() {
                        if (!obj.otherNews) {
                            obj.otherNews = _.filter(obj.news, function (news) {
                                return !isCurrentType(news)
                            });
                        }
                    };

                    refreshCurrentNews();
                    refreshOtherNews();


                    scope.o = obj;

                    refreshNewsTotal();

                    scope.removeNews = function (news) {
                        scope.o.currentNews = _.without(scope.o.currentNews, news);
                        refreshNewsTotal();
                        refreshNewsMin();
                    };


                    scope.$watch('object', function(object) {
                        obj = object;
                        refreshCurrentNews();
                        refreshOtherNews();
                    });

                    scope.$watch('o.newsTotal', function (total) {
                        var o = scope.o;
                        if (o) {
                            if (o.currentNews.length + o.otherNews.length < total) {
                                var newNews = [];
                                for (var i = 0; i < total - (o.currentNews.length + o.otherNews.length); i++) {
                                    newNews.push(createEmptyNews());
                                }
                                o.currentNews = o.currentNews.concat(newNews);
                                refreshNewsMin();
                            } else if (o.currentNews.length + o.otherNews.length > total) {
                                var n = o.currentNews;
                                for (var j = 0; j < o.currentNews.length + o.otherNews.length - total; j++) {
                                    n = _.difference(n, _.find(n, function (news) {
                                        return newsService.isNewsEmpty(news);
                                    }));
                                }
                                o.currentNews = n;
                                refreshNewsMin();
                            }
                        }
                    });

                    scope.$watchCollection('o.currentNews', function (news) {
                        refreshNewsMin();
                    });

                    scope.isEditable = function (news) {
                        return isCurrentType(news) || !news.objectType;
                    };

                    scope.addNews = function() {
                        scope.o.newsTotal++;
                    }
                }
            }
        };
    }];
});