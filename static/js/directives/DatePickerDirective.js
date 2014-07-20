define([
    'Angular',
    'Messages',
    'Underscore',
    'Config'
], function (angular, messages, _, config) {
    return function ($parse) {
        return {
            restrict: 'A',
            require: 'ngModel',
            scope:{
                callback: '&onchange'
            },
            link: function(scope, element, attr, ngModel) {
                $(element).datepicker({format:'dd.mm.yyyy'})
                    .on('changeDate', function(e){
                        if(!e){
                            return
                        }
                        ngModel.$setViewValue(e.date);
                        //scope.$apply();
                        if(scope.callback){
                            scope.callback()
                        }
                    });
            }
        }
    };
});