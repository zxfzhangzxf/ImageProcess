package com.android.imageprocess.ui.camalb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.imageprocess.R
import com.android.imageprocess.databinding.FragmentBackgroundBinding
import com.android.imageprocess.logic.model.Background
import com.android.imageprocess.ui.login.ImageViewModel

class BackgroundFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(ImageViewModel::class.java) }

    private lateinit var adapter: BackgroundAdapter

    private var _binding : FragmentBackgroundBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_background,container,false)
        _binding = FragmentBackgroundBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBackgrounds()
        val layoutManager = LinearLayoutManager(activity)
        binding.rvBackground.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        adapter = BackgroundAdapter(this,viewModel.backgroundList)
        binding.rvBackground.adapter = adapter
    }

    private fun initBackgrounds() {
        viewModel.backgroundList.clear()
        val backgrounds = mutableListOf(Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),
            Background("淡黄",R.drawable.ic_camera_alt_deep_purple_600_24dp),)

        viewModel.backgroundList.addAll(backgrounds)
    }
}