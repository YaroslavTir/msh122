package com.fls.metro.core.data.domain

import com.fls.metro.core.data.dto.schedule.Schedule

/**
 * User: NFadin
 * Date: 08.05.14
 * Time: 13:06
 */
public interface WithSchedule {
    List<Schedule> getSchedule()
    void setSchedule(List<Schedule> schedule)
}