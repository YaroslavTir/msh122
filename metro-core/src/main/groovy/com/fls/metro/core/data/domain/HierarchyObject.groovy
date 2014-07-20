package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.data.dto.screensaver.ScreensaverInfo
import com.fls.metro.core.validation.annotation.NewsTitleNotEmpty

/**
 * User: NFadin
 * Date: 07.05.14
 * Time: 14:19
 */
@NewsTitleNotEmpty(message = '{hierarchy.object.one.news.title.should.not.be.empty}')
abstract class HierarchyObject implements WithName, WithId, WithNews, WithScreensaver, WithStaticDataUpdateDate, WithParent, WithBannerSettings, WithMessageSchedule {
    @Ignore List<News> news
    @Ignore List<HelpInfo> helpInfos
    String screensaverUrl
    @Ignore ScreensaverInfo screensaverInfo = new ScreensaverInfo()
    @TimestampField Date staticDataUpdateDate = new Date();
    @Ignore Map<ObjectType, HierarchyObject> hierarchyInfo
    @Ignore BannerSettings bannerSettings
    @Ignore MessageSchedule messageSchedule
    @TimestampField Date newsUpdateDate = new Date();
    @TimestampField Date updateDate = new Date();
}