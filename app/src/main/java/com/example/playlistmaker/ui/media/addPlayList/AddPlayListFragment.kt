package com.example.playlistmaker.ui.media.addPlayList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlayListBinding
import com.example.playlistmaker.domain.models.PlayList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddPlayListFragment : Fragment() {

    private lateinit var binding: FragmentAddPlayListBinding
    private val viewModel: AddPlayListViewModel by viewModel()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            Glide.with(this)
                .load(it.toString())
                .placeholder(R.drawable.placeholder_playlist)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelOffset(R.dimen.dimen8dp))
                )
                .into(binding.playlistImage)

            viewModel.addImage(it.toString())
        } else {
            Log.d("PhotoPicker", R.string.playlist_image_description.toString())
        }
    }

    private val request = PermissionRequester.instance()

    private var tempPlayList = PlayList(tracks = ArrayList())
    private lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(R.string.playlist_dialog_title)
            .setMessage(R.string.playlist_dialog_message)
            .setNeutralButton(R.string.playlist_dialog_cancel) { _, _ -> }
            .setPositiveButton(R.string.playlist_dialog_confirm) { _, _ ->
                navigateBack()
            }


        viewModel.observerPlayListState().observe(viewLifecycleOwner) { playList ->
            tempPlayList = playList
        }

        binding.toolBar.setNavigationOnClickListener {
            showDialog(tempPlayList)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showDialog(tempPlayList)
                }
            }
        )


        binding.playlistImage.setOnClickListener {
            if (checkPermissions()) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                lifecycleScope.launch {
                    request.request(getPermissionType()).collect { result ->
                        when (result) {
                            is PermissionResult.Granted -> {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }

                            is PermissionResult.Denied.DeniedPermanently -> {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.data =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                requireContext().startActivity(intent)
                            }

                            is PermissionResult.Denied.NeedsRationale -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.rationale_permission),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is PermissionResult.Cancelled -> {
                                return@collect
                            }
                        }
                    }
                }
            }
        }

        binding.inputEdittextName.addTextChangedListener(getTextWatcherForName())
        binding.inputEdittextDescription.addTextChangedListener(getTextWatcherForDescription())

        binding.createButton.setOnClickListener {
            lifecycleScope.launch { viewModel.savePlaylist() }

            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, binding.inputEdittextName.text.toString()),
                Toast.LENGTH_SHORT
            ).show()
            navigateBack()
        }
    }


private fun showDialog(playlist: PlayList) {
    if ((playlist.name == null || playlist.name == "") &&
        (playlist.description == null || playlist.description == "") &&
        (playlist.uri == null || playlist.uri == "")) {
        navigateBack()
    } else {
        confirmDialog.show()
    }
}

private fun navigateBack() {
    if (parentAudioPlayer) {
        parentFragmentManager.popBackStack()
        parentAudioPlayer = false
        requireActivity().findViewById<ConstraintLayout>(R.id.audioPlayerMainScreen).isVisible = true
    }
    else {
        findNavController().popBackStack()
    }
}

private fun checkPermissions(): Boolean {
    val permissionProvided = ContextCompat.checkSelfPermission(
        requireContext(),
        getPermissionType()
    )
    return permissionProvided == PackageManager.PERMISSION_GRANTED
}

private fun getPermissionType(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
    else Manifest.permission.READ_EXTERNAL_STORAGE
}

private fun getTextWatcherForName(): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.addName(s.toString())
            binding.createButton.isEnabled = s?.isNotEmpty() == true
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}

private fun getTextWatcherForDescription(): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.addDescription(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }
}

companion object {
    private var parentAudioPlayer = false
    fun newInstance(parent: Boolean) : AddPlayListFragment {
        parentAudioPlayer = parent
        return AddPlayListFragment()
    }
}
}



