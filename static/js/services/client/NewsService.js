define([
    'Console',
    'Config',
    'Underscore',
    'jQuery'],
    function (Console, Config, _, $) {
        return function ($rootScope, $resource) {

            var isNewsEmpty = function (news) {
                return !news.id && !news.title && !news.text
            };

            this.isNewsEmpty = isNewsEmpty;

            this.preparedNews = function(news) {
//                return _.filter(news, function(n) {
//                    return !isNewsEmpty(n)
//                });
                return news;
            };
        }
    });