package com.fls.metro.core.build

import groovy.util.logging.Slf4j

/**
 * User: NFadin
 * Date: 22.05.14
 * Time: 10:51
 */
@Slf4j
class BuildInfoUtils {
    private static final Properties buildProperties = new Properties()

    static {
        try {
            buildProperties.load(BuildInfoUtils.getResourceAsStream('/build_info.properties'));
        } catch (IOException e) {
            log.error('Can\'t load build props', e);
        }
    }

    public static String getBuildNumber() {
        getBuildProperty('build.number')
    }

    public static String getSvnRevisionNumber() {
        getBuildProperty('svn.revision.number')
    }

    public static String getSvnCommittedDate() {
        getBuildProperty('svn.committedDate')
    }

    public static String getProfile() {
        getBuildProperty('profile')
    }

    public static String getVersion() {
        getBuildProperty('version')
    }

    public static BuildInfo getBuildInfo() {
        return new BuildInfo(
                buildNumber: buildNumber,
                svnRevisionNumber: svnRevisionNumber,
                svnCommittedDate: svnCommittedDate,
                profile: profile,
                version: version
        )
    }

    private static String getBuildProperty(String key) {
        try {
            return (String) buildProperties.get(key);
        } catch (Exception e) {
            log.warn('Can\'t read build_info', e);
            return 'unknown';
        }
    }
}
