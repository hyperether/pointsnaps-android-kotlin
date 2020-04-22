package com.hyperether.pointsnaps.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.Location
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.utils.Constants
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File

class MainFragment : Fragment() {

    private var description = ""
    private lateinit var file: File
    private lateinit var location: Location
    private lateinit var imageuri: Uri

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        setupObservers()

        buttonLoc.setOnClickListener { v ->
            checkLocationPermission()
        }

        buttonDes.setOnClickListener { v ->
            findNavController().navigate(R.id.action_main_to_description)
        }

        cam.setOnClickListener {
            openFileChooser()
        }

        cam_change.setOnClickListener {
            openFileChooser()
        }

        mainButton.setOnClickListener {
            Toast.makeText(context, description, Toast.LENGTH_LONG).show()
        }
    }


    fun setupObservers() {
        viewModel.getDescriptionLiveData().observe(viewLifecycleOwner, Observer {
            description = it
        })

        viewModel.successUpload.observe(viewLifecycleOwner, Observer {
            //todo clear views
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.location.observe(viewLifecycleOwner, Observer {
            location = it
        })
    }

    override fun onResume() {
        super.onResume()
        buttonChecker()
    }

    fun openFileChooser() {

        val customLayout = layoutInflater.inflate(R.layout.choose_file_dialog, null)
        val camera = customLayout.findViewById<TextView>(R.id.photo)
        val files = customLayout.findViewById<TextView>(R.id.gallery)
        val alert = AlertDialog.Builder(context)
            .setView(customLayout)
            .create()
        alert.show()

        camera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED ||
                    ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), Constants.OPEN_CAMERA_PERMISSION
                    )
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
            alert.dismiss()
        }
        files.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (
                    ActivityCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.OPEN_FILES_PERMISSION
                    )
                } else {
                    openFiles()
                }
            } else {
                openFiles()
            }
            alert.dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.OPEN_CAMERA_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamera()
            Constants.OPEN_FILES_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openFiles()
            Constants.LOCATION_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    findNavController().navigate(R.id.action_main_to_location)
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageuri = activity!!.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)
        startActivityForResult(cameraIntent, Constants.OPEN_CAMERA_REQUEST)
    }

    private fun openFiles() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.OPEN_FILES_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.OPEN_CAMERA_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    roundedImage.setImageURI(imageuri)
                    roundedImageLayout.visibility = VISIBLE
                    file = File(imageuri.path!!)
                }
            Constants.OPEN_FILES_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    roundedImage.setImageURI(data!!.data)
                    roundedImageLayout.visibility = VISIBLE
                    file = File(data.data!!.path!!)
                }
        }
    }

    fun buttonChecker() {
        if (!this::file.isInitialized) {
            buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_camera_btn))
            buttonTxt.text = getString(R.string.take_a_photo)
            mainButton.setOnClickListener {
                openFileChooser()
            }
        } else if (!this::location.isInitialized) {
            buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_location_btn))
            buttonTxt.text = getString(R.string.add_location)
            mainButton.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_location)
            }
        } else if (description.isEmpty()) {
            buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_description_btn))
            buttonTxt.text = getString(R.string.add_description)
            mainButton.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_description)
            }
        } else {
            buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_upload_btn))
            buttonTxt.text = getString(R.string.upload)
            mainButton.setOnClickListener {
                viewModel.upload(
                    file,
                    file.name,
                    file.extension,
                    location.address,
                    location.lon,
                    location.lat,
                    description
                )
            }
        }

    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), Constants.LOCATION_PERMISSION
                )
            } else {
                findNavController().navigate(R.id.action_main_to_location)
            }
        } else {
            findNavController().navigate(R.id.action_main_to_location)
        }
    }
}
