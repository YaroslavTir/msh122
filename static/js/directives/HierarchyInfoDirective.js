define(['Angular', 'Messages', 'Underscore', 'Config'], function (angular, messages, _, config) {
    return function ($compile) {
        return {
            scope: {
                object: '=',
                objectType: '=',
                page:'@'
            },
            restrict: 'E',
            template: "<div/>",
            link: function (scope, element, attrs) {
                var schemaLinkTemplate = '<a href="/schema/schema/edit#<%=page%>">' + messages['schema.title'] + '</a>';
                var schemaPlainTemplate = messages['schema.title'];
                var linkTemplate = '<a href="/schema/<%=type%>/edit/<%=o.id%>#<%=page%>"><%=o.name%></a>';
                var plainTemplate = '<%=o.name%>';
                var createItem = function(pair, withLink) {
                    var r = '';
                    if (attrs.displayType !== undefined && pair[0] !== 'SCHEMA') {
                        r += messages['hierarchy.object.name.' + pair[0].toLowerCase()] + ': ';
                    }
                    var tmpl = withLink ?
                        (pair[0] === 'SCHEMA' ? schemaLinkTemplate : linkTemplate) :
                        (pair[0] === 'SCHEMA' ? schemaPlainTemplate : plainTemplate);
                    return r + _.template(tmpl, {
                        type: pair[0].toLowerCase(),
                        o: pair[1],
                        page:scope.page
                    });
                };
                function reload(){
                    var obj = scope.object;
                    var endType = scope.objectType;
                    if (angular.isDefined(obj)) {
                        var h = obj.hierarchyInfo;
                        var pairs = _.pairs(h);
                        var info = [];
                        for (var i = 0; i < pairs.length; i++) {
                            var pair = pairs[i];
                            if (!_.contains(config.excludeHierarchyObjects, pair[0])) {
                                if(!attrs.lastLink){
                                    info.push(createItem(pair, true))
                                }else{
                                    if(endType === pair[0]){
                                        info.push(createItem(pair, true))
                                    }else{
                                        info.push(createItem(pair, false))
                                    }
                                }
                            }
                            if (endType === pair[0]) {
                                break;
                            }
                        }
                        if (attrs.displayObjectName !== undefined) {
                            info.push(createItem([scope.objectType, scope.object], false));
                        }
                        var hierarchyInfoDisplay = info.join(attrs.delimiter ? attrs.delimiter + ' ' : ' ');
                        element.html("");
                        element.append((attrs.prefix ? attrs.prefix + ': ' : '') + hierarchyInfoDisplay)
                    }
                }
                scope.$watch('object', function () {
                    reload();
                });

                scope.$watch('objectType', function () {
                    reload();
                });

            }
        };
    };
});