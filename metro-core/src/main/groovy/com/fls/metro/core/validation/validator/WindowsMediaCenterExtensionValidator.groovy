package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.data.domain.MediaFileType
import com.fls.metro.core.util.FileUtils
import com.fls.metro.core.validation.annotation.WindowsMediaCenterExtension
import org.springframework.beans.factory.annotation.Value

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 06.06.2014
 * Time: 16:31
 */
class WindowsMediaCenterExtensionValidator implements ConstraintValidator<WindowsMediaCenterExtension, MediaFile> {

    private List<String> audioExtensions
    private List<String> videoExtensions


    @Override
    void initialize(WindowsMediaCenterExtension constraintAnnotation) {
    }

    @Override
    boolean isValid(MediaFile value, ConstraintValidatorContext context) {
        switch (value.mediaType) {
            case MediaFileType.AUDIO: return audioExtensions.contains(FileUtils.getExtension(value.name)?.toUpperCase())
            case MediaFileType.VIDEO: return videoExtensions.contains(FileUtils.getExtension(value.name)?.toUpperCase())
            default: return true
        }
    }

    @Value('${media.extensions.audio}')
    void setAudioExtensions(String audioExtension) {
        this.audioExtensions = audioExtension.split(',').collect {
            it.trim()
        }
    }

    @Value('${media.extensions.video}')
    void setVideoExtensions(String videoExtensions) {
        this.videoExtensions = videoExtensions.split(',').collect {
            it.trim()
        }
    }
}
