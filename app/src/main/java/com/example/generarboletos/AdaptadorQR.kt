package com.example.generarboletos

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class AdaptadorQR (private val contex: Activity,
                   private val arraList: ArrayList<CodigoQR>):
    ArrayAdapter<CodigoQR>(contex, R.layout.item,arraList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(contex)
        val view: View = inflater.inflate(R.layout.item, null)

        view.findViewById<TextView>(R.id.FolioQR).text = arraList[position].folio
        view.findViewById<TextView>(R.id.EstatusQR).text = arraList[position].estado

        if(arraList[position].estado == "GENERADO"){
            view.findViewById<ImageView>(R.id.imagenQR).setImageDrawable(ContextCompat.getDrawable(contex, R.drawable.qrdisp))
        }
        else if(arraList[position].estado == "USADO"){
            view.findViewById<ImageView>(R.id.imagenQR).setImageDrawable(ContextCompat.getDrawable(contex, R.drawable.qrusado))
        }
        else if(arraList[position].estado == "NO GENERADO"){
            view.findViewById<ImageView>(R.id.imagenQR).setImageDrawable(ContextCompat.getDrawable(contex, R.drawable.qrnogen))
        }
        else {
            view.findViewById<ImageView>(R.id.imagenQR).setImageDrawable(ContextCompat.getDrawable(contex, R.drawable.qrerror))
        }

        return view
    }
}