define([
    'Angular',
    'Bootbox',
    'Messages',
    'text!view/tools/text-angular/image-picker-template.html'
], function (angular, Bootbox, messages, imagePickerTemplate) {
    return {
        initialize: function (module) {
            module.config(function ($provide) {
                $provide.decorator('taOptions', ['taRegisterTool', '$delegate', '$compile', function (taRegisterTool, taOptions, $compile) {
                    taOptions.toolbar = [
                        ["h1", "h2", "h3", "h4", "h5", "h6", "p"],
                        ["bold", "italics", "underline", "ul", "ol", "redo", "undo"],
                        taOptions.toolbar[2],
                        ['html'],
                        ['imagePicker', 'imageView', 'imageInsert', 'fontColorPicker', 'fontColor', 'fontNamePicker', 'fontNameView', 'fontName']
                    ];

                    // custom toolbar
                    taOptions.toolbar.push([]);
                    var customTbIndex = taOptions.toolbar.length - 1;

                    // image
                    taRegisterTool('imagePicker', {
                        iconclass: 'fa fa-picture-o',
                        action: function() {
                            var $scope = this;
                            var $imagePickerScope = $scope.$new(true);
                            $imagePickerScope.messages = messages;
                            $imagePickerScope.selectImage = function(mf) {
                                $scope.$parent.currentImage = mf;
                            };
                            Bootbox.dialog({
                                message: $compile(imagePickerTemplate)($imagePickerScope),
                                title: messages['tools.text.angular.media.file.picker.dialog.title'],
                                buttons: {
                                    ok: {
                                        label: messages['default.button.ok']
                                    }
                                }
                            });
                        }
                    });
                    taRegisterTool('imageView', {
                        display: '<button class="btn btn-default" tabindex="-1" name="imageView">' +
                            messages['tools.text.angular.media.file.picker.label'] + ': {{$parent.currentImage.name}}</button>',
                        action: function () {
                            var $scope = this;
                            if ($scope.$parent.currentImage) {
                                window.open($scope.$parent.currentImage.url, '_blank')
                            }
                        }
                    });
                    taRegisterTool('imageInsert', {
                        iconclass: 'fa fa-bolt',
                        action: function() {
                            var $scope = this;
                            if ($scope.$parent.currentImage) {
                                return this.$editor().wrapSelection('insertImage', $scope.$parent.currentImage.url);
                            }
                        }
                    });

                    // font color
                    taRegisterTool('fontColorPicker', {
                        display: '<input style="height: 34px; width: 44px;" type="color" ng-model="$parent.currentFontColor" name="fontColorPicker">'
                    });
                    taRegisterTool('fontColor', {
                        iconclass: 'fa fa-magic',
                        action: function (defered) {
                            var $scope = this;
                            if ($scope.$parent.currentFontColor) {
                                $scope.$editor().wrapSelection('forecolor', $scope.$parent.currentFontColor)
                            }
                        }
                    });

                    // fontName
                    taRegisterTool('fontNamePicker', {
//                        display: '<div class="btn-group dropdown"><button type="button" class="btn btn-default">123</button>' +
//                            '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>' +
//                            '<ul class="dropdown-menu" role="menu"><li ng-repeat="o in options">{{o.name}}</li></ul></div>',
                        display: '<div class="btn-group dropdown" style="width: 40px; padding:0px;">' +
                            '<div style="height: 32px;" class="dropdown-toggle" data-toggle="dropdown"><span class="caret" style="margin-top: 10px;"></span></div>' +
                            '<ul class="dropdown-menu" role="menu"><li ng-repeat="o in options"><a ng-click="setCurrentFontName(o)" style="font-family: {{o.css}}">{{o.name}}</a></li></ul></div>',
                        setCurrentFontName: function(o) {
                            if (o) {
                                var $scope = this;
                                $scope.$parent.$parent.currentFont = o;
                            }
                        },
                        options: [
                            {name: 'Arial', css: 'Arial'},
                            {name: 'Comic Sans MS', css: "'comic sans ms', sans-serif"},
                            {name: 'Courier New', css: "'courier new', monospace"},
                            {name: 'Times New Roman', css: 'Times New Roman'},
                            {name: 'Sans-Serif', css: 'Arial, Helvetica, sans-serif'},
                            {name: 'Serif', css: "'times new roman', serif"},
                            {name: 'Wide', css: "'arial black', sans-serif"},
                            {name: 'Narrow', css: "'arial narrow', sans-serif"},
                            {name: 'Garamond', css: 'garamond, serif'},
                            {name: 'Georgia', css: 'georgia, serif'},
                            {name: 'Tahoma', css: 'tahoma, sans-serif'},
                            {name: 'Trebuchet MS', css: "'trebuchet ms', sans-serif"},
                            {name: 'Helvetica', css: "'Helvetica Neue', Helvetica, Arial, sans-serif"},
                            {name: 'Verdana', css: 'verdana, sans-serif'},
                            {name: 'Proxima Nova', css: 'proxima_nova_rgregular'}
                        ]
                    });
                    taRegisterTool('fontNameView', {
                        display: '<button class="btn btn-default" tabindex="-1" name="fontNameView">' +
                            messages['tools.text.angular.font.name.picker.label'] + ': <font style="font-family: {{$parent.currentFont.css}}">{{$parent.currentFont.name}}</font></button>'
                    });
                    taRegisterTool('fontName', {
                        iconclass: 'fa fa-font',
                        action: function() {
                            var $scope = this;
                            if ($scope.$parent.currentFont) {
                                return this.$editor().wrapSelection('fontName', $scope.$parent.currentFont.css);
                            }
                        }
                    });


//                    // font size
//                    taRegisterTool('fontSizePicker', {
//                        display: '<div>{{$parent.currentFontSize || 1}}</div>',
//                        action: function () {
//                            var $scope = this;
//                            var size = prompt(messages['tools.text.angular.color.picker.label']);
//                            if (!_.isNaN(size = parseInt(size))) {
//                                $scope.$parent.currentFontSize = size;
//                            }
//                            return true;
//                        }
//                    });
//                    taOptions.toolbar[customTbIndex].push('fontSizePicker');
//                    taRegisterTool('fontSize', {
//                        iconclass: 'fa fa-text-height',
//                        action: function () {
//                            var $scope = this;
//                            return this.$editor().wrapSelection("fontSize", $scope.$parent.currentFontSize || 1);
//                        }
//                    });
//                    taOptions.toolbar[customTbIndex].push('fontSize');
                    return taOptions;
                }]);
            });
        }
    }
});