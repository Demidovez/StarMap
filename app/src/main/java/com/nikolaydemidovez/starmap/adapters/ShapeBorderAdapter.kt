package adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.core.view.updatePadding
import com.nikolaydemidovez.starmap.R
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.databinding.ShapeMapBorderItemBinding
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder
import com.nikolaydemidovez.starmap.pojo.StarMapBorder
import com.nikolaydemidovez.starmap.utils.helpers.Helper

class ShapeBorderAdapter(
    private val starMapBorder: MutableLiveData<*>,
    private val listener: (shape: ShapeMapBorder) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var sizeList = arrayListOf<ShapeMapBorder>()

    class ShapeBorderHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ShapeMapBorderItemBinding.bind(item)

        fun bind(shape: ShapeMapBorder, position: Int, starMapBorder: MutableLiveData<StarMapBorder>, listener: (shape: ShapeMapBorder) -> Unit) = with(binding) {
            if(position == 0) {
                rootItem.updatePadding(left = Helper.dpToPx(16F, itemView.context))
            }

            val borderResId = if(starMapBorder.value!!.shapeType == shape.type) {
                R.drawable.border_rect_recycler_item_selected
            } else {
                R.drawable.border_rect_recycler_item
            }

            backgroundLayout.background = ContextCompat.getDrawable(itemView.context, borderResId)

            iconImage.setImageDrawable(ContextCompat.getDrawable(itemView.context, shape.iconId!!))
            subTitle.text = shape.title

            rootItem.setOnClickListener {
                listener(shape)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.shape_map_border_item, viewGroup, false)
        return ShapeBorderHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ShapeBorderHolder).bind(sizeList[position], position, starMapBorder as MutableLiveData<StarMapBorder>, listener)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    fun addAllSizeList(list: ArrayList<ShapeMapBorder>) {
        sizeList.addAll(list)

        notifyDataSetChanged()
    }
}