package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanaka.mazivanhanga.itsmvvmmorty.R
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
class CharacterAdapter(val context: Context, private val onItemTouchListener: OnItemTouchListener) :
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }

        }
    }


    fun submitList(characters: List<Character>) {
        differ.submitList(characters)
    }

    var differ = AsyncListDiffer<Character>(this, DIFF_CALLBACK)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.onItemTouchListener = onItemTouchListener
        holder.bindView(character)

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var onItemTouchListener: OnItemTouchListener
        lateinit var character: Character


        var characterImageView: ImageView = itemView.findViewById(R.id.character_image)
        var characterNameTextView: TextView = itemView.findViewById(R.id.character_name)

        fun bindView(character: Character) {
            this.character = character
            Glide.with(context).load(character.image).into(characterImageView)
            characterNameTextView.text = character.name
        }

        init {
            itemView.setOnClickListener {
                onItemTouchListener.onClicked(character = character)
            }
        }
    }
}

interface OnItemTouchListener {
    fun onClicked(character: Character)
}