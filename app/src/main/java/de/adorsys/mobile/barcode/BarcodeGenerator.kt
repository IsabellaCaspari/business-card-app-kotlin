package de.adorsys.mobile.barcode

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


object BarcodeGenerator {

    fun generateBarcode(content: String, format: BarcodeFormat, width: Int, height: Int) = try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap =
            barcodeEncoder.encodeBitmap(content, format, width, height)
        bitmap
    } catch (e: Exception) {
        null
    }
}

