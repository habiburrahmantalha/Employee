package com.tigerit.employee.component

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tigerit.employee.R
import kotlinx.android.synthetic.main.layout_button.view.*


class Button @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var view: View? = null

    private var listener: OnClickListener? = null

    init {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.Button)
        LayoutInflater.from(context).inflate(R.layout.layout_button, this)

        tv_label.text = attributes.getString(R.styleable.Button_label)
        _layout.setOnClickListener(this)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawableProgress =
                DrawableCompat.wrap(progress_bar.indeterminateDrawable)
            DrawableCompat.setTint(
                drawableProgress,
                ContextCompat.getColor(getContext(), R.color.white)
            )
            progress_bar.indeterminateDrawable = DrawableCompat.unwrap(drawableProgress)
        }

        attributes.recycle()
    }

    fun setLoadingStatus(visibility: Boolean) = if (visibility){
        progress_bar.visibility = View.VISIBLE
    }else{
        progress_bar.visibility = View.INVISIBLE
    }

    fun setOnClickListener(listener: OnClickListener, view:View) {
        this.listener = listener
        this.view = view
    }

    override fun onClick(view: View?) {
        listener?.onClick(this.view)
    }

    fun setLabel(s: String) {
        tv_label.text = s
    }


}

