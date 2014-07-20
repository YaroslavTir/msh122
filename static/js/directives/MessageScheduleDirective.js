define([
    'Angular',
    'Bootbox',
    'Underscore',
    'Messages',
    'text!view/edit/message-schedule-template.html'
], function (angular, Bootbox, _, messages, messageScheduleTemplate) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                ownerType:'=',
                owner:'=',
                object: '=',
                error:'='
            },
            restrict: 'E',
            template: messageScheduleTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;
                var oldOwnerType=scope.object.ownerType;
                var oldOwnerId=scope.object.ownerId;


                if (angular.isDefined(obj)) {
                    scope.setOwn=function(){
                        if(obj.own){
                            obj.ownerType=scope.ownerType;
                            obj.ownerId=scope.owner.id;
                        }else{
                            obj.ownerType=oldOwnerType;
                            obj.ownerId=oldOwnerId;
                        }
                    }
                }
            }
        };
    }];
});