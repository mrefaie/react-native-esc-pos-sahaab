package com.reactnativeescpossahaab

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.github.escposjava.print.Commands.*;

// @ref https://github.com/LeeryBit/esc-pos-android/blob/master/library/src/main/java/com/leerybit/escpos/Ticket.java
class LayoutBuilder {
    var charsOnLine = CHARS_ON_LINE_58_MM

    internal constructor() {}
    internal constructor(charsOnLine: Int) {
        this.charsOnLine = charsOnLine
    }

    @Throws(IOException::class)
    fun createFromDesign(text: String?): String {
        return createFromDesign(text, charsOnLine)
    }

    /**
     * DESIGN 1: Order List                       *
     * D0004 | Table #: A1 {C}           *
     * ------------------------------------------ *
     * [Dine In]                                  *
     * [ ] Espresso                               *
     * - No sugar, Regular 9oz, Hot           *
     * {R} x 1 *
     * ------------------------------------------ *
     * [ ] Blueberry Cheesecake                   *
     * - Slice                                *
     * {R} x 1 *
     * *
     * DESIGN 2: Menu Items                       *
     * ------------------------------------------ *
     * Item         {<>}       Qty  Price  Amount *
     * Pork Rice    {<>}         1  13.80   13.80 *
     */
    @Throws(IOException::class)
    fun createFromDesign(text: String?, charsOnLine: Int): String {
        val reader = BufferedReader(StringReader(text))
        val designText = StringBuilder()
        var line: String = ""
        var linex: String? = null
        while ({ linex = reader.readLine(); linex }() != null) {
            line = linex.toString();
            if (line.toString().matches(Regex("---.*"))) {
                designText.append(createDivider(charsOnLine))
            } else if (line.toString().matches(Regex("===.*"))) {
                designText.append(createDivider('=', charsOnLine))
            } else if (line.contains("{RP:")) {
                designText.append(duplicateStringSymbol(line))
            } else if (line.contains("{<>}")) {
                val splitLine: List<String> = line.split("\\{<>\\}")
                if(splitLine.size > 1){
                    designText.append(createMenuItem(splitLine[0], splitLine[1], ' ', charsOnLine))
                }
            } else {
                designText.append(line)
                designText.append("\n")
                // designText.append(createTextOnLine(line, ' ', TEXT_ALIGNMENT_LEFT, charsOnLine));
            }
        }
        return designText.toString()
    }

    fun createAccent(text: String, accent: Char): String {
        return createAccent(text, accent, charsOnLine)
    }

    fun createAccent(text: String, accent: Char, charsOnLine: Int): String {
        var accent = accent
        if (text.length - 4 > charsOnLine) {
            accent = ' '
        }
        return createTextOnLine(' ' + text + ' '.toInt(), accent, TEXT_ALIGNMENT_CENTER, charsOnLine)
    }

    fun createDivider(): String {
        return createDivider('-', charsOnLine)
    }

    fun createDivider(charsOnLine: Int): String {
        return createDivider('-', charsOnLine)
    }

    fun createDivider(symbol: Char, charsOnLine: Int): String {
        return StringUtils.repeat(symbol, charsOnLine).toString() + "\n"
    }

    fun createMenuItem(key: String, value: String, space: Char): String {
        return createMenuItem(key, value, space, charsOnLine)
    }

    fun createMenuItem(key: String, value: String, space: Char, charsOnLine: Int): String {
        return if (key.length + value.length + 2 > charsOnLine) {
            createTextOnLine("$key: $value", ' ', TEXT_ALIGNMENT_LEFT, charsOnLine)
        } else StringUtils.rightPad(key, charsOnLine - value.length, space) + value + "\n"
    }

    fun createTextOnLine(text: String, space: Char, alignment: String?): String {
        return createTextOnLine(text, space, alignment, charsOnLine)
    }

    fun createTextOnLine(text: String, space: Char, alignment: String?, charsOnLine: Int): String {
        if (text.length > charsOnLine) {
            val out = StringBuilder()
            val len: Int = text.length
            for (i in 0..len / charsOnLine) {
                val str: String = text.substring(i * charsOnLine, Math.min((i + 1) * charsOnLine, len))
                if (!str.trim().isEmpty()) {
                    out.append(createTextOnLine(str, space, alignment))
                }
            }
            return out.toString()
        }
        return when (alignment) {
            TEXT_ALIGNMENT_RIGHT -> StringUtils.leftPad(text, charsOnLine, space).toString() + "\n"
            TEXT_ALIGNMENT_CENTER -> StringUtils.center(text, charsOnLine, space).toString() + "\n"
            else -> StringUtils.rightPad(text, charsOnLine, space).toString() + "\n"
        }
    }

    fun duplicateStringSymbol(text: String): String {
        var text = text
        val repeatTag = "{RP:"
        val regex = "\\$repeatTag\\d+:.*?\\}"
        val m: Matcher = Pattern.compile(regex).matcher(text)
        var tagCount = 0
        while (m.find()) {
            tagCount++
        }
        for (x in 0 until tagCount) {
            var symbol = ""
            var count = "0"
            var repeatedSymbol = ""
            val rpIndex: Int = text.indexOf(repeatTag)
            val workingString: String = text.substring(rpIndex + repeatTag.length, text.indexOf('}'))
            val separatorIdx: Int = workingString.indexOf(':')
            count = workingString.substring(0, separatorIdx)
            symbol = workingString.substring(separatorIdx + 1, workingString.length)
            repeatedSymbol = StringUtils.repeat(symbol, Integer.parseInt(count))
            val replaceRepeatTag = "$repeatTag$workingString}"
            text = text.replaceFirst(Pattern.quote(text.substring(text.indexOf(replaceRepeatTag), text.indexOf(replaceRepeatTag) + replaceRepeatTag.length)), Matcher.quoteReplacement(repeatedSymbol))
        }
        return """
               $text
               
               """.trimIndent()
    }

    // public fun setCharsOnLine(charsOnLine : Int) {
    //     this.charsOnLine = charsOnLine;
    // }

    // public fun getCharsOnLine() : Int {
    //     return charsOnLine;
    // }

    companion object {
        const val TEXT_ALIGNMENT_LEFT = "LEFT"
        const val TEXT_ALIGNMENT_CENTER = "CENTER"
        const val TEXT_ALIGNMENT_RIGHT = "RIGHT"
        const val CHARS_ON_LINE_58_MM = 32
        const val CHARS_ON_LINE_76_MM = 42
        const val CHARS_ON_LINE_80_MM = 48
    }
}