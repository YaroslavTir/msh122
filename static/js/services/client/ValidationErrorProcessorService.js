define(['Console', 'Config'], function (Console, Config) {
    return function () {
        this.process = function (error, cb) {
            if (error.status === Config['validation.error.code']) {
                cb(error.data.errors);
            }
        };
    }
});