define(
    'EditControllerUtils',
    [],
    function () {
        return {
            prepareScheduleForSave: function (schedule) {
                for (var sch in schedule) {
                    var content = [];
                    for (var s in schedule[sch].content) {
                        var c = schedule[sch].content[s];
                        c.startTime = c.startHour + ":" + c.startMin;
                        c.endTime = c.endHour + ":" + c.endMin;
                        var newContent = {
                            startTime: c.startHour + ":" + c.startMin,
                            endTime: c.endHour + ":" + c.endMin,
                            content: c.content,
                            contentExt: c.contentExt
                        };
                        content.push(newContent);
                    }
                    schedule[sch].content = content;
                }
                return schedule;
            },

            prepareScheduleForEditing: function (schedule) {
                for (sch in schedule) {
                    var current=schedule[sch];
                    for (s in current.content) {
                        var c = current.content[s];
                        var arr = c.startTime.split(':');
                        c.startHour = arr[0];
                        c.startMin = arr[1];

                        arr = c.endTime.split(':');

                        c.endHour = arr[0];
                        c.endMin = arr[1];


                    }
                }
                return schedule;
            },

            prepareBannerSettingsForEditing: function (bs) {
                if (!bs) return bs;
                bs.schedule = this.prepareScheduleForEditing(bs.schedule);
                return bs;
            },

            prepareBannerSettingsForSave: function (bs) {
                if (!bs) return bs;
                bs.schedule = this.prepareScheduleForSave(bs.schedule);
                return bs;
            },

            prepareMessageScheduleForEditing: function (bs) {
                if (!bs) return bs;
                bs.schedule = this.prepareScheduleForEditing(bs.schedule);
                return bs;
            },

            prepareMessageScheduleForSave: function (bs) {
                if (!bs) return bs;
                bs.schedule = this.prepareScheduleForSave(bs.schedule);
                return bs;
            },

            createEditErrors:function(){
                return {
                    bannerError:{
                        error:false
                    },
                    messageError:{
                        error:false
                    },
                    helpError:{
                        error:false
                    }
                };
            },

            hasError:function(errors){
                if(!errors){
                    return false;
                }
                return errors.bannerError.error || errors.messageError.error || errors.helpError.error;
            },

            getEditErrorMessage:function(errors, messages){
                var msg=messages['edit.error.message'];
                var needComma=false;
                if(errors.bannerError.error){
                    msg=msg+messages['edit.error.banner']
                    needComma=true;
                }
                if(errors.messageError.error){
                    if(needComma){
                        msg=msg+', ';
                    }
                    msg=msg+messages['edit.error.message.schedule']
                    needComma=true;
                }
                if(errors.helpError.error){
                    if(needComma){
                        msg=msg+', ';
                    }
                    msg=msg+messages['edit.error.message.help']
                    needComma=true;
                }
                return msg;
            }
        }
    }
);
