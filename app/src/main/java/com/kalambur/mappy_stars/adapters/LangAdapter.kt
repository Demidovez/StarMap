package com.kalambur.mappy_stars.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.pojo.Lang
import com.kalambur.mappy_stars.templates.TemplateCanvas

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