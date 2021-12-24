package jp.sabiz.flow_er.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.sabiz.flow_er.R
import jp.sabiz.flow_er.flowitem.FlowItemRecyclerViewAdapter
import jp.sabiz.flow_er.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class FlowSettingFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FlowSettingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.flow_setting_fragment, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = FlowItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }
}