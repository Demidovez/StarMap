package com.nikolaydemidovez.starmap.controllers.save_v1

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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SaveV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: SaveV1ControllerViewModel
    private lateinit var binding: FragmentSaveV1ControllerBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(SaveV1ControllerViewModel::class.java)
        binding = FragmentSaveV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        // Обработка нажатия кнопки "Сохранить"
        binding.saveBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                val extensionFile = root.findViewById<RadioButton>(binding.radioGroupFormat.checkedRadioButtonId).text.toString()
                val typeFile = if(extensionFile == "PNG") "image/png" else "application/pdf"

                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(Intent.EXTRA_TITLE, "StarMap.${extensionFile.lowercase()}")
                    type = typeFile
                }

                val activityLauncher = when(extensionFile) {
                    "PNG" -> savePngLauncher
                    "PDF" -> savePdfLauncher

                    else -> savePngLauncher
                }

                activityLauncher.launch(intent)
            }
        }

        // Обработка нажатия кнопки "Поделиться"
        binding.shareBtn.setOnClickListener {
            val extensionFile = root.findViewById<RadioButton>(binding.radioGroupFormat.checkedRadioButtonId).text.toString()
            val sharingFile = templateCanvas.convertToSharingFile(extensionFile)
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

        return root
    }

    var savePngLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            templateCanvas.saveToPNG(it.data?.data!!)
        }
    }

    var savePdfLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                templateCanvas.saveToPDF(it.data?.data!!)
            }
        }
    }

}