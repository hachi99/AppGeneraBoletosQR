package com.example.generarboletos

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class NuevoQR : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevoqr)

        val estado = findViewById<TextView>(R.id.Estatus)
        val folio = findViewById<TextView>(R.id.FolioBoleto)
        val imageViewQR = findViewById<ImageView>(R.id.QR)
        val imagen = findViewById<ImageView>(R.id.EstatusIcon)

        val parametros = intent.extras
        val folioValue = parametros?.getString("folio") ?: ""

        // Actualizar el TextView con el folio
        folio.text = folioValue

        // Generar y mostrar el código QR basado en el folio
        val qrBitmap = generateQRCode(folioValue, 500, 500)
        imageViewQR.setImageBitmap(qrBitmap)

        // Actualizar el estado de la imagen según el estado recibido
        estado.text = parametros?.getString("estado")
        when (estado.text) {
            "GENERADO" -> imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.qrdisp))
            "NO GENERADO" -> imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.qrnogen))
            "USADO" -> imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.qrusado))
            else -> imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.qrerror))
        }
    }

    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }

        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
}
