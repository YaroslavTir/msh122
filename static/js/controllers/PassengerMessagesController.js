define([
    'Console',
    'Messages',
    'jQuery',
    'AngularGridDoubleClick',
    'Bootbox',
    'Messages',
    'text!view/passenger-messages/view-dialog.html'
], function(Console, messages, $, AngularGridDoubleClick, Bootbox, Messages, viewDialogTemplate) {
    return ['$scope', '$compile', 'PassengerMessageService', function($scope, $compile, passengerMessageService) {

        $scope.filter = {
            imName: ''
        };

        $scope.pagingOptions = {
            pageSizes: [14],
            pageSize: 14,
            currentPage: 1
        };

        var setData = function(data) {
            $scope.pmData = data.data;
            $scope.total = data.total;
        };

        var getData = function(filter, pagingOptions) {
            passengerMessageService.get({
                imName: filter.imName,
                createDate: filter.createDate,
                currentPage: pagingOptions.currentPage,
                pageSize: pagingOptions.pageSize
            }, function(data) {
                setData(data)
            });
        };

        $scope.doFilter = function () {
            $scope.pagingOptions.currentPage = 1;
            getData($scope.filter, $scope.pagingOptions)
        };

        $scope.doClean = function () {
            $scope.filter.imName = '';
            $scope.pagingOptions.currentPage = 1;
            $scope.doFilter();
        };

        $scope.$watch('pagingOptions', function (newVal, oldVal) {
            if (newVal !== oldVal && newVal.currentPage && newVal.currentPage !== oldVal.currentPage) {
                getData($scope.filter, $scope.pagingOptions);
            }
        }, true);

        $scope.gridOptions = {
            data: 'pmData',
            enablePaging: true,
            showFooter: true,
            totalServerItems: 'total',
            pagingOptions: $scope.pagingOptions,
            filterOptions: $scope.filter,
            i18n: 'ru',
            enableSorting: false,
            multiSelect: false,
            dblClickFn: function(pm) {
                var $dialogScope = $scope.$new();
                $dialogScope.messageRows = pm.message.split('\n');
                Bootbox.dialog({
                    message: $compile(viewDialogTemplate)($dialogScope),
                    title: Messages['passenger.message.dialog.view.title'],
                    buttons: {
                        ok: {
                            label: Messages['default.button.ok']
                        }
                    }
                });
                $dialogScope.$apply(); // dunno why...
            },
            plugins: [AngularGridDoubleClick],
            columnDefs: [
                { field: 'imName', displayName: messages['passenger.message.grid.column.title.im.name'] },
                { field: 'name', displayName: messages['passenger.message.grid.column.title.name'] },
                { field: 'contact', displayName: messages['passenger.message.grid.column.title.contact'] },
                {
                    field: 'createDate',
                    displayName: messages['passenger.message.grid.column.title.createDate'],
                    cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{row.entity.createDate | date: \'yyyy-MM-dd HH:mm:ss\'}}</span></div>'
                }
            ]
        };

        $scope.doFilter();
    }];
});