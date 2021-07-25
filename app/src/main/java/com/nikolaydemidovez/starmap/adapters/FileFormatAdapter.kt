package adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.core.view.updatePadding
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FormatFileItemBinding
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import java.util.*
import kotlin.collections.ArrayList

class FileFormatAdapter(
    private val currentFormat: MutableLiveData<String>,
    private val listener: (format: String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var formatList = arrayListOf<String>()

    class FileFormatHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = FormatFileItemBinding.bind(item)

        fun bind(format: String, position: Int, currentFormat: MutableLiveData<String>, listener: (format: String) -> Unit) = with(binding) {
            if(position == 0) {
                rootItem.updatePadding(left = Helper.dpToPx(16F, itemView.context))
            }

            val borderResId = if(currentFormat.value == format) {
                R.drawable.border_rect_recycler_item_selected
            } else {
                R.drawable.border_rect_recycler_item
            }

            backgroundLayout.background = ContextCompat.getDrawable(itemView.context, borderResId)

            title.text  = format.uppercase(Locale.getDefault())

            rootItem.setOnClickListener {
                listener(format)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.format_file_item, viewGroup, false)
        return FileFormatHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as FileFormatHolder).bind(formatList[position], position, currentFormat, listener)
    }

    override fun getItemCount(): Int {
        return formatList.size
    }

    fun addAllFormatList(list: ArrayList<String>) {
        formatList.addAll(list)

        notifyDataSetChanged()
    }
}