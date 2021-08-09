package com.nikolaydemidovez.starmap.pages.settings

import android.content.ActivityNotFoundException
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentProjectsBinding
import com.nikolaydemidovez.starmap.databinding.FragmentSettingsBinding
import com.nikolaydemidovez.starmap.pages.projects.ProjectsViewModel
import com.nikolaydemidovez.starmap.pages.projects.ProjectsViewModelFactory
import android.content.Intent
import android.net.Uri
import java.lang.Exception


class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

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