package com.kalambur.mappy_stars.controllers.save_v1

import adapters.FileFormatAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import com.kalambur.mappy_stars.databinding.FragmentSaveV1ControllerBinding
import com.kalambur.mappy_stars.templates.TemplateCanvas
import android.content.pm.ResolveInfo
import android.content.pm.PackageManager
import android.graphics.Insets
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.templates.TemplateCanvas.Companion.DEFAULT

class SaveV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var binding: FragmentSaveV1ControllerBinding
    private var currentFormat = MutableLiveData("jpg")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSaveV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        recyclerFormatFile()

        binding.shareBtn.setOnClickListener {
            val sharingFile = templateCanvas.convertToSharingFile(currentFormat.value!!)
            val uriSharingFile = FileProvider.getUriForFile(requireContext(), "com.kalambur.mappy_stars..provider", sharingFile)

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
                    putExtra(Intent.EXTRA_TITLE, "StarMap.${currentFormat.value!!}") // TODO: Переименовать на название приложения
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

        binding.saveBtn.setOnClickListener {
            showSaveDialog()
        }

        binding.deleteBtn.visibility = if(templateCanvas.type == DEFAULT) GONE else VISIBLE

        binding.deleteBtn.setOnClickListener {
            showDeleteDialog()
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

    private fun showSaveDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.dialog_save_layout, null)
        layout.findViewById<TextView>(R.id.title).text = resources.getString(R.string.save_to_project)

        val editText      = layout.findViewById<EditText>(R.id.editName)
        val imgClearText  = layout.findViewById<ImageView>(R.id.clearText)
        val descText      = layout.findViewById<TextView>(R.id.descText)

        descText.text = resources.getString(R.string.title_project)

        editText.setText(templateCanvas.title ?: templateCanvas.category)
        editText.doOnTextChanged { text, _, _, _ ->
            descText.text = resources.getString(R.string.title_project)
            descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))

            imgClearText.visibility = if(text!!.isNotEmpty()) VISIBLE else GONE
        }
        imgClearText.setOnClickListener { editText .setText("") }

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton(R.string.save, null)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setView(layout)

        val dialog = builder.create()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.setDecorFitsSystemWindows(false)
            dialog.window?.decorView!!.setOnApplyWindowInsetsListener { v, insets ->
                val imeInsets: Insets = insets.getInsets(WindowInsets.Type.ime())
                val paddingBottom = if(imeInsets.bottom == 0) 40 else imeInsets.bottom
                v.updatePadding(bottom = paddingBottom)
                insets
            }
        } else {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        dialog.setOnShowListener {
            imgClearText.visibility = if(editText.text.isNotEmpty()) VISIBLE else GONE
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                if(editText.text.isNotEmpty()) {
                    templateCanvas.saveToProjects(editText.text.toString())

                    requireView().findNavController().navigate(R.id.action_templateFragment_to_navigation_projects)

                    dialog.dismiss()
                } else {
                    descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_flat))
                    descText.text = resources.getString(R.string.error_empty)
                }
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

    }

    private fun showDeleteDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.dialog_ask_layout, null)

        layout.findViewById<TextView>(R.id.title).text = resources.getString(R.string.you_sure)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton(R.string.delete, null)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setView(layout)

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                templateCanvas.deleteProject()

                requireView().findNavController().navigate(R.id.action_templateFragment_to_navigation_projects)

                dialog.dismiss()
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

}