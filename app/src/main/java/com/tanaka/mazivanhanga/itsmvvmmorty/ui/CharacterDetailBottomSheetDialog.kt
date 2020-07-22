package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tanaka.mazivanhanga.itsmvvmmorty.R
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
object CharacterDetailBottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var character: Character
    lateinit var detailNameTextView: TextView
    lateinit var detailStatusTextView: TextView
    lateinit var detailSpeciesTextView: TextView
    lateinit var detailGenderTextView: TextView
    lateinit var detailLocationTextView: TextView
    lateinit var detailImageView: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.character_detail_bottom_sheet_layout, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews(view)
        setUpCharacter(view)

    }

    private fun setUpCharacter(view: View) {
        detailNameTextView.text = character.name
        detailStatusTextView.text = character.status
        detailSpeciesTextView.text = character.species
        detailGenderTextView.text = character.gender
        detailLocationTextView.text = character.location.name
        Glide.with(view).load(character.image).into(detailImageView)
    }

    private fun setUpViews(view: View) {
        detailNameTextView = view.findViewById(R.id.detail_name)
        detailStatusTextView = view.findViewById(R.id.detail_status)
        detailSpeciesTextView = view.findViewById(R.id.detail_species)
        detailGenderTextView = view.findViewById(R.id.detail_gender)
        detailLocationTextView = view.findViewById(R.id.detail_location)
        detailImageView = view.findViewById(R.id.detail_image)
    }


}