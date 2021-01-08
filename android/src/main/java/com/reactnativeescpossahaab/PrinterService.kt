package com.reactnativeescpossahaab

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.github.escposjava.print.Printer;
import io.github.escposjava.print.exceptions.BarcodeSizeError;
import io.github.escposjava.print.exceptions.QRCodeException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.reactnativeescpossahaab.command.PrinterCommand;
import com.reactnativeescpossahaab.helpers.EscPosHelper;
import com.reactnativeescpossahaab.utils.BitMatrixUtils;
import io.github.escposjava.print.Commands.*;

class PrinterService {
    private val layoutBuilder: LayoutBuilder = LayoutBuilder()
    private val DEFAULT_QR_CODE_SIZE = 200
    private val DEFAULT_IMG_MAX_HEIGHT = 200
    private val DEFAULT_IMG_WIDTH_OFFSET = 0
    private val DEFAULT_BAR_CODE_HEIGHT = 120
    private val DEFAULT_BAR_CODE_WIDTH = 3
    private val DEFAULT_BAR_CODE_FORMAT = 73 //CODE-128
    private val DEFAULT_BAR_CODE_FONT = 0
    private val DEFAULT_BAR_CODE_POSITION = 2
    private var printingWidth = PRINTING_WIDTH_58_MM
    private var basePrinterService: io.github.escposjava.PrinterService
    private var context: ReactApplicationContext? = null
    private var charsetx: String = "UTF-8"
    private var codepage: Byte = 0x00

    constructor(printer: Printer) {
        basePrinterService = io.github.escposjava.PrinterService(printer)
    }

    constructor(printer: Printer, printingWidth: Int) {
        basePrinterService = io.github.escposjava.PrinterService(printer)
        this.printingWidth = printingWidth
    }

    fun cutPart() {
        basePrinterService.cutPart()
    }

    fun cutFull() {
        basePrinterService.cutFull()
    }

    @Throws(UnsupportedEncodingException::class)
    fun print(text: String) {
        // TODO: get rid of GBK default!
        write(text.toByteArray(charset(charsetx)))
    }

    @Throws(UnsupportedEncodingException::class)
    fun printLn(text: String) {
        print(text + CARRIAGE_RETURN)
    }

    fun lineBreak() {
        basePrinterService.lineBreak()
    }

    fun lineBreak(nbLine: Int) {
        basePrinterService.lineBreak(nbLine)
    }

    // We have to modify the printBarcode method in io.github.escposjava.PrinterService
    // Take a look on the toByteArray() function. It works incorrectly.
    @Throws(BarcodeSizeError::class)
    fun printBarcode(code: String, bc: String, width: Int, height: Int, pos: String, font: String) {
        basePrinterService.printBarcode(code, bc, width, height, pos, font)
    }

    fun printBarcode(str: String, nType: Int, nWidthX: Int, nHeight: Int, nHriFontType: Int, nHriFontPosition: Int) {
        val printerBarcode: ByteArray? = PrinterCommand.getBarCodeCommand(str, nType, nWidthX, nHeight, nHriFontType, nHriFontPosition, charsetx)
        basePrinterService.write(printerBarcode)
    }

    @Throws(IOException::class)
    fun printSample() {
        val design = """               ABC Inc. {C}               
           1234 Main Street {C}           
        Anytown, US 12345-6789 {C}        
            (555) 123-4567 {C}            
                                          
          D0004 | Table #: A1 {C}         
------------------------------------------
Item            {<>}    Qty  Price  Amount
Chicken Rice    {<>}      2  12.50   25.00
Coke Zero       {<>}      5   3.00   15.00
Fries           {<>}      3   3.00    9.00
Fresh Oyster    {<>}      1   8.00    8.00
Lobster Roll    {<>}      1  16.50   16.50
------------------------------------------
       {QR[Where are the aliens?]}        
"""
        printDesign(design)
    }

    @Throws(IOException::class)
    fun printDesign(text: String) {
        val baos: ByteArrayOutputStream = generateDesignByteArrayOutputStream(text)
        write(baos.toByteArray())
    }

    @Throws(IOException::class)
    fun readImage(filePath: String, reactContext: ReactApplicationContext?): Bitmap {
        val fileUri: Uri = Uri.parse(filePath)
        var image: Bitmap
        val op: BitmapFactory.Options = BitmapFactory.Options()
        op.inPreferredConfig = Bitmap.Config.ARGB_8888
        image = BitmapFactory.decodeFile(fileUri.getPath(), op)
        return image
    }

    @Throws(IOException::class)
    fun printImage(filePath: String) {
        printImage(readImage(filePath, context))
    }

    @Throws(IOException::class)
    fun printImage(filePath: String, widthOffset: Int) {
        printImage(readImage(filePath, context), widthOffset)
    }

    @Throws(IOException::class)
    fun printImage(image: Bitmap) {
        var image: Bitmap = image
        image = EscPosHelper.resizeImage(image, printingWidth - DEFAULT_IMG_WIDTH_OFFSET, DEFAULT_IMG_MAX_HEIGHT)
        val baos: ByteArrayOutputStream = generateImageByteArrayOutputStream(image)
        write(baos.toByteArray())
    }

    @Throws(IOException::class)
    fun printImage(image: Bitmap, widthOffset: Int) {
        var image: Bitmap = image
        image = EscPosHelper.resizeImage(image, Math.max(printingWidth - Math.abs(widthOffset), 0), DEFAULT_IMG_MAX_HEIGHT)
        val baos: ByteArrayOutputStream = generateImageByteArrayOutputStream(image)
        write(baos.toByteArray())
    }

    @Throws(QRCodeException::class)
    fun printQRCode(value: String, size: Int) {
        val baos: ByteArrayOutputStream = generateQRCodeByteArrayOutputStream(value, size)
        write(baos.toByteArray())
    }

    fun write(command: ByteArray?) {
        basePrinterService.write(command)
    }

    fun setCharCode(code: String) {
        charsetx = (code);
        // basePrinterService.setCharCode(code)
    }

    fun setCharCodePage(page: Int) {
        basePrinterService.write(byteArrayOf(0x1d,0x48, page.toByte()))
        codepage = page.toByte()
    }

    fun setCharsOnLine(charsOnLine: Int) {
        layoutBuilder.charsOnLine = charsOnLine
    }

    fun setPrintingWidth(printingWidth: Int) {
        this.printingWidth = printingWidth
    }

    fun setTextDensity(density: Int) {
        basePrinterService.setTextDensity(density)
    }

    fun beep() {
        basePrinterService.beep()
    }

    @Throws(IOException::class)
    fun open() {
        basePrinterService.open()
    }

    @Throws(IOException::class)
    fun close() {
        basePrinterService.close()
    }

    fun kickCashDrawerPin2() {
        basePrinterService.write(CD_KICK_2)
    }

    fun kickCashDrawerPin5() {
        basePrinterService.write(CD_KICK_5)
    }

    /**
     * DESIGN 1: Order List                       *
     * D0004 | Table #: A1 {C} {H1}      *
     * ------------------------------------------ *
     * [Dine In] {U} {B}                          *
     * [ ] Espresso {H2}                          *
     * - No sugar, Regular 9oz, Hot           *
     * {H3} {R} x 1 *
     * ------------------------------------------ *
     * [ ] Blueberry Cheesecake {H2}              *
     * - Slice                                *
     * {H3} {R} x 1 *
     * *
     * DESIGN 2: Menu Items                       *
     * ------------------------------------------ *
     * Item         {<>}       Qty  Price  Amount *
     * Pork Rice    {<>}         1  13.80   13.80 *
     * *
     * DESIGN 3: Barcode                          *
     * {QR[Love me, hate me.]} {C}                *
     * {BC[Your Barcode here]} {C}                *
     */
    @Throws(IOException::class)
    private fun generateDesignByteArrayOutputStream(text: String): ByteArrayOutputStream {
        val reader = BufferedReader(StringReader(text.trim()))
        val baos = ByteArrayOutputStream()
        var line: String
        var linex: String? = null
        while ({ linex = reader.readLine(); linex }() != null) {
            line = linex.toString()
            var qtToWrite: ByteArray? = null
            var imageToWrite: ByteArray? = null
            var bcToWrite: ByteArray? = null
            if (line.matches(Regex(".*\\{QR\\[(.+)\\]\\}.*"))) {
                qtToWrite = try {
                    generateQRCodeByteArrayOutputStream(line.replace(Regex(".*\\{QR\\[(.+)\\]\\}.*"), "$1"),
                            DEFAULT_QR_CODE_SIZE).toByteArray()
                } catch (e: QRCodeException) {
                    throw IOException(e)
                }
            }
            if (line.matches(Regex(".*\\{BC\\[(.+)\\]\\}.*"))) {
                bcToWrite = PrinterCommand.getBarCodeCommand(line.replace(Regex(".*\\{BC\\[(.+)\\]\\}.*"), "$1"), DEFAULT_BAR_CODE_FORMAT, DEFAULT_BAR_CODE_WIDTH, DEFAULT_BAR_CODE_HEIGHT, DEFAULT_BAR_CODE_FONT, DEFAULT_BAR_CODE_POSITION, charsetx)
            }
            val imgRegex = ".*\\{IMG\\[(.+)\\](?:\\}|:(\\d+)\\}).*"
            val imgPatter: Pattern = Pattern.compile(imgRegex)
            val imgMatcher: Matcher = imgPatter.matcher(line)
            if (imgMatcher.find()) {
                try {
                    var offset = DEFAULT_IMG_WIDTH_OFFSET
                    if (imgMatcher.group(2).length > 0) {
                        offset = Integer.parseInt(imgMatcher.group(1))
                    }
                    imageToWrite = generateImageByteArrayOutputStream(
                            EscPosHelper.resizeImage(
                                    readImage(line.replace(Regex(".*\\{IMG\\[(.+)\\]\\}.*"), "$1"), context),
                                    Math.max(printingWidth - Math.abs(offset), 0),
                                    DEFAULT_IMG_MAX_HEIGHT
                            )
                    ).toByteArray()
                } catch (e: IOException) {
                    throw IOException(e)
                }
            }
            val bold: Boolean = line.contains("{B}")
            val underline: Boolean = line.contains("{U}")
            val h1: Boolean = line.contains("{H1}")
            val h2: Boolean = line.contains("{H2}")
            val h3: Boolean = line.contains("{H3}")
            val lsm: Boolean = line.contains("{LS:M}")
            val lsl: Boolean = line.contains("{LS:L}")
            val ct: Boolean = line.contains("{C}")
            val rt: Boolean = line.contains("{R}")
            var charsOnLine: Int = layoutBuilder.charsOnLine

            // TODO: Shouldn't put it here
            val ESC_t = byteArrayOf(0x1b, 't'.toByte(), codepage)
            val ESC_M = byteArrayOf(0x1b, 'M'.toByte(), 0x00)
            val FS_and = byteArrayOf(0x1c, '&'.toByte())
            val TXT_NORMAL_NEW = byteArrayOf(0x1d, '!'.toByte(), 0x00)
            val TXT_4SQUARE_NEW = byteArrayOf(0x1d, '!'.toByte(), 0x11)
            val TXT_2HEIGHT_NEW = byteArrayOf(0x1d, '!'.toByte(), 0x01)
            val TXT_2WIDTH_NEW = byteArrayOf(0x1d, '!'.toByte(), 0x10)
            val LINE_SPACE_68 = byteArrayOf(0x1b, 0x33, 68)
            val LINE_SPACE_88 = byteArrayOf(0x1b, 0x33, 120)
            val DEFAULT_LINE_SPACE = byteArrayOf(0x1b, 50)
            baos.write(ESC_t)
            baos.write(FS_and)
            baos.write(ESC_M)

            // Add tags
            if (bold) {
                baos.write(TXT_BOLD_ON)
                line = line.replace("{B}", "")
            }
            if (underline) {
                baos.write(TXT_UNDERL_ON)
                line = line.replace("{U}", "")
            }
            if (h1) {
                baos.write(TXT_4SQUARE_NEW)
                baos.write(LINE_SPACE_88)
                line = line.replace("{H1}", "")
                charsOnLine = charsOnLine / 2
            } else if (h2) {
                baos.write(TXT_2HEIGHT_NEW)
                baos.write(LINE_SPACE_88)
                line = line.replace("{H2}", "")
            } else if (h3) {
                baos.write(TXT_2WIDTH_NEW)
                baos.write(LINE_SPACE_68)
                line = line.replace("{H3}", "")
                charsOnLine = charsOnLine / 2
            }
            if (lsm) {
                baos.write(LINE_SPACE_24)
                line = line.replace("{LS:M}", "")
            } else if (lsl) {
                baos.write(LINE_SPACE_30)
                line = line.replace("{LS:L}", "")
            }
            if (ct) {
                baos.write(TXT_ALIGN_CT)
                line = line.replace("{C}", "")
            }
            if (rt) {
                baos.write(TXT_ALIGN_RT)
                line = line.replace("{R}", "")
            }
            try {
                if (qtToWrite != null) {
                    baos.write(qtToWrite)
                }
                if (imageToWrite != null) {
                    baos.write(imageToWrite)
                }
                if (bcToWrite != null) {
                    baos.write(bcToWrite)
                }
                if (qtToWrite == null && imageToWrite == null && bcToWrite == null) {
                    // TODO: get rid of GBK default!
                    baos.write(layoutBuilder.createFromDesign(line, charsOnLine).toByteArray(charset(charsetx)))
                }
            } catch (e: UnsupportedEncodingException) {
                // Do nothing?
            }

            // Remove tags
            if (bold) {
                baos.write(TXT_BOLD_OFF)
            }
            if (underline) {
                baos.write(TXT_UNDERL_OFF)
            }
            if (h1 || h2 || h3) {
                baos.write(DEFAULT_LINE_SPACE)
                baos.write(TXT_NORMAL_NEW)
            }
            if (lsm || lsl) {
                baos.write(LINE_SPACE_24)
            }
            if (ct || rt) {
                baos.write(TXT_ALIGN_LT)
            }
        }
        return baos
    }

    @Throws(IOException::class)
    private fun generateImageByteArrayOutputStream(image: Bitmap): ByteArrayOutputStream {
        val baos = ByteArrayOutputStream()
        baos.write(LINE_SPACE_24)
        var y = 0
        while (y < image.getHeight()) {
            baos.write(SELECT_BIT_IMAGE_MODE) // bit mode
            // width, low & high
            baos.write(byteArrayOf((0x00ff and image.getWidth()).toByte(), (0xff00 and image.getWidth() shr 8).toByte()))
            for (x in 0 until image.getWidth()) {
                // For each vertical line/slice must collect 3 bytes (24 bytes)
                baos.write(EscPosHelper.collectImageSlice(y, x, image))
            }
            baos.write(CTL_LF)
            y += 24
        }
        return baos
    }

    @Throws(QRCodeException::class)
    private fun generateQRCodeByteArrayOutputStream(value: String, size: Int): ByteArrayOutputStream {
        return try {
            val result: BitMatrix = QRCodeWriter().encode(value, BarcodeFormat.QR_CODE, size, size, null)
            val qrcode: Bitmap = BitMatrixUtils.convertToBitmap(result)
            generateImageByteArrayOutputStream(qrcode)
        } catch (e: IllegalArgumentException) {
            // Unsupported format
            throw QRCodeException("QRCode generation error", e)
        } catch (e: WriterException) {
            throw QRCodeException("QRCode generation error", e)
        } catch (e: IOException) {
            throw QRCodeException("QRCode generation error", e)
        }
    }

    fun setContext(reactContext: ReactApplicationContext?) {
        context = reactContext
    }

    companion object {
        const val PRINTING_WIDTH_58_MM = 384
        const val PRINTING_WIDTH_76_MM = 450
        const val PRINTING_WIDTH_80_MM = 576
        private val CARRIAGE_RETURN: String = System.getProperty("line.separator").toString()
    }
}