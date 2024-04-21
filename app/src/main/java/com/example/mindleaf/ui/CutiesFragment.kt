package com.example.mindleaf.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mindleaf.R
import com.example.mindleaf.api.PuppyApi
import kotlinx.coroutines.launch

class CutiesFragment : Fragment() {
    private lateinit var puppyImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cuties, container, false)
        puppyImageView = view.findViewById(R.id.puppyImageView)
        view.setOnClickListener {
            refreshPuppyImage()
        }
        return view
    }
    fun refreshPuppyImage() {
        // Initiate the fetch in a coroutine scope
        lifecycleScope.launch {
            val puppyImageUrlOrResourceId = fetchPuppyImageUrl()
            displayPuppyImage(puppyImageUrlOrResourceId)
        }
    }
    // This function is suspend because it makes a network request
    private suspend fun fetchPuppyImageUrl(): String {
        return try {
            val puppyImage = PuppyApi.instance.getRandomPuppyImage()
            puppyImage.imageUrl
        } catch (e: Exception) {
            // Log the exception and return the fallback image
            Log.e("CutiesFragment", "Failed to fetch puppy image", e)
            ""
        }
    }

    // This function loads the image using Glide
    private fun displayPuppyImage(puppyImageUrlOrResourceId: String) {
        val isResource = puppyImageUrlOrResourceId.toIntOrNull() != null
//        Glide.with(this)
//            .load(if (isResource) puppyImageUrlOrResourceId.toInt() else puppyImageUrlOrResourceId)
//            .placeholder(R.drawable.ic_puppy_placeholder)
//            .error(R.drawable.fallback_puppy)
//            .into(puppyImageView)
        Glide.with(this)
            .load(puppyImageUrlOrResourceId)
            .thumbnail(Glide.with(this).load(puppyImageView.drawable))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .placeholder(R.drawable.ic_puppy_placeholder)
            .error(R.drawable.fallback_puppy)
            .into(puppyImageView)
//        if (isResource) {
//            // If the image is a resource, load it directly without a placeholder
//            Glide.with(this)
//                .load(puppyImageUrlOrResourceId.toInt())
//                .error(R.drawable.fallback_puppy)
//                .into(puppyImageView)
//        } else {
//            // If the image is a URL, load it with a placeholder
//            Glide.with(this)
//                .load(puppyImageUrlOrResourceId)
//                .placeholder(R.drawable.ic_puppy_placeholder)
//                .error(R.drawable.fallback_puppy)
//                .into(puppyImageView)
//        }
    }
}
