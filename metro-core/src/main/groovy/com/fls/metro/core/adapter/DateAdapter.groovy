package com.fls.metro.core.adapter

import com.fls.metro.core.util.Constants

import javax.xml.bind.annotation.adapters.XmlAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * User: NFadin
 * Date: 24.04.14
 * Time: 10:56
 */
class DateAdapter extends XmlAdapter<String, Date> {


    @Override
    Date unmarshal(String v) throws Exception {
        return new SimpleDateFormat(Constants.FULL_DATE_FORMAT).parse(v)
    }

    @Override
    String marshal(Date v) throws Exception {
        return new SimpleDateFormat(Constants.FULL_DATE_FORMAT).format(v)
    }
}
