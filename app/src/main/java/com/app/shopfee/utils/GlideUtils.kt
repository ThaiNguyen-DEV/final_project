package com.app.shopfee.utils

import android.widget.ImageView
import com.app.shopfee.R
import com.bumptech.glide.Glide

object GlideUtils {
    fun loadUrlBanner(url: String?, imageView: ImageView?) {
        if (StringUtil.isEmpty(url)) {
            imageView!!.setImageResource(R.drawable.image_no_available)
            return
        }
        Glide.with(imageView!!.context)
            .load(url)
            .error(R.drawable.image_no_available)
            .dontAnimate()
            .into(imageView)
    }

    fun loadUrl(url: String?, imageView: ImageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.image_no_available)
            return
        }
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.image_no_available)
            .dontAnimate()
            .into(imageView)
    }
}