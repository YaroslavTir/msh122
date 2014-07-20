define([
    'Angular',
    'Underscore',
    'Messages',
    'text!view/edit/station-plan-template.html'], function (angular, _, messages, stationPlanTemplate) {
    return ['$compile', function ($compile) {
        return {
            scope: {
                object: '=',
                type: '='
            },
            restrict: 'E',
            link: function (scope, element, attrs) {
                scope.messages = messages;
                scope.selectedPlanItem = null;
                var obj=scope.object.stationPlanSettings;
                var oldOwnerType=obj.ownerType;
                var oldOwnerId=obj.ownerId;

                scope.setOwn=function(){
                    if(obj.own){
                        obj.ownerType=scope.type;
                        obj.ownerId=scope.object.id;
                    }else{
                        obj.ownerType=oldOwnerType;
                        obj.ownerId=oldOwnerId;
                    }
                }

                scope.isPlanItemSelected = function(item) {
                    if (scope.selectedPlanItem && item) {
                        return scope.selectedPlanItem.type === item.type;
                    }
                    return false;
                };
                scope.selectPlanItem = function(item) {
                    scope.selectedPlanItem = item;
                };

                scope.moveUp=function(item){
                    var items=scope.object.stationPlanSettings.items;
                    if(items.length<=1){
                        return;
                    }
                    scope.selectedPlanItem=item;
                    var index = items.indexOf(item);
                    if (index > 0 && items[index-1].type!='INFO') {
                        items.splice(index, 1);
                        items.splice(index-1, 0, item);
                    }
                }

                scope.moveDown=function(item){
                    var items=scope.object.stationPlanSettings.items;
                    if(items.length<=1){
                        return;
                    }
                    scope.selectedPlanItem=item;
                    var index = items.indexOf(item);
                    if (index !=-1 && index< items.length-1) {
                        items.splice(index, 1);
                        items.splice(index+1, 0, item);
                    }
                }

                scope.$watch('object', function(newValue){
                    scope.object=newValue;
                    scope.selectedPlanItem = null;
                });

                element.append($compile(stationPlanTemplate)(scope));
            }
        }
    }];
});