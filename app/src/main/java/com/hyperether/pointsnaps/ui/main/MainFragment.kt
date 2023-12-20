package com.hyperether.pointsnaps.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.model.Location
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.databinding.MainFragmentBinding
import com.hyperether.pointsnaps.ui.base.BaseFragment
import com.hyperether.pointsnaps.utils.Constants
import com.hyperether.pointsnaps.utils.Utils
import com.hyperether.pointsnapssdk.PointSnapsSDK
import kotlinx.coroutines.launch
import java.io.File


class MainFragment : BaseFragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private var description = ""
    private var file: File = File("")
    private var location = Location()
    private var sucess = false

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!)[MainViewModel::class.java]
        setToolBar()

        setupObservers()

        binding.apply {
            buttonLoc.setOnClickListener {
                checkLocationPermission()
            }

            buttonDes.setOnClickListener {
                findNavController().navigate(R.id.action_main_to_description)
            }

            cam.setOnClickListener {
                openFileChooser()
            }

            camChange.setOnClickListener {
                openFileChooser()
            }
        }
    }


    private fun setupObservers() {
        viewModel.file.observe(viewLifecycleOwner) {
            file = it
            if (Utils.isNotFileEmpty(file)) {
                binding.roundedImage.setImageURI(Uri.parse(file.absolutePath))
                binding.roundedImageLayout.visibility = VISIBLE
            } else {
                binding.roundedImageLayout.visibility = GONE
            }
        }

        viewModel.descriptionData.observe(viewLifecycleOwner) {
            description = it
            if (description.isNotEmpty())
                binding.buttonDesTxt.text = description
            else
                binding.buttonDesTxt.text = getString(R.string.description)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            createToast(it)
        }

        viewModel.location.observe(viewLifecycleOwner) {
            location = it
            if (location.address.isNotEmpty())
                binding.buttonLocTxt.text = location.address
            else
                binding.buttonLocTxt.text = getString(R.string.location)
        }

        viewModel.successUpload.observe(viewLifecycleOwner) {
            sucess = it
            if (it) {
                createToast(getString(R.string.success_upload))
                viewModel.successUpload.postValue(false)
            }
            buttonChecker()
        }
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

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(context!!.packageManager)?.also {
                file = Utils.createImageFile(context!!)
                file?.also {
                    val uri = FileProvider.getUriForFile(
                        context!!,
                        "com.hyperether.pointsnaps.fileprovider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(intent, Constants.OPEN_CAMERA_REQUEST)
                }
            }
        }
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
                    viewModel.setFile(file)
                }

            Constants.OPEN_FILES_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    file = Utils.getFileFromUri(data?.data!!, context!!)
                    viewModel.setFile(file)
                }
        }
    }

    fun buttonChecker() {
        if (!Utils.isNotFileEmpty(file)) {
            binding.apply {
                buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_camera_btn))
                buttonTxt.text = getString(R.string.take_a_photo)
                mainButton.setOnClickListener {
                    openFileChooser()
                }
            }
        } else if (location.address.isEmpty()) {
            binding.apply {
                buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_location_btn))
                buttonTxt.text = getString(R.string.add_location)
                mainButton.setOnClickListener {
                    findNavController().navigate(R.id.action_main_to_location)
                }
            }
        } else if (description.isEmpty()) {
            binding.apply {
                buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_description_btn))
                buttonTxt.text = getString(R.string.add_description)
                mainButton.setOnClickListener {
                    findNavController().navigate(R.id.action_main_to_description)
                }
            }
        } else {
            binding.apply {
                buttonImage.setImageDrawable(getDrawable(context!!, R.drawable.ic_upload_btn))
                buttonTxt.text = getString(R.string.upload)
                mainButton.setOnClickListener {
                    if (PointSnapsSDK.isUserLoggedIn()) {
                        progressBar.visibility = VISIBLE
                        viewModel.upload(
                            file,
                            file.name,
                            file.extension,
                            location.address,
                            location.lon,
                            location.lat,
                            description
                        ) {
                            progressBar.visibility = GONE
                        }
                    } else {
                        createToast(getString(R.string.must_login))
                    }
                }
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

    private fun setToolBar() {
        binding.apply {
            toolbar.menu.clear()
            if (!PointSnapsSDK.isUserLoggedIn()) {
                toolbar.inflateMenu(R.menu.login_menu)
            } else {
                toolbar.inflateMenu(R.menu.main_menu)
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.login_menu_item -> {
                        findNavController().navigate(R.id.goToAuthNav)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.logout_menu_item -> {
                        lifecycleScope.launch {
                            val logout = viewModel.logout()
                            if (logout) {
                                createToast(getString(R.string.logout_toast))
                            }
                            setToolBar()
                        }
                        return@setOnMenuItemClickListener true
                    }

                    R.id.dark_menu_item -> {
                        PointSnapsSDK.setDarkMode()
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                        )
                        return@setOnMenuItemClickListener true
                    }

                    R.id.light_menu_item -> {
                        PointSnapsSDK.setLightMode()
                        AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                        )
                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
        }
    }
}
