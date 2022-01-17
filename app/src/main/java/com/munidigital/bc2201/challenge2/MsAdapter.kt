package com.munidigital.bc2201.challenge2

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.munidigital.bc2201.databinding.MsListItemBinding

val TAG: String = MsAdapter::class.java.simpleName

class MsAdapter: ListAdapter<Message, MsAdapter.MsViewHolder>(DiffCallBack) {

    companion object DiffCallBack: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsViewHolder {
        val binding = MsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return MsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MsViewHolder, position: Int) {
        val message: Message = getItem(position)
        holder.bind(message)
    }

    inner class MsViewHolder(private val binding: MsListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.msMessage.text = message.text
            if (message.sender) {
                binding.msMessage.setBackgroundColor(Color.argb(255,208,230,219))
                binding.msMessage.gravity = Gravity.END
            }
            else {
                binding.msMessage.setBackgroundColor(Color.argb(255,234,237,243))
                binding.msMessage.gravity = Gravity.START
            }
            binding.executePendingBindings()
        }
    }
}