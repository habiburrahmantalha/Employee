package com.tigerit.employee.component

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.tigerit.employee.R
import com.tigerit.employee.component.Input.EditTextType.*
import kotlinx.android.synthetic.main.layout_input.view.*

class Input @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var type: EditTextType? = null
    private var isError: Boolean = false
    private var visible: Boolean = false
    private var isValidate: Boolean = false
    private var isRequired: Boolean = false
    private var errorText: String = ""

    var text: String
        get() = edit_text!!.text.toString()
        set(s) {
            edit_text!!.setText(s)
            edit_text!!.setSelection(s.length)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_input, this)
    }

    fun init(label: String, type: EditTextType, errorText: String, isRequired: Boolean) {
        tv_label!!.text = label
        this.errorText = errorText
        this.type = type
        this.isRequired = isRequired
        when (type) {
            TEXT, NAME -> edit_text!!.inputType = InputType.TYPE_CLASS_TEXT
            EMAIL -> edit_text!!.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            PASSWORD -> {
                edit_text!!.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
                hidePassword()
            }
            NUMBER -> {
                edit_text!!.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
        tv_error!!.visibility = View.INVISIBLE
        tv_error!!.text = errorText
        iv_visibility!!.visibility = View.INVISIBLE

        iv_visibility!!.setOnClickListener {
            if (visible) {
                iv_visibility!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_visble))
                hidePassword()
            } else {
                iv_visibility!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_visble_not))
                showPassword()
            }
            visible = !visible
        }

        edit_text!!.setOnFocusChangeListener { _, focused ->
            if (isError) {
                edit_text!!.background = ContextCompat.getDrawable(context, R.drawable.round_rectangle_stroke_red)
            } else {
                if (focused) {
                    edit_text!!.background =
                        ContextCompat.getDrawable(context, R.drawable.round_rectangle_focus)
                } else {
                    edit_text!!.background = ContextCompat.getDrawable(context, R.drawable.round_rectangle_unselected)
                }
            }
        }

        edit_text!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (isValidate) {
                    validate()
                }
                if (type == PASSWORD) {
                    if (charSequence.isEmpty())
                        setEditTextSpacing(0f)
                    else {
                        if (visible) {
                            setEditTextSpacing(0f)
                        } else
                            setEditTextSpacing(.22f)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

                if (type == PASSWORD) {
                    if (editable.isEmpty()) {
                        iv_visibility!!.visibility = View.INVISIBLE
                    } else {
                        iv_visibility!!.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    fun validate(): Boolean {
        isValidate = true
        tv_error!!.text = errorText
        if (!isRequired) {
            isError = false
            return false
        }
        val sequence = edit_text!!.text

        when (type) {
            NAME ->
                if (sequence.toString().length < 5) {
                    showError(context)
                } else {
                    hideError(context)
                }
            TEXT -> if (sequence.toString().trim { it <= ' ' }.isEmpty()) {
                showError(context)
            } else {
                hideError(context)
            }
            EMAIL -> if (isValidEmail(sequence.toString().trim { it <= ' ' })) {
                hideError(context)
            } else {
                showError(context)
            }
            PASSWORD -> if (sequence.length <= 5) {
                showError(context)
            } else {
                hideError(context)
            }
            NUMBER -> if (sequence.toString().isNotEmpty() && sequence.toString().toInt() > 100) {
                showError(context)
            } else {
                hideError(context)
            }
        }
        return isError
    }


    private fun showError(context: Context?) {
        edit_text!!.background = ContextCompat.getDrawable(context!!, R.drawable.round_rectangle_stroke_red)
        tv_error!!.visibility = View.VISIBLE
        isError = true
    }

    fun showErrorServer(context: Context, errorText: String) {
        edit_text!!.background = ContextCompat.getDrawable(context, R.drawable.round_rectangle_stroke_red)
        tv_error!!.text = errorText
        tv_error!!.visibility = View.VISIBLE
        isError = true
    }

    private fun hideError(context: Context?) {
        edit_text!!.background = ContextCompat.getDrawable(context!!, R.drawable.round_rectangle_unselected)
        tv_error!!.visibility = View.INVISIBLE
        isError = false
    }

    private fun showPassword() {
        edit_text!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        edit_text!!.setSelection(edit_text!!.text.length)
        setEditTextSpacing(0f)
    }

    internal fun setEditTextSpacing(f: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            edit_text!!.letterSpacing = f
        }
    }

    private fun hidePassword() {
        edit_text!!.transformationMethod = PasswordTransformationMethod.getInstance()
        edit_text!!.setSelection(edit_text!!.text.length)
        if (edit_text!!.text.isNotEmpty())
            setEditTextSpacing(.22f)
    }

    fun setHintText(hint: String) {
        edit_text!!.hint = hint
    }

    enum class EditTextType {
        TEXT, EMAIL, PASSWORD, NAME, NUMBER
    }

    fun focus() {
        edit_text!!.requestFocus()
    }

    companion object {

        fun isValidEmail(target: CharSequence): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}