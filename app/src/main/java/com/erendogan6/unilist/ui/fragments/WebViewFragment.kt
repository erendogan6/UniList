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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val url = arguments?.getString("url") ?: "https://google.com"
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        setupWebView(url)
        handleBackPress()
        setupUIActions()
        bindName()
        return binding.root
    }

    private fun bindName() {
        val name = arguments?.getString("name")
        binding.universityName.text = name
    }

    @SuppressLint("SetJavaScriptEnabled") private fun setupWebView(url: String) {
        binding.myWebView.apply {
            settings.javaScriptEnabled = true
            loadUrl(url)
            webViewClient = createWebViewClient()
        }
        updateSecurityIcon(url)
    }

    private fun createWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (isAdded) {
                    super.onPageStarted(view, url, favicon)
                    binding.urlTextView.text = formatUrlForDisplay(url!!)
                    TooltipCompat.setTooltipText(binding.urlTextView, url)
                }
            }
        }
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.myWebView.canGoBack()) {
                binding.myWebView.goBack()
            } else {
                isEnabled = false
                findNavController().popBackStack()
            }
        }
    }

    private fun setupUIActions() {
        binding.closeButton.setOnClickListener { findNavController().popBackStack() }
        binding.refreshButton.setOnClickListener { binding.myWebView.reload() }
    }

    private fun updateSecurityIcon(url: String) {
        val isSecure = isHttps(url)
        binding.ImageViewSecure.setImageResource(if (isSecure) R.drawable.secure_icon else R.drawable.unsecure_icon)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}