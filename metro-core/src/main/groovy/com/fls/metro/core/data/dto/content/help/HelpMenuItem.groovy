package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.dto.content.UpdatableItem
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 14:33
 */
@EqualsAndHashCode
@ToString
abstract class HelpMenuItem extends UpdatableItem {
    String caption
    abstract HelpMenuItemDataType getType()
    abstract HelpMenuItemData getData()
    void setData() {
        throw new IllegalAccessException('Don\'t use it')
    }

    String parseUnicode(String text) {
        Pattern p = Pattern.compile("&#.*?;");
        Matcher m = p.matcher(text);
        while (m.find()) { // find next match
            String to = m.group();
            String t = to.substring(2, to.length() - 1);

            char[] chars = new char[1]
            chars[0] = (char) Integer.parseInt(t);
            text = text.replace(to, new String(chars))
        }

        text;
    }
}