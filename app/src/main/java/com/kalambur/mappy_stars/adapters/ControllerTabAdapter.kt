package com.kalambur.mappy_stars.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kalambur.mappy_stars.controllers.canvas_v1.CanvasV1ControllerFragment
import com.kalambur.mappy_stars.controllers.desc_v1.DescV1ControllerFragment
import com.kalambur.mappy_stars.controllers.location_v1.LocationV1ControllerFragment
import com.kalambur.mappy_stars.controllers.map_v1.MapV1ControllerFragment
import com.kalambur.mappy_stars.controllers.save_v1.SaveV1ControllerFragment
import com.kalambur.mappy_stars.controllers.separator_v1.SeparatorV1ControllerFragment
import com.kalambur.mappy_stars.controllers.stars_v1.StarsV1ControllerFragment
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.controllers.event_v1.EventV1ControllerFragment
import com.kalambur.mappy_stars.controllers.map_v2.MapV2ControllerFragment
import com.kalambur.mappy_stars.controllers.map_v2.MapV3ControllerFragment

class ControllerTabAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int, private var templateCanvas: TemplateCanvas) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment =
        when(templateCanvas.getControllerList()[position].name) {
            "event_v1" -> EventV1ControllerFragment(templateCanvas)
            "canvas_v1" -> CanvasV1ControllerFragment(templateCanvas)
            "map_v1" -> MapV1ControllerFragment(templateCanvas)
            "map_v2" -> MapV2ControllerFragment(templateCanvas)
            "map_v3" -> MapV3ControllerFragment(templateCanvas)
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