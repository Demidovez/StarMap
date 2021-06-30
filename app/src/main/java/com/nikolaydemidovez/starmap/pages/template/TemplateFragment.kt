package com.nikolaydemidovez.starmap.pages.template

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import com.nikolaydemidovez.starmap.templates.TemplateView
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateView
import com.nikolaydemidovez.starmap.templates.half_v1.HalfV1TemplateView


class TemplateFragment : Fragment() {

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var adapter: ControllerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val templateName = arguments?.getString("templateName")
        val templateTitle = arguments?.getString("templateTitle")

        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = templateTitle
        }

        templateViewModel = ViewModelProviders
            .of(this, TemplateViewModelFactory(templateName!!))
            .get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val templateView = getTemplateView(templateName, this.context)

//        templateView.viewTreeObserver.addOnDrawListener {
//            ViewTreeObserver.OnDrawListener {
//                overr
//            }
//        }

        binding.canvasImage.setImageBitmap(getBitmapFromView(templateView))
        
        adapter = ControllerAdapter(childFragmentManager, templateView)

        binding.fullScreen.setOnClickListener {
            showFullScreenCanvasDialog(templateView)
        }

        recyclerInit()

        return root
    }

    private fun getBitmapFromView(view: TemplateView): Bitmap? {
        val returnedBitmap = Bitmap.createBitmap(
            view.canvasWidth.toInt(),
            view.canvasHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(returnedBitmap)

        view.draw(canvas)

        return returnedBitmap
    }

    private fun recyclerInit() {
        val recyclerTemplates: RecyclerView = binding.recyclerControllers

        recyclerTemplates.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerTemplates.adapter = adapter

        templateViewModel.controllerList.observe(viewLifecycleOwner, {
            adapter.addAllControllerList(it)
        })
    }

    private fun showFullScreenCanvasDialog(templateView: TemplateView) {
        val dialog = Dialog(requireContext(), R.style.full_screen_dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.full_screen_layout)

        val imageView = dialog.findViewById<SubsamplingScaleImageView>(R.id.full_canvas)

        imageView.setImage(ImageSource.bitmap(getBitmapFromView(templateView)!!))

        //dialog.dismiss()

        dialog.show()
    }

    private fun getTemplateView(templateName: String, context: Context?): TemplateView = when(templateName) {
        "classic" -> ClassicV1TemplateView(context, null)
        "half" -> HalfV1TemplateView(context, null)

        else -> ClassicV1TemplateView(context, null)
    }

}