package jp.sabiz.flow_er.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import jp.sabiz.flow_er.R
import jp.sabiz.flow_er.databinding.TimerFragmentBinding
import jp.sabiz.flow_er.timer.CountDownTimer
import jp.sabiz.flow_er.timer.MinSec
import jp.sabiz.flow_er.viewmodel.TimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment()
        //TODO
        @JvmStatic
        val TIMER_SEQUENCE = listOf(MinSec.from(80UL),
                                    MinSec.from(25UL),
                                    MinSec.from(40UL),
                                    MinSec.from(25UL),
                                    MinSec.from(40UL))
    }

    private var _binding: TimerFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[TimerViewModel::class.java]
    }
    private val countDownTimer by lazy {
        CountDownTimer(10UL, viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        binding.buttonReset.setOnClickListener {
            viewModel.state.value = State.STAND_BY
            // TODO
            viewModel.timerSequence.value = TIMER_SEQUENCE.map { it }.toList()
            countDownTimer.time = 10UL
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
                State.STAND_BY -> {
                    viewModel.progress.value = 0F
                    // TODO
                    viewModel.timerSequence.value = TIMER_SEQUENCE.map { v -> v }.toList()
                    countDownTimer.time = 10UL
                }
                else -> {

                }
            }
        }
    }

    private fun onTick(remainingMinSec: MinSec, remainingAllMilliSec: ULong) {
        viewModel.currentMin.value = remainingMinSec.minutes
        viewModel.currentSec.value = remainingMinSec.seconds
        viewModel.progress.value = (countDownTimer.timeMilliSec - remainingAllMilliSec).toFloat() / countDownTimer.timeMilliSec.toFloat()
        if (remainingMinSec.minutes <= 0UL && remainingMinSec.seconds <= 0UL && remainingAllMilliSec <= 0UL) {
            val nextTimer = viewModel.timerSequence.value?.firstOrNull()
            if(viewModel.timerSequence.value != null && viewModel.timerSequence.value?.size ?: 0 >= 1) {
                viewModel.timerSequence.value = viewModel.timerSequence.value?.filterIndexed { index, _ -> index != 0 }
            }
            if (nextTimer == null) {
                viewModel.state.value = State.STAND_BY
                return
            }
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                countDownTimer.time = nextTimer.totalSeconds
                countDownTimer.start(this@TimerFragment::onTick)
            }
        }
    }

    enum class State {
        STAND_BY,
        RUNNING,
        PAUSE
    }

}