package com.example.mymusicapp.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymusicapp.callback.SongItemListener
import com.example.mymusicapp.common.AppCommon
import com.example.mymusicapp.databinding.FragmentHomeBinding
import com.example.mymusicapp.presentation.activity.SongActivity
import com.example.mymusicapp.presentation.adapter.SongAdapter
import com.example.mymusicapp.presentation.viewmodel.MainViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val songAdapter by lazy {
        SongAdapter(requireContext(), object : SongItemListener {
            override fun onItemClicked(uri: Uri) {
                var position = AppCommon.INVALID_VALUE
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerViews()
        dataBinding()
        addEvents()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }


    private fun setData() {
    }


    private fun addEvents() {
        binding.apply {
            searchEditText.apply {
                setOnQueryTextFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        cardView.visibility = View.GONE
                    } else {
                        cardView.visibility = View.VISIBLE
                    }
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }
            btnNextSong.setOnClickListener {
                startActivity(Intent(activity, SongActivity::class.java))
            }
        }
    }

    private fun dataBinding() {
    }

    private fun prepareRecyclerViews() {
        binding.songRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = songAdapter
        }
    }
}