define([
    'Angular',
    'Underscore',
    'Messages',
    'Bootbox',
    'jQuery',
    'text!view/media/media-template.html',
    'text!view/media/media-upload-dialog-template.html',
    'text!view/tools/angular-grid/compact-footer.html'
], function (angular, _, Messages, Bootbox, $, contentTemplate, uploadDialogTemplate, compactFooter) {
    return ['$compile', '$timeout', 'MediaService', 'UploadService', 'ValidationErrorProcessorService', function ($compile, $timeout, mediaService, uploadService, validationErrorProcessorService) {
        return {
            scope: {
                disableDelete: '=',
                disableUpload: '=',
                disableHeader: '=',
                enableSelection: '=',
                selectedUrl: '=',
                uploadModal: '@',
                showFilter: '@',
                selectionCallback: '&',
                cleanCallback: '&'
            },
            restrict: 'E',
            link: function (scope, element, attrs) {
                if (scope.selectedUrl) {
                    scope.selected = scope.selectedUrl;
                }
                scope.messages = Messages;
                scope.mediaGridClass = attrs.gridClass ? attrs.gridClass : 'media-grid';
                scope.mediaFileTypeFilter = attrs.mediaFileTypeFilter;
                scope.mediaFileType = 'IMAGE_BANNER';
                if (attrs.mediaFileTypeFilter) {
                    scope.mediaFileType = attrs.mediaFileTypeFilter;
                }

                scope.uploadButtonId = 'media_file_upload_button_' + scope.$id; // pff why not...

                scope.filter = {
                    mediaType: scope.mediaFileTypeFilter
                };

                scope.pagingOptions = {
                    pageSizes: attrs.compact ? [5] : [8],
                    pageSize: attrs.compact ? 5 : 8,
                    currentPage: 1
                };

                scope.cleanSelection=function(){
                    if(scope.cleanCallback){
                        scope.cleanCallback()
                        var idx=findSelected();
                        if(idx!==-1){
                            scope.gridOptions.selectRow(idx, false);
                            scope.selected = null
                        }
                    }
                }

                function findSelected(){
                    var idx = _.indexOf(scope.mediaData, _.find(scope.mediaData, function (it) {
                        return it.url === scope.selected;
                    }));
                    return idx;
                }

                var setData = function (media) {
                    if (!media) {
                        return;
                    }
                    scope.mediaData = media.data;
                    scope.total = media.total;
                    if (scope.selected && scope.enableSelection) {
                        var idx=findSelected();
                        if (idx !== -1) {
                            $timeout(function () {
                                scope.gridOptions.selectRow(idx, true);
                            });
                        }
                    }
                };

                var getData = function (filter, pagingOptions) {
                    mediaService.get({
                        mediaType: filter.mediaType,
                        currentPage: pagingOptions.currentPage,
                        pageSize: pagingOptions.pageSize
                    }, function (media) {
                        setData(media)
                    });
                };

                scope.doFilter = function () {
                    scope.pagingOptions.currentPage = 1;
                    getData(scope.filter, scope.pagingOptions)
                };

                scope.doClean = function() {
                    scope.filter.mediaType = '';
                    scope.pagingOptions.currentPage = 1;
                    scope.doFilter();
                };

                scope.$watch('pagingOptions', function (newVal, oldVal) {
                    if (newVal !== oldVal && newVal.currentPage && newVal.currentPage !== oldVal.currentPage) {
                        getData(scope.filter, scope.pagingOptions);
                    }
                }, true);

                scope.messageTypeText = function (mediaType) {
                    return Messages['media.type.' + mediaType]
                };

                var columnsToShow = attrs.columns ?
                    _.map(attrs.columns.split(','), function (v) {
                        return v.trim();
                    }) : undefined;

                var isShowColumn = function (col) {
                    if (columnsToShow) {
                        return _.contains(columnsToShow, col);
                    }
                    return true;
                };

                var columnDefs = function () {
                    var result = [];
                    if (!scope.disableDelete) {
                        result.push({
                            width: '30px',
                            cellTemplate: '<div style="width: 35px;"><span class="glyphicon glyphicon-trash" ng-click="deleteMF(row.entity)"></span></div>'
                        });
                        scope.deleteMF = function (mf) {
                            Bootbox.dialog({
                                message: Messages['media.remove.dialog.message'],
                                title: Messages['media.remove.dialog.title'],
                                buttons: {
                                    cancel: {
                                        label: Messages['media.remove.dialog.button.cancel']
                                    },
                                    ok: {
                                        label: Messages['media.remove.dialog.button.ok'],
                                        callback: function () {
                                            mediaService.delete({mediaId: mf.id}, {}, function () {
                                                scope.doFilter()
                                            });
                                        }
                                    }
                                }
                            });
                        };
                    }
                    if (isShowColumn('name')) {
                        result.push({
                            field: 'name',
                            displayName: Messages['media.table.header.name']
                        })
                    }
                    if (isShowColumn('createdDate')) {
                        result.push({
                            field: 'createdDate',
                            displayName: Messages['media.table.header.created.date'],
                            cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.entity.createdDate | date: \'yyyy-MM-dd HH:mm:ss\'}}</span></div>'
                        })
                    }
                    if (isShowColumn('mediaType')) {
                        result.push({
                            field: 'mediaType',
                            displayName: Messages['media.table.header.type'],
                            cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{messageTypeText(row.entity.mediaType)}}</span></div>'
                        })
                    }
                    if (isShowColumn('preview')) {
                        result.push({
                            field: 'preview',
                            displayName: Messages['media.table.header.preview'],
                            cellTemplate: '<img src="{{row.entity.thumbUrl}}" height="50px"/>',
                            width: '100px'
                        })
                    }
                    if (isShowColumn('url')) {
                        result.push({
                            field: 'url',
                            displayName: Messages['media.table.header.file'],
                            cellTemplate: '<a href="{{row.entity.url}}" target="_blank">' + Messages['media.table.download'] + '</a>',
                            width: '100px'
                        });
                    }
                    return result;
                };

                scope.gridOptions = {
                    enableRowSelection: !!scope.enableSelection,
                    headerRowHeight: scope.disableHeader ? 0 : 31,
                    rowHeight: 60,
                    data: 'mediaData',
                    enablePaging: true,
                    showFooter: true,
                    totalServerItems: 'total',
                    pagingOptions: scope.pagingOptions,
                    filterOptions: scope.filter,
                    i18n: 'ru',
                    enableSorting: false,
                    multiSelect: false,
                    columnDefs: columnDefs(),
                    footerTemplate: attrs.compact ? compactFooter : undefined,
                    afterSelectionChange: function (rowItem, event) {
                        if (scope.enableSelection) {
                            if(!rowItem.selected){
                                return;
                            }
                            var mf = rowItem.entity;
                            scope.selected = mf.url;
                            if (mf) {
                                scope.selectionCallback({mediaFile: mf});
                            }
                        }
                    }
                };

                scope.uploadInProgress = false;

                var createUpload = function(scope) {
                    scope.upload = function (files, mediaFileType, cb) {
                        scope.uploadInProgress = true;
                        uploadService.upload(files, mediaFileType, function () {
                            scope.errors = [];
                            scope.doFilter();
                            scope.uploadInProgress = false;
                            if (_.isFunction(cb)) {
                                cb()
                            }
                        }, function (error) {
                            validationErrorProcessorService.process(error, function (errors) {
                                scope.errors = errors;
                                scope.uploadInProgress = false;
                            });
                            if (_.isFunction(cb)) {
                                cb(true)
                            }
                        });
                    };
                };

                var createShowFileInfo = function(scope) {
                    scope.showFileInfo = function () {
                        return scope.files && scope.files.length > 0;
                    };
                };

                if (!scope.disableUpload) {
                    if (scope.uploadModal) {
                        scope.openUploadDialog = function() {
                            var $uploadDialogScope = scope.$new();
                            $uploadDialogScope.pickFile = function () {
                                $('#media_file_upload_button_dialog').trigger('click');
                            };
                            createUpload($uploadDialogScope);
                            createShowFileInfo($uploadDialogScope);
                            var d = Bootbox.dialog({
                                message: $compile(uploadDialogTemplate)($uploadDialogScope),
                                title: Messages['media.upload.dialog.title'],
                                className: 'big-modal',
                                buttons: {
                                    cancel: {
                                        label: Messages['default.button.cancel']
                                    },
                                    ok: {
                                        label: Messages['media.button.upload'],
                                        callback: function () {
                                            $uploadDialogScope.upload($uploadDialogScope.files, $uploadDialogScope.mediaFileType, function(error) {
                                                if (!error) {
                                                    d.modal('hide');
                                                    scope.doFilter();
                                                }
                                            });
                                            return false;
                                        }
                                    }
                                }
                            });
                            $('#media_file_upload_button_dialog').on('change', function () {
                                $uploadDialogScope.files = this.files;

                                $uploadDialogScope.$apply();
                            });
                        }
                    } else {
                        scope.pickFile = function () {
                            $('#' + scope.uploadButtonId).trigger('click');
                            $('#' + scope.uploadButtonId).on('change', function () {
                                scope.upload(this.files, scope.mediaFileType);
                            });

                        };
                        createUpload(scope);
                        createShowFileInfo(scope);
                    }

                }

                element.append($compile(contentTemplate)(scope));

                $(element).find('.upload-button').attr('id', scope.uploadButtonId);

                $(element).find('#' + scope.uploadButtonId).on('change', function () {
                    scope.files = this.files;
                    scope.$apply();
                });

                scope.doFilter();
            }
        };
    }];
});