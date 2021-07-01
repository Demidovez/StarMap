package com.nikolaydemidovez.starmap.pages.template

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateCanvas
import com.nikolaydemidovez.starmap.templates.half_v1.HalfV1TemplateCanvas


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

        val templateView = getTemplateView(templateName)

        templateView.setOnDrawListener(object: TemplateCanvas.OnDrawListener {
            override fun onDraw() {
                binding.canvasImage.setImageBitmap(templateView.bitmap)
            }
        })

        adapter = ControllerAdapter(childFragmentManager, templateView)

        binding.fullScreen.setOnClickListener {
            showFullScreenCanvasDialog(templateView)
        }

        recyclerInit()

        return root
    }

    private fun recyclerInit() {
        val recyclerTemplates: RecyclerView = binding.recyclerControllers

        recyclerTemplates.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerTemplates.adapter = adapter

        templateViewModel.controllerList.observe(viewLifecycleOwner, {
            adapter.addAllControllerList(it)
        })
    }

    private fun showFullScreenCanvasDialog(templateCanvas: TemplateCanvas) {
        val dialog = Dialog(requireContext(), R.style.full_screen_dialog)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.full_screen_layout)



//        dialog.window?.addFlags(FLAG_LAYOUT_NO_LIMITS);


//        dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        dialog.window?.setStatusBarColor(R.color.red);

//        val window = dialog.window!!
//        val winParams = window.attributes
//        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
//        window.attributes = winParams
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

//        dialog.window?.insetsController?.setSystemBarsAppearance(
//            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//        )

        //dialog.window?.decorView?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN)
        //dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_FULLSCREEN)




        //dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

//        dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        dialog.findViewById<LinearLayout>(R.id.linear_layout).setPadding(0, statusBarHeight(this), 0, 0)


        //dialog?.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.window?.setFlags(0, flagsToUpdate);

        //dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        //dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        //WindowCompat.setDecorFitsSystemWindows(dialog.window!!, false)

        //dialog.window?.statusBarColor = Color.TRANSPARENT

//        dialog.window?.statusBarColor = 0


        //dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN


//        dialog.window?.statusBarColor = Color.TRANSPARENT

//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            dialog.window?.statusBarColor = Color.TRANSPARENT
//        }

        //dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val controller: WindowInsetsController = dialog.window?.insetsController!!
//            controller.hide(WindowInsets.Type.statusBars())
//        } else {
//            dialog.window?.decorView?.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_IMMERSIVE
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            )
//        }

//        dialog.window?.setDecorFitsSystemWindows(false)
//        val controller: WindowInsetsController = dialog.window?.getInsetsController()!!
//        if (controller != null) {
//            controller.hide(WindowInsets.Type.statusBars())
//            controller.systemBarsBehavior =
//                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }

        val imageView = dialog.findViewById<SubsamplingScaleImageView>(R.id.full_canvas)

        imageView.setImage(ImageSource.bitmap(templateCanvas.bitmap))

        //dialog.dismiss()

        dialog.show()
    }

    private fun getTemplateView(templateName: String): TemplateCanvas = when(templateName) {
        "classic_v1" -> ClassicV1TemplateCanvas(requireContext())
        "half_v1" -> HalfV1TemplateCanvas(requireContext())

        else -> ClassicV1TemplateCanvas(requireContext())
    }

}