package com.softinsa.myapplication.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softinsa.myapplication.R
import com.softinsa.myapplication.data.model.PhotoSizeDetailModel
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(private val photoSizeDetailList: ArrayList<PhotoSizeDetailModel>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {
    private var mClickListener: ItemClickListener? = null

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photoSizeDetailModel: PhotoSizeDetailModel) {
            itemView.apply {
                Glide.with(imageViewAvatar.context)
                        .load(photoSizeDetailModel.source)
                        .into(imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))

    override fun getItemCount(): Int = photoSizeDetailList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(photoSizeDetailList[position])

        holder.itemView.setOnClickListener {
            if (mClickListener != null) mClickListener!!.onBirdItemClick(
                    it,
                    position
            )
        }
    }

    fun addPhotos(photoSizeDetailList: List<PhotoSizeDetailModel>) {
        this.photoSizeDetailList.apply {
            clear()
            addAll(photoSizeDetailList)
        }
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onBirdItemClick(view: View?, position: Int)
    }
}