package com.nikolaydemidovez.starmap.controllers.save_v1

import adapters.ColorAdapter
import adapters.FileFormatAdapter
import adapters.HolstSizeAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.FileProvider
import com.nikolaydemidovez.starmap.databinding.FragmentSaveV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.pojo.Holst

class SaveV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: SaveV1ControllerViewModel
    private lateinit var binding: FragmentSaveV1ControllerBinding
    private var currentFormat = MutableLiveData("jpg")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(SaveV1ControllerViewModel::class.java)
        binding = FragmentSaveV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        recyclerFormatFile()

        binding.shareBtn.setOnClickListener {
            val sharingFile = templateCanvas.convertToSharingFile(currentFormat.value!!)
            val uriSharingFile = FileProvider.getUriForFile(requireContext(), "com.nikolaydemidovez.starmap.provider", sharingFile)

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND

                putExtra(Intent.EXTRA_STREAM, uriSharingFile)
                putExtra(Intent.EXTRA_SUBJECT, "Создано в StarMap: https://play.google.com/store/apps/details?id=com.diets.weightloss&hl=ru&gl=US") // TODO: Исправить ссылку на приложение

                type = "*/*"
            }

            // Выдаем разрешения списку приложений для шаринга
            val chooserApp = Intent.createChooser(sendIntent, "Share File")

            val resInfoList: List<ResolveInfo> = requireContext().packageManager.queryIntentActivities(chooserApp, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName

                requireContext().grantUriPermission(
                    packageName,
                    uriSharingFile,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooserApp)

            sharingFile.deleteOnExit()
        }

        binding.downloadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                val typeFile = if(currentFormat.value!! == "jpg") "image/jpeg" else "application/pdf"

                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(Intent.EXTRA_TITLE, "StarMap.${currentFormat.value!!}")
                    type = typeFile
                }

                val activityLauncher = when(currentFormat.value!!) {
                    "jpg" -> saveJpgLauncher
                    "pdf" -> savePdfLauncher

                    else -> saveJpgLauncher
                }

                activityLauncher.launch(intent)
            }
        }

        return root
    }

    private val saveJpgLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            templateCanvas.saveToJPG(it.data?.data!!)
        }
    }

    private val savePdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // TODO: нужна ли проверка?
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                templateCanvas.saveToPDF(it.data?.data!!)
            }
        }
    }


            private fun recyclerFormatFile() {
        val holstSizeAdapter = FileFormatAdapter(currentFormat) {
            currentFormat.value = it
        }

        currentFormat.observe(requireActivity()) {
            holstSizeAdapter.notifyDataSetChanged()
        }

        val recyclerSize: RecyclerView = binding.formatFileRecycler

        recyclerSize.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerSize.adapter = holstSizeAdapter

        holstSizeAdapter.addAllFormatList(arrayListOf(
            "jpg",
            "pdf"
        ))
    }

}