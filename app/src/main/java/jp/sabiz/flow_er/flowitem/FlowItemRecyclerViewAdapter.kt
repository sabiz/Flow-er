package jp.sabiz.flow_er.flowitem

import android.annotation.SuppressLint
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import jp.sabiz.flow_er.databinding.FlowItemBinding
import jp.sabiz.flow_er.viewmodel.FlowSettingViewModel

class FlowItemRecyclerViewAdapter(
    private val viewModel: FlowSettingViewModel, private val viewLifecycleOwner: LifecycleOwner,
    private val itemTouchHelper: ItemTouchHelper) :RecyclerView.Adapter<FlowItemRecyclerViewAdapter.ViewHolder>() {


    init {
        viewModel.flowList.observe(viewLifecycleOwner) {
            notifyDataSetChanged()
        }
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

        @SuppressLint("CheckResult", "ClickableViewAccessibility")
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
                        viewModel.deleteItem(item) {
                            this@FlowItemRecyclerViewAdapter.notifyItemRangeChanged(0, itemCount + 1)
                        }
                    }
                    negativeButton(text = "NO")
                    onDismiss {
                        binding.slided = false
                    }
                }
            }
            binding.imageEdit.setOnClickListener {
                MaterialDialog(binding.root.context).show {
                    input(prefill = item.count.toString(), inputType = InputType.TYPE_CLASS_NUMBER,
                          maxLength = 3, waitForPositiveButton = false) { dialog, text ->
                        val longValue = text.toString().toLongOrNull() ?: 0
                        val inputField = dialog.getInputField()
                        inputField.error = if(longValue > 0) null else "Must input integer"
                        dialog.setActionButtonEnabled(WhichButton.POSITIVE, longValue > 0)
                    }
                    positiveButton {
                        val inputValue = it.getInputField().text.toString().toLongOrNull() ?: return@positiveButton
                        viewModel.updateItem(item, inputValue) {
                            this@FlowItemRecyclerViewAdapter.notifyItemRangeChanged(0, itemCount)
                        }
                    }
                    onDismiss {
                        binding.slided = false
                    }
                }
            }
            binding.imageHandle.setOnTouchListener { _, motionEvent ->
                if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper.startDrag(this)
                }
                return@setOnTouchListener false
            }
        }
    }

}