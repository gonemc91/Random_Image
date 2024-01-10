package com.example.myapplication

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myapplication.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var useKeyword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * If you need get image size use this.
         */

        /*binding.imageView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.layout_height)
        binding.imageView.layoutParams.wight = resources.getDimensionPixelSize(R.dimen.layout_wight)

        binding.imageView.requestLayout()*/


        binding.button.setOnClickListener() {
            if (useKeyword)
                getRandomImageWithKeyword()
            else
                getRandomImagePressed()

        }

        binding.RadioGroup.setOnCheckedChangeListener { _, checkeID ->
            getRandomImagePressed()
        }

        binding.editTextTextPersonName.setOnEditorActionListener() { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener getRandomImageWithKeyword()
            }
            return@setOnEditorActionListener false
        }
        binding.checkbox.setOnClickListener {
            this.useKeyword = binding.checkbox.isChecked
            updateUi()
        }

    }

    private fun getRandomImageWithKeyword(): Boolean {
        val keyword = binding.editTextTextPersonName.text.toString()
        if (useKeyword && keyword.isBlank()) {
            binding.editTextTextPersonName.error = "Keyword is empty"
            return true
        }
        val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name())

        binding.progressBar.visibility = View.VISIBLE

        Glide.with(this)
            .load("https://source.unsplash.com/random/600x600${if (useKeyword) "?$encodedKeyword" else ""}")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.imageView)
        return false
    }


    private fun getRandomImagePressed(): Boolean {
        val checkedId = binding.RadioGroup.checkedRadioButtonId
        var keyword = ""

        if (checkedId != binding.All.id)
            keyword = binding.RadioGroup.findViewById<Button>(checkedId).text.toString()

        val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name())

        binding.progressBar.visibility = View.VISIBLE

        Glide.with(this)
            .load("https://source.unsplash.com/random/600x600?$encodedKeyword")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.imageView)
        return false
    }

    private fun updateUi() = with(binding) {
        checkbox.isChecked = useKeyword
        if (useKeyword) {
            editTextTextPersonName.visibility = View.VISIBLE
            RadioGroup.visibility = View.GONE
        } else {
            editTextTextPersonName.visibility = View.GONE
            RadioGroup.visibility = View.VISIBLE
        }

    }

}

