package com.kalambur.mappy_stars.pages.settings

import android.content.ActivityNotFoundException
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.databinding.FragmentSettingsBinding
import android.content.Intent
import android.graphics.Insets
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import java.lang.Exception
import android.widget.Toast
import com.kalambur.mappy_stars.MainActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kalambur.mappy_stars.utils.extensions.dismissWithAds
import java.util.*

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.tellAboutError.setOnClickListener {
            showTellAboutErrorDialog()
        }

        binding.share.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mappy Stars: Звездная карта")
                var shareMessage = "Mappy Stars: Звездная карта\n\nПриложение для создания звездных карт!\n\n"
                shareMessage =
                    """${shareMessage}https://play.google.com/store/apps/details?id=${this.requireContext().packageName}
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Выберите"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.review.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${this.requireContext().packageName}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${this.requireContext().packageName}")))
            }
        }

        binding.politics.setOnClickListener {
            val browse = Intent(Intent.ACTION_VIEW, Uri.parse("https://mappy-stars-zvezdna.flycricket.io/privacy.html"))

            startActivity(browse)
        }

        binding.telegram.setOnClickListener {
            val browse = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mappy_stars"))

            startActivity(browse)
        }

        return root
    }

    private fun showTellAboutErrorDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.dialog_tell_about_error_layout, null)

        val title = "Сообщение"

        val titleTextView    = layout.findViewById<TextView>(R.id.title)
        val editContact      = layout.findViewById<EditText>(R.id.contact)
        val editMessage      = layout.findViewById<EditText>(R.id.message)
        val imgClearContact  = layout.findViewById<ImageView>(R.id.clearContact)
        val imgClearMessage  = layout.findViewById<ImageView>(R.id.clearMessage)

        titleTextView.text = title

        editMessage.doOnTextChanged { text, _, _, _ ->
            titleTextView.text = title
            titleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))
            imgClearMessage.visibility = if(text!!.isNotEmpty()) View.VISIBLE else View.GONE
        }

        editContact.doOnTextChanged { text, _, _, _ ->
            imgClearContact.visibility = if(text!!.isNotEmpty()) View.VISIBLE else View.GONE
        }

        editMessage.doOnTextChanged { text, _, _, _ ->
            imgClearMessage.visibility = if(text!!.isNotEmpty()) View.VISIBLE else View.GONE
        }

        imgClearContact.setOnClickListener { editContact.text = null }
        imgClearMessage.setOnClickListener { editMessage.text = null }

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton("Отправить", null)
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
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                if(editMessage.text.isNotEmpty()) {
                    sendProblemToFirebase(editMessage.text.toString(), editContact.text.toString())

                    dialog.dismissWithAds(requireActivity())
                } else {
                    titleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_flat))
                    titleTextView.text = "Пустое значение недопустимо!"
                }
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun sendProblemToFirebase(message: String, contact: String) {
        val database = Firebase.database("https://mappy-stars-default-rtdb.europe-west1.firebasedatabase.app")
        val myRef = database.getReference("problems")

        myRef.child(Calendar.getInstance().time.time.toString()).setValue("$message::$contact")
            .addOnSuccessListener {
                Toast.makeText(activity, "Отправлено!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Ошибка!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editActionAndStatusBar() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)
        }
    }
}