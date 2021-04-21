package com.kml.views.fragments.mainFeatures

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.R
import com.kml.data.app.KmlApp
import com.kml.data.utilities.FileFactory
import com.kml.databinding.FragmentProfileBinding
import com.kml.extensions.showSnackBar
import com.kml.extensions.showToast
import com.kml.extensions.toBitmap
import com.kml.models.Profile
import com.kml.viewModelFactories.ProfileViewModelFactory
import com.kml.viewModels.ProfileViewModel
import com.kml.views.BaseFragment
import com.kml.views.activities.LoginScreen
import com.kml.views.dialogs.ChangePassDialog
import kotlin.math.roundToInt


class ProfileFragment : BaseFragment() {

    companion object {
        const val PICK_IMAGE_RESULT = 1
        const val PHOTO_WIDTH = 480
    }

    private lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentProfileBinding
    private lateinit var dataFile: FileFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataFile = FileFactory(requireContext())
        val viewModelFactory = ProfileViewModelFactory(dataFile)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.profileProgressBar.visibility = ProgressBar.VISIBLE
        viewModel.profileData.observe(viewLifecycleOwner) {
            launchProfile(it)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.profilePhoto.setOnClickListener {
            openGalleryForImage()
        }
        binding.changePass.setOnClickListener { showDialogToChangePass() }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshProfile()
        }
        restoreProfilePhoto()
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
                showToast(R.string.load_previous_data)
            viewModel.isDatabaseUnavailable ->
                showToast(R.string.external_database_unavailable)
            viewModel.isNoDataFound ->
                showToast(R.string.something_wrong)
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
        showSnackBar(toastWelcomeText, 1200)

        LoginScreen.isLogNow = false
    }

    private fun setUserIdentity(firstName: String, lastName: String) {
        KmlApp.firstName = firstName
        KmlApp.lastName = lastName
    }

    private fun openGalleryForImage() {
        val imageIntent = Intent()
        imageIntent.action = Intent.ACTION_OPEN_DOCUMENT
        imageIntent.type = "image/*"
        startActivityForResult(imageIntent, PICK_IMAGE_RESULT)
    }

    private fun showDialogToChangePass() {
        val dialog = ChangePassDialog(viewModel)
        dialog.show(parentFragmentManager, "ChangePassword")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_RESULT && resultCode == Activity.RESULT_OK && data != null) {
            data.data?.toBitmap(requireContext())?.let {
                val scaledBitmap = scaleProfilePhoto(it)
                viewModel.saveProfilePhoto(scaledBitmap)
                        .invokeOnCompletion {
                            activity?.runOnUiThread {
                                setProfilePhoto(scaledBitmap)
                            }
                        }
            }
        }
    }

    private fun restoreProfilePhoto() {
        viewModel.getProfilePhoto() { bitmap ->
            if (bitmap != null) {
                setProfilePhoto(bitmap)
            }
        }
    }

    private fun scaleProfilePhoto(bitmap: Bitmap): Bitmap {
        val aspectRatio: Float = bitmap.width / bitmap.height.toFloat()
        val height = (PHOTO_WIDTH / aspectRatio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, PHOTO_WIDTH, height, false)
    }

    private fun setProfilePhoto(bitmap: Bitmap) {
        binding.profilePhoto.setImageBitmap(bitmap)
    }
}