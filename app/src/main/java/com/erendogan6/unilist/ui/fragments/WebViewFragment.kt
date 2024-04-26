package com.erendogan6.unilist.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erendogan6.unilist.R
import com.erendogan6.unilist.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("SetJavaScriptEnabled") override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val url = arguments?.getString("url") ?: "https://google.com"
        val name = arguments?.getString("name")

        binding.universityName.text = name

        val webView = binding.myWebView

        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)

        val isSecure = isHttps(url)
        binding.ImageViewSecure.setImageResource(if (isSecure) R.drawable.secure_icon
        else R.drawable.unsecure_icon)

        requireActivity().onBackPressedDispatcher.addCallback {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                findNavController().popBackStack()
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                val formattedUrl = formatUrlForDisplay(url!!)
                binding.urlTextView.text = formattedUrl
                TooltipCompat.setTooltipText(binding.urlTextView, url)
            }
        }
        binding.closeButton.setOnClickListener { findNavController().popBackStack() }
        binding.refreshButton.setOnClickListener { webView.reload() }
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun formatUrlForDisplay(url: String): String {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host?.removePrefix("www.") ?: ""
            val pathSegments = uri.pathSegments
            val titleSegment = if (pathSegments.isNotEmpty()) pathSegments[0] else ""
            "$host/$titleSegment"
        } catch (e: Exception) {
            "Invalid URL"
        }
    }

    private fun isHttps(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            uri.scheme?.equals("https", ignoreCase = true) ?: false
        } catch (e: Exception) {
            false
        }
    }


}