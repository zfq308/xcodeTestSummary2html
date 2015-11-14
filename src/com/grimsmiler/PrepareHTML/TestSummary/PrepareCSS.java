package com.grimsmiler.PrepareHTML.TestSummary;

import j2html.tags.Tag;

import static j2html.TagCreator.tag;

/**
 * Created by ilya on 14/11/15.
 */
class PrepareCSS {

    static Tag getStyleTag() {
        String style = getSpan() + getTable() + getImg();
        Tag styleTag = tag("style").withText(style);
        return styleTag;
    }

    private static String getSpan() {
        return "span.passedTest {\n" +
                "        color: green;\n" +
                "        font-family: Verdana;\n" +
                "        font-weight: 10;\n" +
                "    }\n" +
                "    span.failedTest {\n" +
                "        color: red;\n" +
                "        font-family: Verdana;\n" +
                "        font-weight: 10;\n" +
                "    }\n";
    }

    private static String getTable() {
        return "table {\n" +
                "        border: 0;\n" +
                "        cellpadding: 1;\n" +
                "        cellspacing: 1;\n" +
                "    }\n";
    }

    private static String getImg() {
        return "img {\n" +
                "        width: auto;\n" +
                "        max-height: 40%;\n" +
                "    }\n";
    }
}
