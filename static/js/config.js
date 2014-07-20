define(function () {
    return {
        setTest: function () {
            this.testMode = true
        },
        apiBaseUrl: '/api/admin/v1/',
        imApiBaseUrl: '/api/im/v1/',
        roles: [
            {
                value: 'ADMIN',
                name: 'Администратор'
            },
            {
                value: 'USER',
                name: 'Пользователь'
            }
        ],
        excludeHierarchyObjects: [],
        'validation.error.code': 400,
        testMode: false,
        'google.maps': {
            defaults: {
                zoom: 11,
                center: {
                    latitude: '55.75382375323252',
                    longitude: '37.620797753334045'
                }
            },
            minZoom: 9,
            bounds: {
                northeast: {
                    latitude: '56.275386383661846',
                    longitude: '38.65814208984375'
                },
                southwest: {
                    latitude: '55.084655921502',
                    longitude: '36.7108154296875'
                }
            }
        },
        'screensaver.maxWidth': 1080,
        'screensaver.maxHeight': 1070,
        'info.help': {
            'editor.font': 'Times New Roman'
        }
    };
});
