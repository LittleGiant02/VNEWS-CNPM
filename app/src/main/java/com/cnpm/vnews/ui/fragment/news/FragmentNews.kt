package com.cnpm.vnews.ui.fragment.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnpm.cnpm_vnews.viewmodel.NewsViewModel
import com.cnpm.vnews.const_util.apiNewsScreen
import com.cnpm.vnews.databinding.FragmentNewsBinding
import com.cnpm.vnews.model.MediaModel
import com.cnpm.vnews.ui.fragment.detail.news_video.FragmentVideoNewsDetail
import com.cnpm.vnews.viewmodel.VideoViewModel

class FragmentNews : Fragment() {
    companion object {
        fun newInstance() = FragmentNews()
    }

    private lateinit var binding: FragmentNewsBinding
    private val videoViewModel: VideoViewModel by viewModels()
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoViewModel.menuVideoViewModel(apiNewsScreen).observe(viewLifecycleOwner) { menuModel ->
            val listComponent = menuModel.components!!

            val listPrivateKey = mutableListOf<String>()
            val listTypePlaylistChild = mutableListOf<Long>()
            listComponent.forEach {
                listPrivateKey.add(it.privateKey!!)
                listTypePlaylistChild.add(it.layoutType!!.type!!)
            }
            listTypePlaylistChild.forEachIndexed { index, type ->
                when (type) {
                    //most
                    0L -> {
                        newsViewModel.mostViewModel(listPrivateKey[index])
                    }
                    //highlight
                    1L -> {
//                        newsViewModel.highlightAppViewModel(apiNewsScreenHighlightApp)
                        newsViewModel.highlightViewModel(listPrivateKey[index])
                    }
                    //newest
                    2L -> {
                        newsViewModel.newestViewModel(listPrivateKey[index])
                    }
                }
            }

            newsViewModel.mostMenuModel.observe(viewLifecycleOwner)
            { itMost ->
                newsViewModel.highlightMenuModel.observe(viewLifecycleOwner)
                { itHighlight ->
                    newsViewModel.newestMenuModel.observe(viewLifecycleOwner)
                    { itNewest ->
                        val listSlider = itHighlight.media!!.subList(0, 5)
                        val listGrid = itHighlight.media.subList(5, 7)
                        val listMost1 = itMost.media!!.subList(0, 5)
                        val itemLarge = itNewest.media!!.subList(0, 1)
                        val listMost2 = itNewest.media.subList(1, 11)
                        val size = if (itHighlight.media.size - 7 > 10) {
                            10
                        } else {
                            itHighlight.media.size
                        }
                        val listHighlight = itHighlight.media.subList(
                            7,
                            size
                        )
                        val adapter = AdapterNews(
                            listSlider,
                            listGrid,
                            listMost1,
                            itemLarge,
                            listMost2,
                            listHighlight as List<MediaModel>,
                        )
                        binding.recyclerViewNews.layoutManager =
                            LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        binding.recyclerViewNews.adapter = adapter
                        binding.progressBarNews.visibility = View.GONE
                        binding.recyclerViewNews.visibility = View.VISIBLE
                        adapter.itemClickListener = { news ->
                            FragmentVideoNewsDetail.openWith(
                                requireActivity().supportFragmentManager,
                                news.id!!
                            )
                        }
                    }
                }
            }
        }
    }
}