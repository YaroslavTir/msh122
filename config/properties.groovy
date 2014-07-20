db {
    url = 'jdbc:postgresql://localhost:5432/metro'
    username = 'postgres'
    password = '123456'
}
server {
    port = 8080
    host = 'localhost'
    staticCfg {
        addStatic = false
        resourcePath = '.'
    }
}
common {
    admin {
        name = 'admin'
        password = 'admin'
    }
    usernameSessionAttr = 'username'
    imRole = 'IM'
    validateImToken = false
    realIpHeader = ''
    lineDefaultColor = '#d42a2a'
}
images{

    pathToStore='/home/metro/content/images/'
    baseUrl='http://91.225.130.11/images/'
    thumbwidth=100

    pathToStoreGenerated='/home/metro/content/images/generated/'
    baseUrlGenerated='http://91.225.130.11/images/generated/'
}

schedule{
    thresholdSec=60
}

content {
    news {
        url {
            ru = 'http://static.feed.rbc.ru/rbc/internal/rss.rbc.ru/rbc.ru/mainnews.rss'
            en = 'http://static.feed.rbc.ru/rbc/internal/rss.rbc.ru/rbc.ru/engfpnews_rss.rss'
        }
        delay = 600 // seconds
    }
    weather {
        url = 'http://export.yandex.ru/weather-ng/forecasts/27612.xml'
        cron = '0 0 * * * *'
    }
    exchangeRate {
        url = 'http://www.cbr.ru/scripts/XML_daily.asp'
        cron = '0 0 7,12,17 * * ?'
        currencies = 'USD, EUR'
    }
    schedule {
        cron = '0 0/5 * * * *'
    }
}

media {
    extensions {
        audio = '.ASX, .WM, .WMA, .WMX, .WAV, .MP3, .M3U, .AAC'
        video = '.WM, .WMV, .ASF, .M2TS, .M2T, .MOV, .QT, .AVI, .WTV, .DVR-MS, .MP4, ' +
                '.MOV, .M4V, .MPEG, .MPG, .MPE, .M1V, .MP2, .MPV2, .MOD, .VOB, .M1V, .AVI, .MOV'
    }
}

im {
    defaultPort = 8080
    resource {
        status {
            name = 'status'
            method = 'GET'
        }
        notification {
            name = 'update'
            method = 'POST'
        }
    }
    notification {
        delay = '600000' // milliseconds
    }
}