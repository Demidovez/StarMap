package adapters

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.kalambur.mappy_stars.R
import androidx.lifecycle.MutableLiveData
import com.kalambur.mappy_stars.databinding.ShapeItemBinding
import com.kalambur.mappy_stars.interfaces.HasShapeInterface
import com.kalambur.mappy_stars.interfaces.ShapeInterface
import com.kalambur.mappy_stars.utils.helpers.Helper

class ShapeAdapter(
    private val holderShape: MutableLiveData<HasShapeInterface>,
    private val listener: (shape: ShapeInterface) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var sizeList = arrayListOf<ShapeInterface>()

    class ShapeHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ShapeItemBinding.bind(item)

        fun bind(shape: ShapeInterface, position: Int, holderShape: MutableLiveData<HasShapeInterface>, listener: (shape: ShapeInterface) -> Unit) = with(binding) {
            if(position == 0) {
                rootItem.updatePadding(left = Helper.dpToPx(16F, itemView.context))
            } else {
                rootItem.updatePadding(left = 0)
            }

            val borderResId = if(holderShape.value!!.shapeType == shape.type) {
                R.drawable.border_rect_recycler_item_selected
            } else {
                R.drawable.border_rect_recycler_item
            }

            backgroundLayout.background = ContextCompat.getDrawable(itemView.context, borderResId)

            val icon = ContextCompat.getDrawable(itemView.context, shape.iconId)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                icon!!.colorFilter = BlendModeColorFilter(ContextCompat.getColor(itemView.context, R.color.dark), BlendMode.SRC_ATOP)
            } else {
                icon!!.setColorFilter(ContextCompat.getColor(itemView.context, R.color.dark), PorterDuff.Mode.SRC_ATOP)
            }

            iconImage.setImageDrawable(icon)

            rootItem.setOnClickListener {
                listener(shape)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.shape_item, viewGroup, false)
        return ShapeHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ShapeHolder).bind(sizeList[position], position, holderShape, listener)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    fun addAllSizeList(list: ArrayList<ShapeInterface>) {
        sizeList.addAll(list)

        notifyDataSetChanged()
    }
}