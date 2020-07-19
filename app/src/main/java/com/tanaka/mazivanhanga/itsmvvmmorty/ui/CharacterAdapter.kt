package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanaka.mazivanhanga.itsmvvmmorty.R
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CharacterAdapter(val context: Context, val onItemTouchListener: OnItemTouchListener) :
    PagedListAdapter<Character, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.character = character
            holder.onItemTouchListener = onItemTouchListener
            Glide.with(context).load(character.image).into(holder.characterImageView)
            holder.characterNameTextView.text = character.name
        } else {
            Toast.makeText(context, "Failed to get Item", Toast.LENGTH_SHORT).show()
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var onItemTouchListener: OnItemTouchListener
        lateinit var character: Character


        var characterImageView: ImageView = itemView.findViewById(R.id.character_image)
        var characterNameTextView: TextView = itemView.findViewById(R.id.character_name)
        init {
            itemView.setOnClickListener{
                onItemTouchListener.onClicked(character = character)
            }
        }
    }
}

interface OnItemTouchListener{
    fun onClicked(character: Character)
}