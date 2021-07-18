package com.nikolaydemidovez.starmap.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikolaydemidovez.starmap.controllers.canvas_v1.CanvasV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.desc_v1.DescV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.location_v1.LocationV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.map_v1.MapV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.save_v1.SaveV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.separator_v1.SeparatorV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.stars_v1.StarsV1ControllerFragment
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import controllers.event_v1.EventV1ControllerFragment

class ControllerTabAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int, private var templateCanvas: TemplateCanvas) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment =
        when(templateCanvas.getControllerList()[position].name) {
            "event_v1" -> EventV1ControllerFragment(templateCanvas)
            "canvas_v1" -> CanvasV1ControllerFragment(templateCanvas)
            "map_v1" -> MapV1ControllerFragment(templateCanvas)
            "stars_v1" -> StarsV1ControllerFragment(templateCanvas)
            "desc_v1" -> DescV1ControllerFragment(templateCanvas)
            "separator_v1" -> SeparatorV1ControllerFragment(templateCanvas)
            "location_v1" -> LocationV1ControllerFragment(templateCanvas)
            "save_v1" -> SaveV1ControllerFragment(templateCanvas)

            else -> EventV1ControllerFragment(templateCanvas)
        }


    override fun getItemCount(): Int {
        return numberOfTabs
    }
}