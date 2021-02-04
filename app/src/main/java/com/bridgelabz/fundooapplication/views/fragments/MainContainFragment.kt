package com.bridgelabz.fundooapplication.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.viewModel.MainContainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainContainFragment : Fragment(), View.OnClickListener{

    lateinit var fabButton :FloatingActionButton

    private lateinit var viewModel: MainContainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_contain_fragment, container, false)
        return view
    }

    override fun onClick(v: View?) {
    }
}