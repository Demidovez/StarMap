package com.kalambur.mappy_stars.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.pojo.Location
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.extensions.dismissWithAds

class LocationAdapter (
    private var activity: Activity?,
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
        val view = LayoutInflater.from(activity!!).inflate(R.layout.location_item, null)
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
                    templateCanvas.coordinates.value  = arrayListOf(place.latLng!!.latitude.toFloat(), place.latLng!!.longitude.toFloat())

                    dialog.dismissWithAds(activity!!)
                }
        }

        return view
    }

    fun clear() {
        locationList.clear()
    }
}