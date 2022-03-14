package com.example.mvvmApodTest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmApodTest.databinding.ItemViewPictureOfDayBinding
import com.squareup.picasso.Picasso


class SearchPictureAdapter : RecyclerView.Adapter<SearchPictureAdapter.ViewHolder>() {

    private var data = ArrayList<PictureOfDayModel>()
    private var itemClickListener: ItemClick? = null
    private var addButtonListener: AddInDbClick? = null
    private var delButtonListener: DelInDbClick? = null
    private var dateList :ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewPictureOfDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), itemClickListener, addButtonListener, delButtonListener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(data[position])
    }

    fun setDateList(list: ArrayList<String>) {
        dateList = list as ArrayList<String>
        Log.d(this.javaClass.name, "adapter dateList size = " + dateList?.size)
        notifyDataSetChanged()

    }

    fun getItemViewPosition(position: Int): PictureOfDayModel {
        return data[position]
    }

    fun setItemClickListener(itemClickListener: ItemClick) {
        this.itemClickListener = itemClickListener
    }

    fun setAddButtonClickListener(addButtonListener: AddInDbClick) {
        this.addButtonListener = addButtonListener
    }

    fun setDeleteButtonClickListener(delButtonListener: DelInDbClick) {
        this.delButtonListener = delButtonListener
    }

    fun setData(list: List<PictureOfDayModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    interface ItemClick {
        fun onItemClick(item: PictureOfDayModel?)
    }

    interface AddInDbClick {
        fun onAddInDbClick(item: PictureOfDayModel?)
    }

    interface DelInDbClick {
        fun onDelInDbClick(item: PictureOfDayModel?)
    }

    inner class ViewHolder(
        private val binding: ItemViewPictureOfDayBinding, private val itemClick: ItemClick?,
        private val addInDbClick: AddInDbClick?, private val delInDbClick: DelInDbClick?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val date: TextView = binding.textViewDate
        val title: TextView = binding.textViewTitle
        val imageView: ImageView = binding.imageViewPicture
        private val addButton: ImageButton = binding.addInDbButton
        private val delButton: ImageButton = binding.deleteInDbButton
        private var responseModel: PictureOfDayModel? = null

        init {
            itemView.setOnClickListener {
                itemClick?.onItemClick(responseModel)
            }
            addButton.setOnClickListener {
                addInDbClick?.onAddInDbClick(responseModel)
            }
            delButton.setOnClickListener {
                delInDbClick?.onDelInDbClick(responseModel)
            }
        }

        fun bind(responseModel: PictureOfDayModel) {
            this.responseModel = responseModel
            date.text = responseModel.date
            title.text = responseModel.title
            if (dateList?.contains(responseModel.date) == true) {
                addButton.visibility = View.INVISIBLE
                delButton.visibility = View.VISIBLE
            } else {
                addButton.visibility = View.VISIBLE
                delButton.visibility = View.INVISIBLE
            }

            if (responseModel.media_type == "video") {
                Picasso.get()
                    .load(R.drawable.playbtn)
                    .placeholder(R.drawable.ic_baseline_access_time_24)
                    .error(R.drawable.ic_baseline_adb_24)
                    .into(imageView)
            } else if (responseModel.media_type == "image") {
                Picasso.get()
                    .load(responseModel.url)
                    .placeholder(R.drawable.ic_baseline_access_time_24)
                    .error(R.drawable.ic_baseline_adb_24)
                    .into(imageView)
            }
        }
    }
}