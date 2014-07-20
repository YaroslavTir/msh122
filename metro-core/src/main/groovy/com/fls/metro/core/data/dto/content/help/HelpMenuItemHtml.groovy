package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.domain.HelpInfo
import com.fls.metro.core.data.dto.content.HtmlDataFileToLinkMapper
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 15:56
 */
@EqualsAndHashCode
@ToString
class HelpMenuItemHtml extends HelpMenuItem {

    HelpMenuItemDataHtml data

    public HelpMenuItemHtml(HelpInfo i) {
        data = new HelpMenuItemDataHtml(
                title: i.title,
                images: parseHtmlDataFileToLink(i),
                text: parseUnicode(i.htmlText));

        caption = i.name
        updateDate = i.updateDate
    }

    private List<HtmlDataFileToLinkMapper> parseHtmlDataFileToLink(HelpInfo helpInfo) {
        List<HtmlDataFileToLinkMapper> result = new ArrayList<HtmlDataFileToLinkMapper>()

        Pattern p = Pattern.compile("src=.*?>");
        Matcher m = p.matcher(helpInfo.htmlText);
        String fileName = "";
        while (m.find()) { // find next match
            String url = m.group();
            if (url.endsWith("/>")) {
                url = url.substring(url.indexOf("http"), url.length() - 3);
            } else {
                url = url.substring(url.indexOf("http"), url.length() - 2);
            }
            fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            result.add(new HtmlDataFileToLinkMapper(
                    file: fileName,
                    link: url
            ))
        }

        for (HtmlDataFileToLinkMapper l : result) {
            helpInfo.htmlText = helpInfo.htmlText.replace(l.link, l.file)
        }

        result
    }

    @Override
    HelpMenuItemDataType getType() {
        return HelpMenuItemDataType.HTML
    }

    @Override
    HelpMenuItemDataHtml getData() {
        return data
    }
}
