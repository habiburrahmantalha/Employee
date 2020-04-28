package com.tigerit.employee.ui.add_employee

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.tigerit.employee.R
import javax.inject.Inject
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tigerit.employee.di.qualifiers.ViewModelInjection
import com.tigerit.employee.di.ViewModelInjectionField
import com.tigerit.employee.base.BaseActivity
import com.tigerit.employee.component.Input
import com.tigerit.employee.ui.home.Employee
import kotlinx.android.synthetic.main.activity_add_employee.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddEmployeeActivity : BaseActivity(), View.OnClickListener {

    companion object{
        @JvmStatic var employee: Employee? = null
    }
    @Inject
    @ViewModelInjection
    lateinit var viewModel: ViewModelInjectionField<AddEmployeeVM>

    override fun layoutRes() = R.layout.activity_add_employee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator ( R.drawable.ic_back );
        }
        initViews()
        setListeners()
    }

    private fun initViews() {
        input_name.init("Full Name", Input.EditTextType.NAME, "Enter Name (length >= 5)", true)
        input_age.init("Age", Input.EditTextType.NUMBER, "Enter Age ( less than 100)", true)
        viewModel.get().genderLiveData.postValue("Female")
        viewModel.get().imagePathLiveData.postValue("")
        viewModel.get().addedLiveData.observe(this, Observer {
            it.let { added->
                if(added)
                    onBackPressed()
            }
        })
        button_submit.setLabel("Add Employee")

        employee?.let {
            input_name.text = it.name
            input_age.text = it.age.toString()
            viewModel.get().genderLiveData.postValue(it.gender)
            when (it.gender) {
                "Male" -> {
                    rb_male.isChecked = true
                }
                "Female" -> {
                    rb_female.isChecked = true
                }
                else -> {
                    rb_other.isChecked = true
                }
            }
            button_submit.setLabel("Update Employee")
            it.photo?.let{ photo ->
                decode(photo)
            }
        }
    }

    private fun setListeners(){
        button_select_image.setOnClickListener(this,button_select_image)
        button_submit.setOnClickListener(this,button_submit)

        toolbar.setNavigationOnClickListener{
            onClick(findViewById(R.id.toolbar))
        }

        rg_gender.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_female -> {
                        viewModel.get().genderLiveData.postValue("Female")
                    }
                    R.id.rb_male -> {
                        viewModel.get().genderLiveData.postValue("Male")
                    }
                    else -> {
                        viewModel.get().genderLiveData.postValue("Other")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(imageReturnedIntent)
                if (resultCode == RESULT_OK) {
                    try {
                        var imagePath = result.uri.toString()

                        imagePath = imagePath!!.replace("file://", "")
                        val resultUri = result.uri
                        //val imageStream = contentResolver.openInputStream(resultUri)
                        //val selectedImage = BitmapFactory.decodeStream(imageStream)

                        var stringImage = encode(resultUri)
                        decode(stringImage)

                        //iv_photo.setImageBitmap(selectedImage)
                        //viewModel.get().imagePathLiveData.postValue(imagePath)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar -> {
                onBackPressed()
            }
            R.id.button_select_image-> {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this@AddEmployeeActivity)
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {

                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                        }

                    }

                    )
                    .check()

            }
            R.id.button_submit ->{
                if(!input_name.validate() && !input_age.validate()) {
                    button_submit.setLoadingStatus(true)
                    if(employee == null)
                        viewModel.get().addEmployee(input_name.text, input_age.text)
                    else
                        viewModel.get().updateEmployee(employee?.id!!, input_name.text, input_age.text)
                }
            }
        }
    }

    fun encode(imageUri: Uri): String {
        val input = getContentResolver().openInputStream(imageUri)
        val image = BitmapFactory.decodeStream(input , null, null)
        //encode image to base64 string
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        var imageBytes = baos.toByteArray()
        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageString
    }

    fun decode(imageString: String) {
        //decode base64 string to image
        Log.d("Image size",imageString.length.toString())
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        iv_photo.setImageBitmap(decodedImage)
        viewModel.get().imagePathLiveData.postValue(imageString)
    }
}