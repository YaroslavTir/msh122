define([
    'Bootbox',
    'Messages',
    'Config',
    'Angular',
    'Underscore',
    'Messages',
    'jQuery',
    'text!view/edit/help-info-template.html'], function(Bootbox, Messages, Config, angular, _, messages,$, infoHelpTemplate) {
    return ['$compile', '$timeout', 'NewsService', function ($compile, $timeout, newsService) {
        return {
            scope: {
                object: '=',
                type: '=',
                error:'='
            },
            restrict: 'E',
            template: infoHelpTemplate,
            link: function (scope, element, attrs) {
                scope.messages = messages;
                scope.currentHtmlText='';
                var obj = scope.object;
                var type = scope.type;
                obj.language = 'RU';
                var errorsRu=[];
                var errorsEn=[];

                var createNewEmptyInfo = function () {
                    return {
                        title: "",
                        objectType: type,
                        htmlText: "",
                        name: "",
                        language: obj.language,
                        isSelected: false,
                        video:[],
                        audio:[]
                    }
                }

                var findFirstHelpInfo = function (helpInfos, language) {
                    for (var i = 0; i < helpInfos.length; i++) {
                        var help = helpInfos[i];
                        if (help.language == language) {
                            return help;
                        }
                    }
                }

                var findLastHelpInfo = function (helpInfos, language) {
                    if (helpInfos.length < 1) {
                        return null;
                    }
                    for (var i = helpInfos.length - 1; i >= 0; i--) {
                        var help = helpInfos[i];
                        if (help.language == language) {
                            return help;
                        }
                    }
                }

                function checkHelpInfoValid(hi, index){
                    if(!hi){
                        return
                    }
                    if(!hi.name || !hi.title || !hi.helpInfoType){
                        if(hi.language=='RU'){
                            errorsRu[index]=true;
                        }else{
                            errorsEn[index]=true;
                        }
                    }else{
                        if(hi.language=='RU'){
                            errorsRu[index]=false;
                        }else{
                            errorsEn[index]=false;
                        }
                    }
                    checkErrors();
                }

                function eraseError(hi, index){
                    if(hi.language=='RU'){
                        errorsRu.splice(index, 1)
                    }else{
                        errorsEn.splice(index, 1)
                    }
                    checkErrors();
                }

                function checkErrors(){
                    for(i in errorsRu){
                        if(errorsRu[i]){
                            scope.error.error=true;
                            return;
                        }
                    }
                    for(i in errorsEn){
                        if(errorsEn[i]){
                            scope.error.error=true;
                            return;
                        }
                    }
                    scope.error.error=false;
                }

                scope.isValueEmpty=function(helpInfo, field){
                    if(!helpInfo){
                        return
                    }
                    checkHelpInfoValid(helpInfo, obj.selectedIndex)
                    if (!angular.isDefined(helpInfo)) {
                        return false
                    }
                    if(!helpInfo[field]){
                        return true;
                    }else{
                        return false;
                    }
                }
                scope.reload=function(){
                    obj = scope.object;
                    var type = scope.type;
                    if (angular.isDefined(scope.o) && angular.isDefined(scope.o.language)) {
                        obj.language = scope.o.language;
                    } else {
                        obj.language = 'RU';
                    }
                    if (angular.isDefined(obj)) {

                        if (!obj.helpInfos) {
                            obj.helpInfos = [];
                        }

                        if (obj.helpInfos.length > 0) {
                            selectHelp(true);
                        }
                        scope.o = obj;
                    }
                }

                function selectHelp(first){
                    var help = first ?
                        findFirstHelpInfo(obj.helpInfos, obj.language) :
                        findLastHelpInfo(obj.helpInfos, obj.language);
                    if(help){
                        help.isSelected = true;
                        obj.selectedIndex = obj.helpInfos.indexOf(help);
                        scope.currentHtmlText=help.htmlText;
                    }else{
                        obj.selectedIndex=undefined;
                        scope.currentHtmlText='';
                    }
                    $("#htmlTextDisplay").html(scope.currentHtmlText);
                }

                scope.reload();

                var isCurrentType = function (help) {
                    if(help){
                        return help.objectType === type;
                    }
                };

                scope.addNewHelpInfo = function () {
                    var help = createNewEmptyInfo();
                    scope.o.helpInfos.push(help);
                    scope.setSelection(help);
                }

                scope.moveUp=function(helpInfo){
                    scope.setSelection(helpInfo);
                    var index = scope.o.helpInfos.indexOf(helpInfo);
                    if (index > 0) {
                        if(scope.isEditable(scope.o.helpInfos[index-1])){
                            scope.o.helpInfos.splice(index, 1);
                            scope.o.helpInfos.splice(index-1, 0, helpInfo);
                        }
                    }
                }

                scope.moveDown=function(helpInfo){
                    scope.setSelection(helpInfo);
                    var index = scope.o.helpInfos.indexOf(helpInfo);
                    if (index !=-1 && index< scope.o.helpInfos.length-1) {
                        if(scope.isEditable(scope.o.helpInfos[index+1])){
                            scope.o.helpInfos.splice(index, 1);
                            scope.o.helpInfos.splice(index+1, 0, helpInfo);
                        }
                    }
                }
                scope.removeCurrentHelpInfo = function () {
                    Bootbox.dialog({
                        message: Messages['default.object.remove.dialog.msg'],
                        title: Messages['default.object.remove.dialog.title'],
                        buttons: {
                            cancel: {
                                label: Messages['default.button.cancel'],
                                callback: function() {
                                    return false;
                                }
                            },
                            remove: {
                                label: Messages['default.button.remove'],
                                callback: function() {
                                    if(scope.hasSelection()){
                                        eraseError(scope.o.helpInfos[scope.o.selectedIndex], scope.o.selectedIndex);
                                        scope.o.helpInfos.splice(scope.o.selectedIndex, 1);
                                    }
                                    if(scope.o.helpInfos.length==0){
                                        scope.o.selectedIndex=undefined;
                                        scope.currentHtmlText='';
                                        $("#htmlTextDisplay").html(scope.currentHtmlText);
                                    }else{
                                        selectHelp(false)
                                    }
                                    scope.$apply();
                                    return true;
                                }
                            }
                        }
                    });
                }

                scope.setSelection = function (helpInfo) {
                    var newSelectionIndex = scope.o.helpInfos.indexOf(helpInfo);
                    if (angular.isDefined(scope.o.selectedIndex)){
                        scope.o.helpInfos[scope.o.selectedIndex].isSelected = false;
                        scope.o.helpInfos[scope.o.selectedIndex].htmlText=scope.currentHtmlText;
                    }
                    helpInfo.isSelected = true;
                    scope.o.selectedIndex = newSelectionIndex;
                    scope.o.helpInfos[scope.o.selectedIndex].isSelected = true;
                    scope.currentHtmlText=scope.o.helpInfos[scope.o.selectedIndex].htmlText;
                    $("#htmlTextDisplay").html(scope.currentHtmlText);

                }

                scope.$watch('currentHtmlText', function(newValue){
                    if(scope.hasSelection() && scope.o.helpInfos){
                        scope.o.helpInfos[scope.o.selectedIndex].htmlText=newValue;
                    }
                });

                scope.$watch('object', function(newValue){
                    scope.reload();
                });

                scope.hasSelection=function(){
                    return (angular.isDefined(scope.o.selectedIndex));
                }

                scope.isEditable = function (help) {
                    if(help){
                        return isCurrentType(help) || !help.objectType;
                    }
                };

                scope.isLanguage = function (language) {
                    return scope.o.language == language;
                };

                scope.setLanguage = function (language) {
                    if (angular.isDefined(scope.o.selectedIndex)){
                        scope.o.helpInfos[scope.o.selectedIndex].isSelected = false;
                        scope.o.helpInfos[scope.o.selectedIndex].htmlText=scope.currentHtmlText;
                    }
                    scope.o.language = language;
                    selectHelp(true);
                };

                $timeout(function () {
                    var $taTextElement = $(element).find('#taTextElement');
                    var $taHtmlElement = $(element).find('#taHtmlElement');
                    $taTextElement.css("font-family", Config['info.help']['editor.font']);
                    $taTextElement.css("min-height", "400px");
                    $taHtmlElement.attr('rows', 20);
                });
            }
        };
    }];
});