package com.cnpm.vnews.ui.fragment.live

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnpm.cnpm_vnews.viewmodel.LiveViewModel
import com.cnpm.vnews.R
import com.cnpm.vnews.const_util.*
import com.cnpm.vnews.databinding.FragmentLiveBinding
import com.cnpm.vnews.model.EPGModel
import com.cnpm.vnews.ui.adapter.list.AdapterEpg
import com.cnpm.vnews.ui.fragment.detail.news_video.SheetQualityFragment
import com.cnpm.vnews.viewmodel.VideoViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTimeBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FragmentLive : Fragment() {
    companion object {
        fun newInstance() = FragmentLive()
    }

    private lateinit var binding: FragmentLiveBinding
    private val videoViewModel: VideoViewModel by viewModels()
    private val liveViewModel: LiveViewModel by viewModels()

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var buttonPlay: ImageView
    private lateinit var buttonFullScreen: ImageView
    private lateinit var buttonOption: ImageView
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var currentPositionVideo: TextView
    private lateinit var durationVideo: TextView
    private lateinit var defaultTimeBar: DefaultTimeBar
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var buttonReplay5: ImageView
    private lateinit var buttonForward5: ImageView
    private var currentQuality = "Auto"
    var job: Job? = null

    private var positionLive: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlayer()
        setUpVideoLive()
        setUpLayout()
        playerButtonClick()
    }

    private fun initPlayer() {
        trackSelector = DefaultTrackSelector(requireContext())

        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd()
                .setMaxVideoBitrate(Int.MAX_VALUE)
        )

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector).build()
        binding.playerLive.player = simpleExoPlayer
        binding.playerLive.keepScreenOn = true
        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    simpleExoPlayer!!.pause()
                    simpleExoPlayer!!.seekTo(0)
                }
                if (state == Player.STATE_BUFFERING) {
                    buttonPlay.visibility = View.INVISIBLE
                    progressBarLoading.visibility = View.VISIBLE
                }
                if (state == Player.STATE_IDLE) {
                    setUpVideoLive()
                }
                if (state == Player.STATE_READY) {
                    buttonPlay.visibility = View.VISIBLE
                    progressBarLoading.visibility = View.INVISIBLE
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    buttonPlay.setImageResource(R.drawable.ic_pause)
                } else {
                    buttonPlay.setImageResource(R.drawable.ic_play)
                }
            }
        })
    }

    private fun setUpVideoLive() {
        videoViewModel.menuVideoViewModel(apiLiveScreen)
            .observe(viewLifecycleOwner) {
                currentPositionVideo.visibility = View.INVISIBLE
                durationVideo.visibility = View.INVISIBLE
                defaultTimeBar.visibility = View.INVISIBLE
                buttonForward5.visibility = View.INVISIBLE
                buttonReplay5.visibility = View.INVISIBLE

                val mediaItem: MediaItem =
                    MediaItem.fromUri(it.domain!!)
                simpleExoPlayer!!.setMediaItem(mediaItem)
                simpleExoPlayer!!.prepare()
//                simpleExoPlayer!!.play()
            }
    }

    private fun setUpLayout() {
        liveViewModel.epgViewModel(getDateNow()).observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val listEpg = liveViewModel.listEPGViewModel.value!!
                if (listEpg.isNotEmpty()) {
                    positionLive = getPositionLive(listEpg)

                    val layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

                    binding.textViewCurrentTitle.text = listEpg[positionLive].name!!
                    binding.textViewTitleLive.text = listEpg[positionLive].name!!
                    binding.textViewTimeStart.text =
                        listEpg[positionLive].startTime!!.substring(11, 16)
                    binding.textViewTimeEnd.text = listEpg[positionLive].endTime!!.substring(11, 16)

                    val adapterEpg = AdapterEpg(listEpg)
                    adapterEpg.itemClickListener = { epgModel, position ->
                        if (position == positionLive) {
                            setUpVideoLive()
                        }
                        binding.textViewCurrentTitle.text = epgModel.name!!
                    }
                    binding.textViewTitleLive.setOnClickListener {
                        layoutManager.scrollToPosition(positionLive)
                        setUpVideoLive()
                        binding.textViewCurrentTitle.text = listEpg[positionLive].name
                    }
                    binding.icLive.setOnClickListener {
                        layoutManager.scrollToPosition(positionLive)
                        setUpVideoLive()
                        binding.textViewCurrentTitle.text = listEpg[positionLive].name
                    }
                    binding.recyclerViewEpg.adapter = adapterEpg
                    binding.recyclerViewEpg.layoutManager = layoutManager
                    binding.recyclerViewEpg.setHasFixedSize(true)

                    binding.recyclerViewEpg.viewTreeObserver
                        .addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                layoutManager.scrollToPosition(positionLive)
                                binding.recyclerViewEpg.viewTreeObserver
                                    .removeOnGlobalLayoutListener(this)
                            }
                        })

                    videoViewModel.menuVideoViewModel(apiHot)
                        .observe(viewLifecycleOwner) { itHot ->
                            val listHot = itHot.media!!
                            videoViewModel.menuVideoViewModel(apiCategoryScreen)
                                .observe(viewLifecycleOwner) { menuModel ->
                                    val listProgram = menuModel.components!!
                                    val adapterLive =
                                        AdapterLive(listHot, listProgram.subList(0, 6))
                                    binding.recyclerViewLive.layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    binding.recyclerViewLive.adapter = adapterLive
                                    binding.progressBarLive.visibility = View.GONE
                                    binding.recyclerViewLive.visibility = View.VISIBLE
//                                    adapterLive.itemProgramClickListener = { privateKey ->
//                                        FragmentCategoryDetail.openWith(
//                                            requireActivity().supportFragmentManager,
//                                            privateKey
//                                        )
//                                    }
//                                    adapterLive.itemHotClickListener = { news ->
//                                        FragmentVideoNewsDetail.openWith(
//                                            requireActivity().supportFragmentManager,
//                                            news.id!!
//                                        )
//                                    }
                                }
                        }

                    playerNotLiveButtonClick(adapterEpg)

                } else {
                    binding.textViewTitleLive.setOnClickListener {
                        setUpVideoLive()
                    }
                }
            } else {
                val listEpg: List<EPGModel> = mutableListOf()
                val adapterEpg = AdapterEpg(listEpg)
                binding.recyclerViewEpg.adapter = adapterEpg
                playerNotLiveButtonClick(adapterEpg)
                setUpVideoLive()
            }
        }
    }

    private fun playerNotLiveButtonClick(adapterEpg: AdapterEpg) {
        adapterEpg.itemClickListener = { epgModel, position ->
            if (position != positionLive) {
                currentPositionVideo.visibility = View.VISIBLE
                durationVideo.visibility = View.VISIBLE
                defaultTimeBar.visibility = View.VISIBLE
                buttonForward5.visibility = View.VISIBLE
                buttonReplay5.visibility = View.VISIBLE
                binding.textViewCurrentTitle.text = epgModel.name!!
                val mediaItem: MediaItem =
                    MediaItem.fromUri(epgModel.url!!)
                simpleExoPlayer!!.setMediaItem(mediaItem)
                simpleExoPlayer!!.prepare()
                simpleExoPlayer!!.play()
            } else {
                setUpVideoLive()
            }

        }
    }

    private fun playerButtonClick() {
        buttonPlay = binding.playerLive.findViewById(R.id.buttonPlay)!!
        buttonFullScreen = binding.playerLive.findViewById(R.id.buttonFullScreen)!!
        buttonOption = binding.playerLive.findViewById(R.id.buttonOption)
        progressBarLoading = binding.playerLive.findViewById(R.id.progressBarLoading)
        currentPositionVideo = binding.playerLive.findViewById(R.id.exo_position)
        durationVideo = binding.playerLive.findViewById(R.id.exo_duration)
        defaultTimeBar = binding.playerLive.findViewById(R.id.exo_progress)
        buttonReplay5 = binding.playerLive.findViewById(R.id.buttonReplay5)
        buttonForward5 = binding.playerLive.findViewById(R.id.buttonForward5)
        buttonPlay.setOnClickListener {
            if (simpleExoPlayer!!.isPlaying) {
                simpleExoPlayer!!.pause()
            } else {
                simpleExoPlayer!!.play()
            }
        }
        buttonOption.setOnClickListener {
            qualitySelection()
        }
        buttonReplay5.setOnClickListener {
            simpleExoPlayer?.seekTo(simpleExoPlayer!!.currentPosition - 5000)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.play()
        }
        buttonForward5.setOnClickListener {
            simpleExoPlayer?.seekTo(simpleExoPlayer!!.currentPosition + 5000)
            simpleExoPlayer?.prepare()
            simpleExoPlayer?.play()
        }

        buttonFullScreen.setOnClickListener {
//            val orientation = resources.configuration.orientation
//            requireActivity().requestedOrientation =
//                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                } else {
//                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                }
        }

//        binding.btSelectedEPG.setOnClickListener {
//            val c = Calendar.getInstance()
//            val date = Date()
//            c.time = date
//            val year = c.get(Calendar.YEAR)
//            var month = (c.get(Calendar.MONTH))
//            month = if (month < 12) {
//                c.get(Calendar.MONTH)
//            } else {
//                1
//            }
//            val day = c.get(Calendar.DAY_OF_MONTH)
//            val dpd = DatePickerDialog(
//                requireContext(),
//                { _, yearSelected, monthOfYear, dayOfMonth ->
//                    val month = if (monthOfYear < 12) {
//                        monthOfYear + 1
//                    } else {
//                        1
//                    }
//                    var strDay = dayOfMonth.toString()
//                    var strMonth = month.toString()
//                    if (month < 10) {
//                        strMonth = "0$month"
//                    }
//                    if (dayOfMonth < 10) {
//                        strDay = "0$dayOfMonth"
//                    }
//
//                    val dateString = "${strDay}_${strMonth}_${yearSelected}"
//
//                    viewModelVideoLive.getEPGTvByBoolean(dateString)
//                        .observe(viewLifecycleOwner)
//                        { haveEpg ->
//                            if (haveEpg) {
//                                val list = viewModelVideoLive.listSchedude.value!!
//                                val posLive: Int
//                                if (adapterEpg != null) {
//                                    adapterEpg?.submitData(list)
//                                } else {
//                                    adapterEpg = ItemEPGAdapter(list)
//                                    binding.rvEPG.adapter = adapterEpg
//
//                                }
//                                try {
//                                    posLive = getEPGLive(list)
//                                    layoutManagerEPG.scrollToPosition(posLive)
//                                    setupVideoLive()
//                                    binding.tvTitleLive.text = list[posLive].name
//                                    clickInAdapterEpg(adapterEpg!!)
//                                } catch (e: IndexOutOfBoundsException) {
//
//                                }
//                            } else {
//                                val listEpg: List<EPGModel> = mutableListOf()
//                                adapterEpg = ItemEPGAdapter(listEpg)
//                                binding.rvEPG.adapter = adapterEpg
//                                clickInAdapterEpg(adapterEpg!!)
//                                setupVideoLive()
//                            }
//                        }
//                },
//                year,
//                month,
//                day
//            )
//            dpd.show()
//        }
//
//        //back press
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            val orientation = resources.configuration.orientation
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                requireActivity().requestedOrientation =
//                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            } else {
//                toolbar.visibility = View.VISIBLE
//                bottomNav.visibility = View.VISIBLE
//                requireActivity().finish()
//            }
//        }

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

                    val trackGroupArray = mappedTrackInfo.getTrackGroups(videoRenderer)

                    for (groupIndex in 0 until trackGroupArray.length) {
                        for (trackIndex in 0 until trackGroupArray[groupIndex].length) {

                            val height =
                                trackGroupArray[groupIndex].getFormat(trackIndex).height
                            val width = trackGroupArray[groupIndex].getFormat(trackIndex).width


                            listHeight.add(height.toString() + "p")
                            listWidth.add(width.toString() + "p")
                        }
                    }
                }
            }

            val bottomSheet =
                SheetQualityFragment(listWidth, listHeight, trackSelector, currentQuality)

            bottomSheet.show(childFragmentManager, "bottomSheet")

            bottomSheet.onSelected = {
                currentQuality = it
            }
            bottomSheet.onDismiss = {
                Log.i("ssss", "onViewCreated: " + false)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requireActivity().window.decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                }
            }

        } catch (e: NullPointerException) {
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateNow(): String {
        val dateFormatter = SimpleDateFormat("dd_MM_yyyy")
        val cal = Calendar.getInstance()
        return dateFormatter.format(cal.time)
    }

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

    override fun onPause() {
        super.onPause()
        simpleExoPlayer!!.pause()
    }


    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer!!.release()
    }
}