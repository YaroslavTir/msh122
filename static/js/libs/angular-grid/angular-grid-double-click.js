define(function () {
    return function ngGridDoubleClick() {
        var self = this;
        self.$scope = null;
        self.myGrid = null;
        self.init = function(scope, grid, services) {
            self.$scope = scope;
            self.myGrid = grid;
            self.assignEvents();
        };
        self.assignEvents = function() {
            self.myGrid.$viewport.on('dblclick', self.onDoubleClick);
        };
        self.onDoubleClick = function(event) {
            self.myGrid.config.dblClickFn(self.$scope.selectedItems[0]);
        };
    }
});
