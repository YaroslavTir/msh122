define([
        'Console',
        'jQuery',
        'Underscore',
        'Messages',
        'directives/ShowDataDirective',
        'directives/HierarchyInfoDirective',
        'directives/NewsDirective',
        'directives/EditDirective',
        'directives/HierarchyObjectSaveCancel',
        'directives/FileModel',
        'directives/ContentSettingsDirective',
        'directives/SosNumberDirective',
        'directives/ScheduleDirective',
        'directives/BannerSettingsDirective',
        'directives/MessageScheduleDirective',
        'directives/ServerValidationErrorDirective',
        'directives/MediaDirective',
        'directives/MediaContentDirective',
        'directives/DatePickerDirective',
        'directives/ContentListDirective',
        'directives/HelpInfoMediaListDirective',
        'directives/HelpInfoDirective',
        'directives/BuildInfoDirective',
        'directives/ImmediateMessagesDirective',
        'directives/SelectImageDirective',
        'directives/StationPlanDirective',
        'directives/initializators/TextAngular',
        'directives/DateValueFormatDirective',
        'directives/ScreensaverDirective'
    ], function (Console, $, _, messages, ShowDataDirective, HierarchyInfoDirective, NewsDirective, EditDirective, HierarchyObjectSaveCancel, FileModel, ContentSettings, SosNumber, Schedule, BannerSettings, MessageSchedule, ServerValidationErrorDirective, MediaDirective, MediaContentDirective, DatePicker, ContentList, InfoHelpMediaListDirective,InfoHelpDirective, BuildInfoDirective, ImmediateMessagesDirective, SelectImageDirective, StationPlanDirective, TextAngularInit, DateValueFormat, ScreensaverDirective) {
        "use strict";

        var directives = {
            showData: ShowDataDirective,
            hierarchyInfo: HierarchyInfoDirective,
            news: NewsDirective,
            saveCancelObject: HierarchyObjectSaveCancel,
            edit: EditDirective,
            serverValidationError: ServerValidationErrorDirective,
            fileModel: FileModel,
            contentSettings: ContentSettings,
            sosNumber: SosNumber,
            schedule: Schedule,
            bannerSettings: BannerSettings,
            messageSchedule: MessageSchedule,
            media: MediaDirective,
            mediaContent: MediaContentDirective,
            datePicker: DatePicker,
            contentList: ContentList,
            infoHelpMediaList: InfoHelpMediaListDirective,
            infoHelp: InfoHelpDirective,
            buildInfo: BuildInfoDirective,
            immediateMessages: ImmediateMessagesDirective,
            selectImage: SelectImageDirective,
            stationPlan: StationPlanDirective,
            dateValueFormat:DateValueFormat,
            screensaver: ScreensaverDirective
        };

        var initializators = [
            TextAngularInit
        ];

        return {
            initialize: function (module) {
                _.each(initializators, function(init) {
                    init.initialize(module);
                });
                _.each(directives, function (directive, name) {
                    module.directive(name, directive);
                });
            }
        };
    }
)
;
