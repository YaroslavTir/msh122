define([
    'Angular',
    'Messages',
    'Underscore',
    'Config'
], function (angular, messages, _, config) {
    return function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                changeCallback=attrs.onchange
                if(changeCallback){
                    $(element).timepicker({minuteStep:5, showMeridian:false}).on('changeTime.timepicker', function(e){
                        changeCallback(e)
                    });
                }else{
                    $(element).timepicker({minuteStep:5, showMeridian:false});
                }
            }
        }
    };
});