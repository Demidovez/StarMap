package com.nikolaydemidovez.starmap.adapters

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.ResourcesCompat.getFont
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.pojo.Lang
import com.nikolaydemidovez.starmap.pojo.Location
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class LangAdapter (
    private var applicationContext: Context?,
    private var templateCanvas: TemplateCanvas,
    private var dialog: Dialog,
    private var listener: (lang: Lang) -> Unit
) : BaseAdapter() {
    private var langList: ArrayList<Lang> = templateCanvas.langList

    override fun getCount(): Int {
        return langList.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val view = LayoutInflater.from(applicationContext!!).inflate(R.layout.lang_item, null)
        val lang = view.findViewById<TextView>(R.id.lang)

        lang.text = langList[i].label

        view.setOnClickListener {
            listener(langList[i])

            dialog.dismiss()
        }

        return view
    }
}