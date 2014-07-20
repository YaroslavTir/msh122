define([
        'Console',
        'Config',
        'Underscore',
        'jQuery',
        'Messages'],
    function (Console, Config, _, $, Messages) {
        return function ($rootScope, $resource, $http) {
            this.upload = function (files, mediaFileType, reloadCallback, errorCallback) {
                var fd = new FormData();
                //Take the first selected file
                fd.append("file", files[0]);
                fd.append("mediaFileType", mediaFileType);

                $http.post('/api/admin/v1/media', fd, {
                    withCredentials: true,
                    headers: {'Content-Type': undefined },
                    transformRequest: angular.identity
                }).success(function (data, status, headers, config) {
                    if (data.url) {
                        reloadCallback();
                        return data.url;
                    } else {
                        alert(Messages['file.upload.failed']);
                        return null;
                    }
                }).error(function (data, status, headers, config) {
                    errorCallback({
                        data: data,
                        status: status
                    });
                    return null;
                });

            };
        }
    });