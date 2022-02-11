package jp.sabiz.flow_er.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.sabiz.flow_er.R
import jp.sabiz.flow_er.databinding.FlowSettingFragmentBinding
import jp.sabiz.flow_er.flowitem.FlowItemRecyclerViewAdapter
import jp.sabiz.flow_er.viewmodel.FlowSettingViewModel
import kotlin.math.max
import kotlin.math.min

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

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.bindingAdapterPosition
            val to = target.bindingAdapterPosition
            viewModel.moveItem(from, to) {
                binding.flowList.adapter?.notifyItemMoved(from, to)
            }
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        override fun isLongPressDragEnabled(): Boolean = false

    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.flow_setting_fragment ,container, false)
        with(binding.flowList) {
            layoutManager = LinearLayoutManager(context)
            itemTouchHelper.attachToRecyclerView(this)
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
            adapter = FlowItemRecyclerViewAdapter(viewModel, viewLifecycleOwner, itemTouchHelper)
        }

        binding.fabAddItem.setOnClickListener {
            viewModel.addNewFlowItem {
                val size = viewModel.flowList.value?.size ?: return@addNewFlowItem

                binding.flowList.smoothScrollToPosition(size)
                binding.flowList.adapter?.notifyItemInserted(size )
            }

        }

        return binding.root
    }
}