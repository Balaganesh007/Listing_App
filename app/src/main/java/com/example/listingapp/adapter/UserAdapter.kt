package com.example.listingapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listingapp.databinding.AllUsersListBinding
import com.example.listingapp.domain.UserDataModel

class UserAdapter(private val onClickListener: OnClickListener) : ListAdapter<UserDataModel, UserAdapter.ViewHolder>(DataDiffCallBack()){

    class DataDiffCallBack : DiffUtil.ItemCallback<UserDataModel>() {

        override fun areItemsTheSame(oldItem: UserDataModel, newItem: UserDataModel): Boolean {
            return oldItem.last == newItem.last
        }

        override fun areContentsTheSame(oldItem: UserDataModel, newItem: UserDataModel): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder (private val binding: AllUsersListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: UserDataModel
        ) {
            Log.v("bala4",item.toString())
            binding.data = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                Log.v("bala2","view holder")
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AllUsersListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v("bala1","oncreate")
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v("bala3","onbindholder")
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    class OnClickListener(val clickListener: (userDataModel: UserDataModel) -> Unit) {
        fun onClick(userDataModel:UserDataModel) = clickListener(userDataModel)
    }

//     override fun getFilter(): Filter {
//        return object: Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filterResults = FilterResults()
//                if (constraint == null || constraint.length < 0){
//                    filterResults.count = itemCount
//                    filterResults.values =
//                }
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//
//            }
//
//        }
//    }
}
