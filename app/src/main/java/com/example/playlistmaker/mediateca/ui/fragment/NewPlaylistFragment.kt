package com.example.playlistmaker.mediateca.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.R.dimen
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mediateca.ui.viewModel.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding

    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val viewModel: NewPlaylistViewModel by viewModel()

    private val backCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveButton.isClickable = false
        binding.saveButton.isEnabled = false
        binding.nameShape.isEnabled = false
        binding.descriptionShape.isEnabled = false



        binding.newPlaylistToolbars.setNavigationOnClickListener {
            backScreen()
        }
        getPreviw()
        binding.editName.addTextChangedListener(textWatcherName)
        binding.description.addTextChangedListener(textWatcherDescription)

    }


    private val textWatcherName = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.saveButton.isClickable = s?.isNotEmpty() == true
            binding.saveButton.isEnabled = s?.isNotEmpty() == true
            binding.nameShape.isEnabled = s?.isNotEmpty() == true
            binding.descriptionShape.isEnabled = s?.isNotEmpty() == true
            if (s?.isNotEmpty() == true) {
                binding.smallName.visibility = View.VISIBLE

            } else {
                binding.smallName.visibility = View.GONE

            }
        }
    }


    val textWatcherDescription = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s?.isNotEmpty() == true) {
                binding.smallDescription.visibility = View.VISIBLE
            } else {
                binding.smallDescription.visibility = View.GONE
            }
        }
    }

    private fun getPreviw() {
        var actualUri: Uri? = null
        var flag = false
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                actualUri = uri
                if (uri != null) {
                    flag = true
                    Glide.with(requireContext())
                        .load(uri)
                        .placeholder(R.drawable.ic_toast)
                        .fitCenter()
                        .transform(
                            RoundedCorners(this.resources.getDimensionPixelSize(dimen.cornerRadius_8))
                        )
                        .into(binding.addImage)
                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.saveButton.setOnClickListener {
            viewModel.savePlaylist(
                binding.editName.text.toString(),
                binding.description.text.toString(),
                actualUri
            )
            showMessage("Плейлист ${binding.editName.text} создан")
            findNavController().navigateUp()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setNeutralButton("Отмена") { dialog, which ->
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }



        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.editName.text.isNotEmpty() || binding.description.text.isNotEmpty() || flag)
                    confirmDialog.show()
                else
                    findNavController().navigateUp()
            }
        })
    }

    private fun showMessage(textInp: String) {
        val inflater = layoutInflater
        val container = requireActivity().findViewById<ViewGroup>(R.id.custom_toast_container)
        val layout: View = inflater.inflate(R.layout.toast_creat_playlist, container)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = textInp
        with(Toast(requireContext())) {
            setGravity(Gravity.BOTTOM, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {

        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "cover.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun backScreen()  {
        if (binding.addImage .background == null
            || binding.editName.text!!.isNotEmpty()
            || binding.description.text!!.isNotEmpty()
        ) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
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

}