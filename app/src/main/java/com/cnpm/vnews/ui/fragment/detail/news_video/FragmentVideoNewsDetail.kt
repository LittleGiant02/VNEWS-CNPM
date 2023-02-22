package com.cnpm.vnews.ui.fragment.detail.news_video

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cnpm.cnpm_vnews.viewmodel.NewsViewModel
import com.cnpm.vnews.R
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.FragmentNewsDetailBinding
import com.cnpm.vnews.model.VideoDetailModel
import com.cnpm.vnews.ui.adapter.list.AdapterListNewsVideoSmallVer
import com.cnpm.vnews.ui.adapter.list.AdapterRelated
import com.cnpm.vnews.viewmodel.VideoViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FragmentVideoNewsDetail(private val id: String) : Fragment() {
    companion object {
        private fun newInstance(id: String) = FragmentVideoNewsDetail(id)
        fun openWith(fragmentManager: FragmentManager, id: String) {
            fragmentManager.beginTransaction()
                .replace(
                    R.id.frame_child,
                    newInstance(id), "news"
                )
                .addToBackStack(null).commit()
        }
    }

    private lateinit var binding: FragmentNewsDetailBinding
    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var buttonFullScreen: ImageView
    private lateinit var buttonOption: ImageView
    private lateinit var buttonPlay: ImageView
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var buttonReplay5: ImageView
    private lateinit var buttonForward5: ImageView

    private lateinit var trackSelector: DefaultTrackSelector
    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var curRes = "Auto"
    private var dialogFullScreen: Dialog? = null
    private var isFullScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back()
        getDataDetail()
    }

    private fun back() {
        binding.sectionAppBar.buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        //back press
        backPressDispatcher(requireActivity(), viewLifecycleOwner)
    }

    private fun getDataDetail() {
        newsViewModel.getNewsMediaWithCheck(id).observe(viewLifecycleOwner) { haveResult ->
            if (haveResult) {//video
                val newsMedia = newsViewModel.videoDetailModel.value!!
                Log.e("path", "${newsMedia.path}")
                if (!newsMedia.path!!.endsWith("/")) {
                    binding.imageViewNewsDetail.visibility = View.GONE
                    initPlayer(newsMedia.path)
                    playerButtonClick(simpleExoPlayer!!)
                } else {
                    binding.playerView.visibility = View.GONE
                    Glide.with(binding.root).load(getUrlImageForType(newsMedia.image!!, "lg"))
                        .apply(optionsLoadImage)
                        .into(binding.imageViewNewsDetail)
                }
                binding.imageViewShare.setOnClickListener {
                    shareLink(
                        requireContext(),
                        getValueMetadataForName(newsMedia.metadata!!, Slug),
                        newsMedia.title!!
                    )
                }
                val title = newsMedia.title ?: "VNews"
                binding.textViewName.text = title
                binding.sectionAppBar.textViewTitle.text = title
                binding.textViewCategory.text =
                    getValueMetadataForName(newsMedia.metadata!!, DVBCategories)
                if (newsMedia.description?.isNotEmpty() == true) {
                    binding.textViewDescription.text = newsMedia.description
                } else {
                    binding.textViewDescription.visibility = View.GONE
                }
                binding.textViewTimeAgo.text = getTimeAgo(newsMedia.schedule!!, resources)
                if (newsMedia.body?.isNotEmpty() == true) {
                    setupDataToWebView(newsMedia.body)
                }
                binding.textViewAuthor.text = getValueMetadataForName(newsMedia.metadata, "Author")
                setNewsRelated(newsMedia)
                binding.layoutProgressbar.visibility = View.GONE
                binding.scrollviewNews.visibility = View.VISIBLE
                binding.textViewError.visibility = View.GONE
            } else {
                binding.textViewError.visibility = View.VISIBLE
                binding.layoutProgressbar.visibility = View.GONE
                binding.scrollviewNews.visibility = View.GONE
                Toast.makeText(context, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPlayer(path: String) {
        trackSelector = DefaultTrackSelector(requireContext())
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd().setMaxVideoBitrate(
                Int.MAX_VALUE
            )
        )
        simpleExoPlayer =
            SimpleExoPlayer.Builder(requireContext()).setTrackSelector(trackSelector).build()
        binding.playerView.player = simpleExoPlayer
        binding.playerView.keepScreenOn = true
        val mediaItem: MediaItem = MediaItem.fromUri(path)
        simpleExoPlayer!!.setMediaItem(mediaItem)
        simpleExoPlayer!!.prepare()
        simpleExoPlayer!!.play()
        simpleExoPlayer!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    simpleExoPlayer!!.seekTo(0)
                    simpleExoPlayer!!.pause()
                }
                if (state == Player.STATE_BUFFERING) {
                    buttonPlay.visibility = View.INVISIBLE
                    progressBarLoading.visibility = View.VISIBLE
                }
                if (state == Player.STATE_READY) {
                    buttonPlay.visibility = View.VISIBLE
                    progressBarLoading.visibility = View.INVISIBLE
                    buttonPlay.setImageResource(R.drawable.ic_play)
                }
            }

            var isFirstTimePlay = true
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    buttonPlay.setImageResource(R.drawable.ic_pause)
                    if (isFirstTimePlay) {
                        lifecycleScope.launch {
                            var vl = 0.1f
                            while (vl <= 1f) {
                                simpleExoPlayer!!.volume = vl
                                vl += 0.15f
                                delay(400)
                            }
                            isFirstTimePlay = false
                        }
                    }
                } else {
                    buttonPlay.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }

    private fun playerButtonClick(player: SimpleExoPlayer) {
        buttonFullScreen = binding.playerView.findViewById(R.id.buttonFullScreen)!!
        buttonPlay = binding.playerView.findViewById(R.id.buttonPlay)!!
        progressBarLoading = binding.playerView.findViewById(R.id.progressBarLoading)
        buttonOption = binding.playerView.findViewById(R.id.buttonOption)
        buttonReplay5 = binding.playerView.findViewById(R.id.buttonReplay5)
        buttonForward5 = binding.playerView.findViewById(R.id.buttonForward5)
        buttonPlay.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
        buttonOption.setOnClickListener {
            qualitySelection()
        }
        buttonReplay5.setOnClickListener {
            player.seekTo(player.currentPosition - 5000)
            player.play()
        }
        buttonForward5.setOnClickListener {
            player.seekTo(player.currentPosition + 5000)
            player.play()
        }
        dialogFullScreen =
            object : Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
                override fun onBackPressed() {
                    if (isFullScreen) {
                        requireActivity().requestedOrientation =
                            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        closeFullscreenDialog()
                    }
                    super.onBackPressed()
                }
            }
        buttonFullScreen.setOnClickListener {
//            if (!isFullScreen) {
//                removePlayerLive(binding.playerView)
//                requireActivity().requestedOrientation =
//                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                openDialogFullscreen()
//            } else {
//                requireActivity().requestedOrientation =
//                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//                closeFullscreenDialog()
//            }
        }

    }

    private fun openDialogFullscreen() {
        binding.playerView.removeView(binding.playerView)
        dialogFullScreen!!.addContentView(
            binding.playerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        isFullScreen = true
        dialogFullScreen!!.show()
    }

    private fun qualitySelection() {
        try {
            val mappedTrackInfo = trackSelector.currentMappedTrackInfo
            var videoRenderer: Int?
            val listHeight = mutableListOf("Auto")
            val listWidth = mutableListOf("Auto")
            for (i in 0 until mappedTrackInfo!!.rendererCount) {
                if (isVideoRenderer(mappedTrackInfo, i)) {
                    videoRenderer = i
//                    val trackType = mappedTrackInfo.getRendererType(videoRenderer)
                    val trackGroupArray = mappedTrackInfo.getTrackGroups(videoRenderer)

                    for (groupIndex in 0 until trackGroupArray.length) {
                        for (trackIndex in 0 until trackGroupArray[groupIndex].length) {
//                            val trackName = DefaultTrackNameProvider(resources).getTrackName(
//                                trackGroupArray[groupIndex].getFormat(trackIndex)
//                            )
                            val height = trackGroupArray[groupIndex].getFormat(trackIndex).height
                            val width = trackGroupArray[groupIndex].getFormat(trackIndex).width
//                            val isTrackSupported = mappedTrackInfo.getTrackSupport(
//                                videoRenderer,
//                                groupIndex,
//                                trackIndex
//                            ) == RendererCapabilities.FORMAT_HANDLED
                            Log.d(
                                "resNew", "h"
                            )
                            listHeight.add(height.toString() + "p")
                            listWidth.add(width.toString() + "p")
                        }
                    }
                }
            }
            val bottomSheet = SheetQualityFragment(listWidth, listHeight, trackSelector, curRes)
            bottomSheet.show(childFragmentManager, "bottomSheet")
            bottomSheet.onSelected = {
                curRes = it
            }
        } catch (e: NullPointerException) {
        }
    }

    fun closeFullscreenDialog() {
        (binding.playerView.parent as? ViewGroup)?.removeView(binding.playerView)
        binding.frameLayoutPlayer.addView(binding.playerView)
        isFullScreen = false
        dialogFullScreen!!.dismiss()
    }

    private fun removePlayerLive(playerView: PlayerView?) {
        val parent = playerView!!.parent as? ViewGroup ?: return
        val index = parent.indexOfChild(playerView)
        if (index >= 0) {
            parent.removeViewAt(index)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupDataToWebView(body: String) {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            val html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta http-equiv="X-UA-Compatible" content="IE=edge">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
                        <title>Document</title>
                    </head>
                    <body>
                       $body
                    <style>
                             body{
                                 padding-left:16px;
                                 padding-right:16px
                             }
                             .entry-body{
                                 font-size: 14px;
                                 text-align: justify;
                             }
                             h3{
                                 font-size: 14px;
                                 font-weight: 400;
                                 font-family: sans-serif;

                             }
                             .VCSortableInPreviewMode div{
                                 position: relative;
                                 margin-left: -16px; margin-right: -16px
                             }
                             p{
                                 text-align: justify;
                                 font-family: sans-serif;

                             }
                             .name-n-quote p{
                                 padding-left:16px;
                                 padding-right:16px
                             }
                             td p{
                                 padding-left:16px;
                                 padding-right:16px;
                                 font-weight: 400;
                                 font-family: sans-serif;
                             }
                         </style>
                       
                        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
                    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
                    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                    <script>
                        $(document).ready(function(){
                            $( "img" ).addClass( "img-fluid" );
                            });
                        
                    </script>
                    </body>
                    </html>
"""
            val encodedHtml: String =
                Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)

            loadData(
                encodedHtml,
                "text/html", "base64"
            )
        }
    }

    private fun setNewsRelated(newsMedia: VideoDetailModel) {
        //same
        val videoViewModel: VideoViewModel by viewModels()
        val listPrivateKeySameCate = newsMedia.keyword!!.split(",")
        videoViewModel.menuVideoViewModel(listPrivateKeySameCate[0]).observe(viewLifecycleOwner) {
            binding.recyclerViewSame.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            val adapter = AdapterListNewsVideoSmallVer(it.media!!.size, it.media)
            binding.recyclerViewSame.adapter = adapter
            adapter.itemClickListener = { media ->
                requireActivity().supportFragmentManager.popBackStack()
                openWith(
                    requireActivity().supportFragmentManager,
                    media.id!!
                )
            }
        }
        //related
        val jsonRelated = getValueMetadataForName(newsMedia.metadata!!, "Related")
        if (jsonRelated != "VNews" || jsonRelated.isNotEmpty()) {
            val listIdMediaRelated = mutableListOf<String>()
            var jsonObject: JSONObject
            try {
                val jsonArray = JSONArray(jsonRelated)
                for (vt in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(vt)
                    val id: String = jsonObject.getString("ID")
                    listIdMediaRelated.add(id)
                }
            } catch (e: JSONException) {
                Log.e("error news detail", e.printStackTrace().toString())
            }

            for (s in listIdMediaRelated) {
                Log.e("related", s.toString())
            }
            if (listIdMediaRelated.isNotEmpty()) {
                newsViewModel.getListMediaRelatedDetail(listIdMediaRelated)
                    .observe(viewLifecycleOwner) { listNewsRelated ->
                        binding.viewRelated.visibility = View.VISIBLE
                        binding.tvRelated.visibility = View.VISIBLE
                        binding.recyclerViewRelated.visibility = View.VISIBLE
                        binding.recyclerViewRelated.layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )

                        val size = if (listNewsRelated.size >= 6) {
                            6
                        } else {
                            listNewsRelated.size
                        }
                        val adapter = AdapterRelated(
                            size, listNewsRelated
                        )
                        adapter.itemClickListener = { id ->
                            requireActivity().supportFragmentManager.popBackStack()
                            openWith(
                                requireActivity().supportFragmentManager,
                                id,
                            )
                        }
                        binding.recyclerViewRelated.adapter = adapter
                    }
            } else {
                binding.viewRelated.visibility = View.GONE
                binding.tvRelated.visibility = View.GONE
                binding.recyclerViewRelated.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer?.release()
//        val timeEnd = System.currentTimeMillis()

//        currentNativeAd?.destroy()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.pause()
    }


    var job: Job? = null
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        simpleExoPlayer!!.playWhenReady = menuVisible

        if (menuVisible) {

            job = lifecycleScope.launch {
                delay(2000)
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
            }

        } else {
            job?.cancel()
            requireActivity().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        }
        Log.i(
            "pagerIsVisi",
            "Live Frag is: $menuVisible" + " or : " + requireActivity().requestedOrientation
        )
    }


}