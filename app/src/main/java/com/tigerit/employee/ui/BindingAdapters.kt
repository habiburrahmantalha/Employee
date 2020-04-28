package com.tigerit.employee.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.tigerit.employee.R
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.lang.Exception
import kotlin.math.roundToInt


@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    val imageView: ImageView  = this
    if(url.isNullOrEmpty()) {
        imageView.setImageResource(R.drawable.placeholder)
        return
    }

    val imageBytes = Base64.decode(url, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    imageView.setImageBitmap(decodedImage)

}