package com.reactnativeescpossahaab

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class LayoutBuilderModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val reactContext: ReactApplicationContext
    private val layoutBuilder: LayoutBuilder = LayoutBuilder()

    public override fun getConstants() : MutableMap<String, Any> {
            val constants: MutableMap<String, Any> = HashMap()
            constants.put(LayoutBuilder.TEXT_ALIGNMENT_LEFT, LayoutBuilder.TEXT_ALIGNMENT_LEFT)
            constants.put(LayoutBuilder.TEXT_ALIGNMENT_CENTER, LayoutBuilder.TEXT_ALIGNMENT_CENTER)
            constants.put(LayoutBuilder.TEXT_ALIGNMENT_RIGHT, LayoutBuilder.TEXT_ALIGNMENT_RIGHT)
            return constants
    }

    public override fun getName() : String {
        return "LayoutBuilder"
    }

    @ReactMethod
    fun createAccent(text: String, accent: String, promise: Promise) {
        promise.resolve(layoutBuilder.createAccent(text, accent.get(0)))
    }

    @ReactMethod
    @Throws(IOException::class)
    fun createFromDesign(text: String, promise: Promise) {
        try {
            promise.resolve(layoutBuilder.createFromDesign(text))
        } catch (e: IOException) {
            promise.reject(e)
        }
    }

    @ReactMethod
    fun createDivider(promise: Promise) {
        promise.resolve(layoutBuilder.createDivider())
    }

    @ReactMethod
    fun createDivider(symbol: String, promise: Promise) {
        promise.resolve(layoutBuilder.createDivider(symbol.get(0).toInt()))
    }

    @ReactMethod
    fun createMenuItem(key: String, value: String, space: String, promise: Promise) {
        promise.resolve(layoutBuilder.createMenuItem(key, value, space.get(0)))
    }

    @ReactMethod
    fun createTextOnLine(text: String, space: String, alignment: String, promise: Promise) {
        promise.resolve(layoutBuilder.createTextOnLine(text, space.get(0), alignment))
    }

    @ReactMethod
    fun setPrintingSize(printingSize: String) {
        val charsOnLine: Int
        charsOnLine = when (printingSize) {
            EscPosSahaabModule.PRINTING_SIZE_80_MM -> LayoutBuilder.CHARS_ON_LINE_80_MM
            EscPosSahaabModule.PRINTING_SIZE_76_MM -> LayoutBuilder.CHARS_ON_LINE_76_MM
            EscPosSahaabModule.PRINTING_SIZE_58_MM -> LayoutBuilder.CHARS_ON_LINE_58_MM
            else -> LayoutBuilder.CHARS_ON_LINE_58_MM
        }
        layoutBuilder.charsOnLine = charsOnLine
    }

    init {
        this.reactContext = reactContext
    }
}