package com.cnpm.vnews.ui.fragment.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.cnpm.vnews.const_util.apiCategoryScreen
import com.cnpm.vnews.databinding.FragmentCategoryBinding
import com.cnpm.vnews.ui.adapter.list.AdapterGridCategory
import com.cnpm.vnews.ui.fragment.detail.category.FragmentCategoryDetail
import com.cnpm.vnews.viewmodel.VideoViewModel

class FragmentCategory : Fragment() {
    companion object {
        fun newInstance() = FragmentCategory()
    }

    private lateinit var binding: FragmentCategoryBinding
    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoViewModel.menuVideoViewModel(apiCategoryScreen).observe(viewLifecycleOwner)
        {
            val layoutManager = GridLayoutManager(
                context,
                2
            )
            binding.recyclerViewCategoryProgram.layoutManager = layoutManager
            val adapter = AdapterGridCategory(
                it.components!!
            )
            binding.recyclerViewCategoryProgram.adapter = adapter
            binding.progressBarCategory.visibility = View.INVISIBLE
            binding.recyclerViewCategoryProgram.visibility = View.VISIBLE
            adapter.itemClickListener = { privateKey ->
                FragmentCategoryDetail.openWith(
                    requireActivity().supportFragmentManager,
                    privateKey
                )
            }
        }
//        itemDAO = MediaModelDatabase.getInstance(requireActivity())?.itemDAO()!!
//               val layoutManager = LinearLayoutManager(
//            context,
//            RecyclerView.VERTICAL,
//            false
//        )
//        binding.recyclerViewSave.layoutManager = layoutManager
//        val adapter = AdapterListSave(itemDAO.selectAllNews())
//        binding.recyclerViewSave.adapter = adapter
//        binding.recyclerViewSave.visibility = View.VISIBLE
//        adapter.itemClickListener = { id ->
//            FragmentVideoNewsDetail.openWith(
//                requireActivity().supportFragmentManager,
//                id
//            )
//        }
//        adapter.itemUnSaveClickListener = { id ->
//            itemDAO.deleteMediaModelById(id)
//        }
    }
}