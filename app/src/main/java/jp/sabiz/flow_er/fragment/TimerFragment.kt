package jp.sabiz.flow_er.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.sabiz.flow_er.R
import jp.sabiz.flow_er.viewmodel.TimerViewModel
import jp.sabiz.flow_er.databinding.TimerFragmentBinding
import jp.sabiz.flow_er.timer.CountDownTimer

class TimerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment()
    }

    private var _binding: TimerFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[TimerViewModel::class.java]
    }
    private val countDownTimer by lazy {
        CountDownTimer(10UL, viewModel)
    }
    private val timerSequence = mutableListOf(120UL, 25UL, 40UL, 25UL, 40UL)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // TODO シーケンスとタイマー表示の実装
        _binding = DataBindingUtil.inflate(inflater, R.layout.timer_fragment ,container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.timer = viewModel
        binding.textSetting.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.addToBackStack(TimerFragment.toString())
            transaction.replace(android.R.id.content, FlowSettingFragment.newInstance())
            transaction.commit()
        }
        binding.buttonStart.setOnClickListener {
            viewModel.state.value = State.RUNNING
        }
        binding.buttonPause.setOnClickListener {
            viewModel.state.value = State.PAUSE
        }
        setupObserve()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObserve() {
        viewModel.state.observe(viewLifecycleOwner){
            when(it) {
                State.RUNNING -> {
                    countDownTimer.start(this::onTick)
                }
                State.PAUSE -> {
                    countDownTimer.stop()
                }
                else -> {

                }
            }
        }
    }

    private fun onTick(currentTimeMin: ULong, currentTimeSec: ULong) {
        viewModel.currentMin.value = currentTimeMin
        viewModel.currentSec.value = currentTimeSec
        val time = CountDownTimer.toTime(currentTimeMin, currentTimeSec)
        viewModel.progress.value = (countDownTimer.time - time).toFloat() / countDownTimer.time.toFloat()
        if (currentTimeMin == 0UL && currentTimeSec == 0UL) {
            val nextTimer = timerSequence.removeFirstOrNull()
            if (nextTimer == null) {
                viewModel.state.value = State.STAND_BY
                return
            }
            countDownTimer.time = nextTimer
            countDownTimer.start(this::onTick)
        }
    }

    enum class State {
        STAND_BY,
        RUNNING,
        PAUSE
    }

}