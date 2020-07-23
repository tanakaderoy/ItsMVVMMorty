package com.tanaka.mazivanhanga.itsmvvmmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tanaka.mazivanhanga.itsmvvmmorty.databinding.CharacterDetailBottomSheetLayoutBinding
import com.tanaka.mazivanhanga.itsmvvmmorty.model.Character


/**
 * Created by Tanaka Mazivanhanga on 07/18/2020
 */
object CharacterDetailBottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var character: Character
    private var _binding: CharacterDetailBottomSheetLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CharacterDetailBottomSheetLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCharacter(view)

    }

    private fun setUpCharacter(view: View) {
        binding.apply {
            detailName.text = character.name
            detailStatus.text = character.status
            detailSpecies.text = character.species
            detailGender.text = character.gender
            detailLocation.text = character.location.name
            Glide.with(view).load(character.image).into(detailImage)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}