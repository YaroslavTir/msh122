define([
    'Angular',
    'Underscore',
    'Console',
    'Bootbox',
    'Messages',
    'Xeditable',
    'text!view/edit/schedule-template.html',
    'text!view/edit/schedule-media-content.html'
], function (angular, _, Console, Bootbox, messages, xeditable, scheduleTemplate, contentView) {
    return ['$compile', 'NewsService', 'MediaContentService', function ($compile, newsService, mediaContentService) {
        return {
            scope: {
                object: '=',
                type: '=',
                title:'@',
                types:'=',
                audioEnabled:'@',
                area:'@',
                error:'='
            },
            restrict: 'E',
            template: scheduleTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                var obj = scope.object;
                var type = scope.type;

                scope.hourValues = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
                scope.startMinuteValues = ['00', '05', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55'];
                scope.endMinuteValues = ['00', '05', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55', '59'];
                scope.errors=[];

                scope.$watch('object', function(newValue){
                    obj=newValue;
                });

                var allowedTypeNames=scope.types;
                allContentTypes = [
                    {
                        value: 'IMAGE',
                        text: messages['media.content.type.IMAGE'],
                        getUrl: function(c, cext) {
                                if(cext.fileUrl){
                                    return cext.fileUrl;
                                }else{
                                    return c.fileUrl;
                                }

                        }
                    },
                    {
                        value: 'VIDEO',
                        text: messages['media.content.type.VIDEO'],
                        getUrl: function(c, cext) {
                            return c.fileUrl;
                        }
                    }
                ];
                if(allowedTypeNames){
                    scope.contentTypes= _.filter(allContentTypes, function(type){return _.contains(allowedTypeNames, type.value)})
                }else{
                    scope.contentTypes=allContentTypes;
                }


                if (angular.isDefined(obj)) {
                    scope.addSchedule = function () {
                        scope.insertedSchedule = {
                            startDate: null,
                            endDate: null,
                            content: []
                        };
                        obj.schedule.push(scope.insertedSchedule);
                    };

                    scope.removeScheduleWithConfirmation=function(index){
                        Bootbox.dialog({
                            message: messages['schema.settings.schedule.delete.period'],
                            title: messages['default.confirm.delete.title'],
                            buttons: {
                                delete: {
                                    label: messages['default.button.remove'],
                                    callback: function() {
                                        scope.removeSchedule(index);
                                        scope.$apply();
                                    },
                                    className:"btn-primary"
                                },
                                cancel: {
                                    label: messages['default.button.cancel'],
                                    className:"btn-default"
                                }
                            }
                        });
                    }

                    scope.removeSchedule = function (index) {
                        obj.schedule.splice(index, 1);
                    };

                    scope.addContent = function (schedule, type) {
                        scope.inserted = {
                            startHour: '00',
                            startMin: '00',
                            endHour: '00',
                            endMin: '00',
                            isNew: true,
                            content: {
                                type: type,
                                size:'FULLSIZE',
                                infoText:'',
                                fileUrl:null,
                                audioUrl:null,
                                bgColor:'#FFFFFF'
                            },
                            contentExt:{

                            }
                        };
                        if (!schedule.content) {
                            schedule.content = [];
                        }
                        schedule.content.push(scope.inserted);
                        scope.inserted.__schedule = schedule;
                        scope.setContent(scope.inserted);
                    };

                    if (!obj.schedule) {
                        obj.schedule = [];
                    }

                    scope.o = obj;

                    scope.cancel=function(rowform, schedule, v, index){
                        rowform.$cancel();
                        if(v==scope.inserted && !v.content.type){
                            scope.removeContent(schedule, index);
                        }
                    }

                    scope.removeContentWithConfirmation=function(sch, index){
                        Bootbox.dialog({
                            message: messages['default.confirm.delete'],
                            title: messages['default.confirm.delete.title'],
                            buttons: {
                                delete: {
                                    label: messages['default.button.remove'],
                                    callback: function() {
                                        scope.removeContent(sch, index);
                                        scope.$apply();
                                    },
                                    className:"btn-primary"
                                },
                                cancel: {
                                    label: messages['default.button.cancel'],
                                    className:"btn-default"
                                }
                            }
                        });
                    }

                    scope.removeContent = function (schedule, index) {
                        schedule.content.splice(index, 1);
                    };

                    function getStartDate(start) {
                        if (start) {
                            return new Date(start);
                        } else {
                            return new Date(1500, 1, 1);
                        }
                    }

                    function getEndDate(end) {
                        if (end) {
                            return new Date(end);
                        } else {
                            return new Date(2500, 1, 1);
                        }
                    }

                    function setError(index, message){
                        scope.errors[index]=message;
                        if(message){
                            if(scope.error){
                                scope.error.error=true;
                            }
                        }else{
                            if(scope.error){
                                scope.error.error=false;
                            }
                        }
                    }

                    function checkScheduleSetErrors(schedule){
                        var i=0;
                        for(s in schedule){
                            scope.checkDate(schedule[s], i);
                            i++;
                        }
                    }

                    scope.checkDate = function (schedule, index) {
                        var curStartDate = getStartDate(schedule.startDate);
                        var curEndDate = getEndDate(schedule.endDate);
                        if (curStartDate && curEndDate && curStartDate > curEndDate) {
                            error = messages['schema.settings.schedule.error.hours'];
                        }
                        if (error) {
                            setError(index, error);
                            scope.$apply();
                            return;
                        }else{
                            setError(index, null);
                        }
                        for (key in obj.schedule) {
                            var s = obj.schedule[key]
                            if (s == schedule) {
                                continue
                            }
                            var startDate = getStartDate(s.startDate);
                            var endDate = getEndDate(s.endDate);

                            var error;
                            if (curStartDate <= startDate && curEndDate >= endDate) {
                                //error = messages['schema.settings.schedule.error.period.includes'];
                                error=messages['schema.settings.schedule.error.period.overlaps'];
                            }

                            if (curStartDate >= startDate && curEndDate <= endDate) {
                                //error = messages['schema.settings.schedule.error.period.included'];
                                error=messages['schema.settings.schedule.error.period.overlaps'];
                            }

                            if (startDate <= curStartDate && curStartDate <=endDate) {
                                //error = messages['schema.settings.schedule.error.startTime.in.period'];
                                error=messages['schema.settings.schedule.error.period.overlaps'];
                            }
                            if (startDate <= curEndDate && curEndDate <=endDate) {
                                //error = messages['schema.settings.schedule.error.endTime.in.period'];
                                error=messages['schema.settings.schedule.error.period.overlaps'];
                            }
                            if (error) {
                                setError(index, error);
                                scope.$apply();
                                return;
                            }
                        }
                        setError(index, null);
                        scope.$apply();
                    };

                    scope.checkTime = function (schedule, period) {
                        var curStartTime = new Date(2014, 1, 1, period.startHour, period.startMin);
                        var curEndTime = new Date(2014, 1, 1, period.endHour, period.endMin);
                        if (curStartTime >= curEndTime) {
                            return messages['schema.settings.schedule.error.hours'];
                        }
                        for (key in schedule.content) {
                            var s = schedule.content[key]
                            if (s == period) {
                                continue
                            }
                            var startTime = new Date(2014, 1, 1, s.startHour, s.startMin);
                            var endTime = new Date(2014, 1, 1, s.endHour, s.endMin);


                            if (curStartTime <= startTime && curEndTime >= endTime) {
                                return messages['schema.settings.schedule.error.period.includes'];
                            }

                            if (curStartTime >= startTime && curEndTime <= endTime) {
                                return messages['schema.settings.schedule.error.period.included'];
                            }

                            if(startTime<curStartTime && endTime>curStartTime){
                                return messages['schema.settings.schedule.error.startTime.in.period'];
                            }
                            if(startTime<curEndTime && endTime>curEndTime){
                                return messages['schema.settings.schedule.error.endTime.in.period'];
                            }
                        }
                    };

                    scope.printUrl = function (s) {
                        var ct = _.find(scope.contentTypes, function(v) {
                            return v.value === s.content.type;
                        });
                        if (!ct) {
                            return null;
                        }
                        var res = ct.getUrl(s.content, s.contentExt);
                        if (!res) {
                            return '';
                        }
                        return res;
                    };

                    scope.editContent = function (s) {
                        s.isNew = false;
                        scope.setContent(s);
                    }

                    scope.setContent = function (s) {
                        var $contentEditScope = scope.$new();
                        $contentEditScope.content = {
                            content: s.content,
                            contentExt: s.contentExt,
                            area:scope.area
                        };
                        $contentEditScope.imageType = attrs.imageType;
                        $contentEditScope.audioEnabled=attrs.audioEnabled;
                        Bootbox.dialog({
                            message: $compile(contentView)($contentEditScope),
                            title: messages['media.content.edit.dialog.title'],
                            className: 'content-edit-modal',
                            onEscape: function() {
                                if (s.isNew) {
                                    scope.removeContent(s.__schedule, s.__schedule.content.indexOf(s));
                                    scope.$apply();
                                }
                            },
                            buttons: {
                                cancel: {
                                    label: messages['default.button.cancel'],
                                    callback: function () {
                                        if (s.isNew) {
                                            scope.removeContent(s.__schedule, s.__schedule.content.indexOf(s));
                                            scope.$apply();
                                        }
                                    }
                                },
                                save: {
                                    label: messages['default.button.save'],
                                    callback: function () {
                                        scope.generateImageWithTextPreview($contentEditScope);
                                        var c = $contentEditScope.content.contentExt;
                                        s.contentExt = {
                                            id: c.id,
                                            type: c.type,
                                            generated: c.generated,
                                            fileUrl: c.fileUrl,
                                            audioUrl: c.audioUrl,
                                            updateDate: c.updateDate
                                        };
                                        scope.$apply()
                                    }
                                }
                            }
                        });
                    }
                }

                scope.allowedType = function(type) {
                    for (var i = 0; i < scope.contentTypes.length; i++) {
                        if (scope.contentTypes[i].value == type) {
                            return true
                        }
                    }
                    return false;
                }

                scope.generateImageWithTextPreview = function(scopeEdit) {
                    var content = scopeEdit.content.content;
                    scopeEdit.generateInProgress = true;
                    if(scopeEdit.content.contentExt.id){
                        mediaContentService.delete({
                            id: scopeEdit.content.contentExt.id
                        });
                    }
                    mediaContentService.save({}, {
                        content: {
                            type: content.type,
                            size:content.size,
                            infoText: content.infoText,
                            fileUrl: content.fileUrl,
                            bgColor: content.bgColor
                        },
                        area: scopeEdit.area
                    }, function (generated) {
                        scopeEdit.content.contentExt = generated;
                        scopeEdit.generateInProgress = false;
                    });
                };

                scope.checkPeriod = function (schedule, period, rowform) {
                    period.errorText = false;
                    var error = scope.checkTime(schedule, period);
                    if (error) {
                        //rowform.$setError('infoText', error);
                        period.errorText = error;
                        return error;
                    }
                    if (!period.content.infoText && !period.content.audioUrl && !period.content.fileUrl) {
                        period.errorText =   messages['schema.settings.schedule.error.no.content']
                        //rowform.$setError('infoText', messages['schema.settings.schedule.error.no.content']);
                        return messages['schema.settings.schedule.error.no.content'];
                    }
                };

                attrs.$observe('object', function (o) {

                })
            }
        };
    }];
});