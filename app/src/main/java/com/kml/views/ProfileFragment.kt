package com.kml.views

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.app.KmlApp
import com.kml.data.externalDbOperations.DbChangePass
import com.kml.data.models.Profile
import com.kml.databinding.FragmentProfileBinding
import com.kml.viewModels.ProfileViewModel
import com.kml.viewModels.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    companion object {
        const val PICK_IMAGE_RESULT = 1
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentProfileBinding

    lateinit var dataFile: FileFactory
    lateinit var editTextOldPassword: EditText
    lateinit var editTextNewPassword: EditText
    lateinit var dialogChangePass: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        dataFile = FileFactory(requireContext())

        val viewModelFactory = ProfileViewModelFactory(dataFile)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.profileProgressBar.visibility = ProgressBar.VISIBLE
        viewModel.profileData.observe(viewLifecycleOwner) {
            launchProfile(it)
        }

        binding.profilePhoto.setOnClickListener {
            openGalleryForImage()
        }
        binding.changePass.setOnClickListener { showDialogToChangePass() }
        return binding.root
    }

    private fun launchProfile(profile: Profile) {
        viewModel.saveProfileValues(profile)
        setProfileData(profile)
        setUserIdentity(profile.firstName, profile.lastName)
        if (LoginScreen.isLogNow) welcomeUser()

    }

    private fun setProfileData(profile: Profile) {
        when {
            viewModel.isFromFile ->
                Toast.makeText(requireContext(), R.string.load_previous_data, Toast.LENGTH_SHORT).show()
            viewModel.isDatabaseUnavailable ->
                Toast.makeText(requireContext(), R.string.external_database_unavailable, Toast.LENGTH_SHORT).show()
            viewModel.isNoDataFound ->
                Toast.makeText(requireContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show()
        }

        binding.profileProgressBar.visibility = ProgressBar.GONE
        binding.profile = profile
    }

    private fun welcomeUser() {

        val toastWelcomeText: String = when (KmlApp.loginId) {
            KmlApp.MARTA_ID -> "<3 Dzień dobry Muniu! <3"
            KmlApp.SEBASTIAN_ID -> "Dzień dobry Prezesie!"
            else -> "Dzień dobry " + KmlApp.firstName + "!"
        }
        Snackbar.make(requireActivity().findViewById(android.R.id.content), toastWelcomeText, Snackbar.LENGTH_SHORT).show()
        LoginScreen.isLogNow = false
    }

    private fun setUserIdentity(firstName: String, lastName: String) {
        KmlApp.firstName = firstName
        KmlApp.lastName = lastName
    }

    private fun openGalleryForImage() {
        val imageIntent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imageIntent.action = Intent.ACTION_OPEN_DOCUMENT
        } else imageIntent.action = Intent.ACTION_PICK
        imageIntent.type = "image/*"
        startActivityForResult(imageIntent, PICK_IMAGE_RESULT)
    }

    private fun showDialogToChangePass() {
        dialogChangePass = Dialog(requireContext())
        dialogChangePass.setContentView(R.layout.dialog_change_pass)
        dialogChangePass.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogChangePass.show()
        editTextOldPassword = dialogChangePass.findViewById(R.id.dialog_old_password)
        editTextNewPassword = dialogChangePass.findViewById(R.id.dialog_new_password)
        val btnAccept = dialogChangePass.findViewById<Button>(R.id.btn_dialog_change_pass_accept)
        val btnCancel = dialogChangePass.findViewById<Button>(R.id.btn_dialog_change_pass_cancel)
        btnAccept.setOnClickListener {
            changePass(editTextOldPassword.text.toString(), editTextNewPassword.text.toString())
            editTextOldPassword.setText("")
            editTextNewPassword.setText("")
        }
        btnCancel.setOnClickListener { dialogChangePass.dismiss() }
    }

    private fun changePass(oldPassword: String, newPassword: String) {
        val validationResult = viewModel.validatePassword(oldPassword, newPassword)
        if (validationResult != ProfileViewModel.VALIDATION_SUCCESSFUL) {
            Toast.makeText(requireContext(), validationResult, Toast.LENGTH_SHORT).show()
            return
        }

        val result = viewModel.resolvePasswordChanging(newPassword, oldPassword)

        if (result == DbChangePass.CHANGE_SUCCESSFUL) {
            dialogChangePass.dismiss()
        }

        Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_RESULT && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
        }
        if (data != null) viewModel.saveProfilePhoto(data.data.toString())
    }

    override fun onResume() {
        restoreProfilePhoto()
        super.onResume()
    }

    private fun restoreProfilePhoto() {
        val path = viewModel.getProfilePhotoPath()
        if (path.isNotEmpty()) {
            val photoUri = Uri.parse(path)
            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                try {
                    binding.profilePhoto.setImageURI(photoUri)
                } catch (e: Exception) {
                    Log.e("PERMISSION_ERROR", "onResume: " + e.message)
                    viewModel.saveProfilePhoto("") //clear state
                }
            }, 300)
        }
    }
}