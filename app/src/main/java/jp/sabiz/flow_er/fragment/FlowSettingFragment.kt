package jp.sabiz.flow_er.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import jp.sabiz.flow_er.R
import jp.sabiz.flow_er.databinding.FlowSettingFragmentBinding
import jp.sabiz.flow_er.databinding.TimerFragmentBinding
import jp.sabiz.flow_er.flowitem.FlowItemRecyclerViewAdapter
import jp.sabiz.flow_er.flowitem.db.FlowDatabase
import jp.sabiz.flow_er.viewmodel.FlowSettingViewModel
import jp.sabiz.flow_er.viewmodel.TimerViewModel

class FlowSettingFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FlowSettingFragment()
    }

    private var _binding: FlowSettingFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(this)[FlowSettingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.flow_setting_fragment ,container, false)
        with(binding.flowList) {
            layoutManager = LinearLayoutManager(context)
            adapter = FlowItemRecyclerViewAdapter(viewModel, viewLifecycleOwner)
        }

        binding.fabAddItem.setOnClickListener {
            viewModel.addNewFlowItem {
                binding.flowList.smoothScrollToPosition(viewModel.flowList.value?.size ?: 0)
            }

        }

        return binding.root
    }
}