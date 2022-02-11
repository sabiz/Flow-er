package jp.sabiz.flow_er.flowitem

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import jp.sabiz.flow_er.databinding.FlowItemBinding
import jp.sabiz.flow_er.viewmodel.FlowSettingViewModel

class FlowItemRecyclerViewAdapter(private val viewModel: FlowSettingViewModel, private val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<FlowItemRecyclerViewAdapter.ViewHolder>() {

    init {
        viewModel.flowList.observe(viewLifecycleOwner) {
            notifyDataSetChanged()
        }
        // TODO リストの編集手段の実装（編集、並び替え）
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FlowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.flowList.value?.getOrNull(position)?: return
        holder.bind(item, viewLifecycleOwner)
    }

    override fun getItemCount(): Int = viewModel.flowList.value?.size ?: 0

    inner class ViewHolder(private val binding: FlowItemBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FlowItem, viewLifecycleOwner: LifecycleOwner) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                flowItem = item
                slided = false
                executePendingBindings()
            }

            binding.root.setOnLongClickListener {
                binding.slided = true
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                if (binding.slided == true) {
                    binding.slided = false
                }
            }
            binding.imageDelete.setOnClickListener {
                MaterialDialog(binding.root.context).show {
                    message(text = "Delete it ?")
                    positiveButton(text = "YES") {
                        viewModel.deleteItem(item)
                    }
                    negativeButton(text = "NO")
                    onDismiss {
                        binding.slided = false
                    }
                }
            }
        }
    }

}