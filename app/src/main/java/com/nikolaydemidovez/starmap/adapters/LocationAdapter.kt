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
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.pojo.Location
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class LocationAdapter (
    private var applicationContext: Context?,
    private var placesClient: PlacesClient,
    private var templateCanvas: TemplateCanvas,
    private var dialog: Dialog,
    dataList: ArrayList<Location>
) : BaseAdapter() {
    private var locationList: ArrayList<Location> = dataList

    override fun getCount(): Int {
        return locationList.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val view = LayoutInflater.from(applicationContext!!).inflate(R.layout.location_item, null)
        val location = view.findViewById<TextView>(R.id.location)
        val country  = view.findViewById<TextView>(R.id.country)


        location.text = locationList[i].location
        country.text  = locationList[i].country

        view.setOnClickListener {

            val placeFields = listOf(Place.Field.LAT_LNG)

            val request = FetchPlaceRequest.newInstance(locationList[i].placeId!!, placeFields)

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place

                    templateCanvas.eventLocation.value  = locationList[i].location
                    templateCanvas.eventCountry.value   = locationList[i].country
                    templateCanvas.eventLatitude.value  = place.latLng?.latitude
                    templateCanvas.eventLongitude.value = place.latLng?.longitude

                    dialog.dismiss()
                }
        }

        return view
    }

    fun clear() {
        locationList.clear()
    }
}