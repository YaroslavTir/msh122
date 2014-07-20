define([
    'Config',
    'text!view/login/login.html',
    'text!view/schema/schema.html',
    'text!view/passenger-messages/passenger-messages.html',
    'text!view/im-statistic/im-statistic.html',
    'text!view/users/users.html',
    'text!view/im/registration/registration.html',
    'text!view/im/edit/edit.html',
    'text!view/lobby/edit/edit.html',
    'text!view/station/edit/edit.html',
    'text!view/line/edit/edit.html',
    'text!view/schema/edit/edit.html',
    'text!view/media/media.html',
    'text!view/error/internal-error.html'
], function (Config, loginView, schemaView, passengerMessageView, imStatisticView, usersView, registrationView, imEditView, lobbyEditView, stationEditView, lineEditView, schemaEditView, contentView, internalErrorView) {
    return {
        app: {
            route: '', controller: 'app', template: loginView
        },
        login: {
            route: '/login', controller: 'login', template: loginView
        },
        'internal-error': {
            route: '/internal-error', controller: 'internalError', template: internalErrorView
        },
        registration: {
            route: '/registration', controller: 'registration', template: registrationView, isAllowed: function ($routeScope) {
                return Config.testMode
            }
        },
        schema: {
            route: '/schema', controller: 'schema', template: schemaView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        passengerMessages: {
            route: '/passenger-messages', controller: 'passengerMessages', template: passengerMessageView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        imEdit: {
            route: '/schema/im/edit/:id', controller: 'imEdit', template: imEditView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        lobbyEdit: {
            route: '/schema/lobby/edit/:id', controller: 'lobbyEdit', template: lobbyEditView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        stationEdit: {
            route: '/schema/station/edit/:id', controller: 'stationEdit', template: stationEditView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        lineEdit: {
            route: '/schema/line/edit/:id', controller: 'lineEdit', template: lineEditView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        schemaEdit: {
            route: '/schema/schema/edit', controller: 'schemaEdit', template: schemaEditView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        imStatistic: {
            route: '/im-statistic', controller: 'imStatistic', template: imStatisticView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        content: {
            route: '/content', controller: 'content', template: contentView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        },
        users: {
            route: '/users', controller: 'users', template: usersView, isAllowed: function ($routeScope) {
                return $routeScope.hasRole('ADMIN')
            }
        }
    }
});