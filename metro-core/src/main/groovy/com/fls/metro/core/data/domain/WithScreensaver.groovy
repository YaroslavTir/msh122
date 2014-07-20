package com.fls.metro.core.data.domain

import com.fls.metro.core.data.dto.screensaver.ScreensaverInfo

/**
 * User: NFadin
 * Date: 15.05.14
 * Time: 17:19
 */
public interface WithScreensaver {
    String getScreensaverUrl()
    ScreensaverInfo getScreensaverInfo()
    void setScreensaverInfo(ScreensaverInfo screensaverInfo)
}