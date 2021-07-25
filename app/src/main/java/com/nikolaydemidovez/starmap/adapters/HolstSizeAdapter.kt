package adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.graphics.*
import android.view.*
import android.widget.*
import androidx.core.view.updatePadding
import com.nikolaydemidovez.starmap.R
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.databinding.HolstSizeItemBinding
import com.nikolaydemidovez.starmap.pojo.Holst
import com.nikolaydemidovez.starmap.utils.helpers.Helper

class HolstSizeAdapter(
    private val templateHolst: MutableLiveData<*>,
    private val listener: (holst: Holst) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var sizeList = arrayListOf<Holst>()

    class HolstSizeHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = HolstSizeItemBinding.bind(item)

        fun bind(holst: Holst, position: Int, templateHolst: MutableLiveData<Holst>, listener: (holst: Holst) -> Unit) = with(binding) {
            if(position == 0) {
                rootItem.updatePadding(left = Helper.dpToPx(16F, itemView.context))
            }

            val borderResId = if(templateHolst.value!!.width == holst.width) {
                R.drawable.border_rect_recycler_item_selected
            } else {
                R.drawable.border_rect_recycler_item
            }

            backgroundLayout.background = ContextCompat.getDrawable(itemView.context, borderResId)

            titleHolst.text    = holst.title
            subTitleHolst.text = holst.subTitle

            rootItem.setOnClickListener {
                listener(holst)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.holst_size_item, viewGroup, false)
        return HolstSizeHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as HolstSizeHolder).bind(sizeList[position], position, templateHolst as MutableLiveData<Holst>, listener)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    fun addAllSizeList(list: ArrayList<Holst>) {
        sizeList.addAll(list)

        notifyDataSetChanged()
    }
}