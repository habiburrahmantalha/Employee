package com.tigerit.employee.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.tigerit.employee.R
import com.tigerit.employee.base.BaseActivity
import com.tigerit.employee.di.ViewModelInjectionField
import com.tigerit.employee.di.qualifiers.ViewModelInjection
import com.tigerit.employee.ui.FileUtils
import com.tigerit.employee.ui.add_employee.AddEmployeeActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), View.OnClickListener {


    @Inject
    @ViewModelInjection
    lateinit var viewModel: ViewModelInjectionField<HomeVM>

    override fun layoutRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setListeners()

    }

    private fun setListeners(){
        button_add.setOnClickListener(this, button_add )
        button_export.setOnClickListener(this, button_export)
        button_import.setOnClickListener(this, button_import)
    }

    private fun initViews() {
        val adapter = EmployeeAdapter( listOf(),viewModel.get())
        rv_employees?.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        viewModel.get().employeeListLiveData.observe(this, Observer {
            it.let { list ->
                if(list.size == 0){
                    adapter.updateList(it)
                }else{
                    adapter.updateList(it)
                }
            }
        })

        viewModel.get().employeeLiveData.observe(this, Observer {
            navigateToEdit(it)
        })
        viewModel.get().fileResponseLiveData.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })


    }

    private fun navigateToEdit(employee: Employee?) {
        AddEmployeeActivity.employee = employee
        val intent = Intent(this, AddEmployeeActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add -> {
                navigateToAdd()
            }
            R.id.button_export -> {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            var path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                            viewModel.get().exportDB(path)
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {}
                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            Toast.makeText(this@MainActivity,response.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                    )
                    .check()
            }
            R.id.button_import -> {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            val intent = Intent()
                                .setType("*/*")
                                .setAction(Intent.ACTION_GET_CONTENT)
                            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
                        }
                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {}
                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {}
                    }
                    )
                    .check()
            }
        }
    }

    private fun navigateToAdd() {
        AddEmployeeActivity.employee = null
        val intent = Intent(this, AddEmployeeActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.get().loadData()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data

            selectedFile?.let {
                Log.d("File: ",FileUtils.getPath(this,it))


                viewModel.get().importDB(FileUtils.getPath(this,it))
            }
        }
    }

}
