package com.example.imagesearch.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.Response
import com.example.imagesearch.R
import com.example.imagesearch.activity.ResultActivity
import com.example.imagesearch.adapter.ColorAdapter
import com.example.imagesearch.adapter.ImageAdapter
import com.example.imagesearch.apiClient.RetrofitClient
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.LayoutBottomSheetFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback


class BottomSheetFragment : BottomSheetDialogFragment(), AdapterClick {
    private lateinit var binding: LayoutBottomSheetFilterBinding
    var listColor : ArrayList<Color> = ArrayList()
    private var imageList: List<Hits>? = null
    var colorAdapter: ColorAdapter? = null
    var pos = 0
    var image_type : String? = null
    var orientation : String? = null
    var category : String? = null
    var min_width = 0;
    var min_height = 0
    var color : String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LayoutBottomSheetFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listColor.add(Color("red",R.color.red))
        listColor.add(Color("yellow",R.color.yellow))
        listColor.add(Color("green",R.color.green))
        listColor.add(Color("blue",R.color.blue))
        listColor.add(Color("pink",R.color.pink))
        listColor.add(Color("turquoise",R.color.cyan))
        listColor.add(Color("lilac",R.color.orange))
        listColor.add(Color("white",R.color.yellow))
        listColor.add(Color("gray",R.color.green))
        listColor.add(Color("black",R.color.blue))
        listColor.add(Color("brown",R.color.pink))
        listColor.add(Color("blue",R.color.blue))

        colorAdapter = ColorAdapter(listColor,context,this)

        binding.rcvColor.layoutManager = GridLayoutManager(requireContext(),8)
        colorAdapter!!.setData(listColor)
        colorAdapter!!.updateData(Common.getColorInt(requireContext()))
        binding.rcvColor.adapter = colorAdapter

        //Check orientation
        binding.tvAll.setOnClickListener {
            checkOrientation(1)
        }
        binding.tvHorizontal.setOnClickListener {
            checkOrientation(2)
        }
        binding.tvVertical.setOnClickListener {
            checkOrientation(3)
        }

        //Check order
        binding.tvPopular.setOnClickListener {
            checkOrder(1)
        }
        binding.tvLastes.setOnClickListener {
            checkOrder(2)
        }

        //Check category
        binding.tvBackGround.setOnClickListener {
            checkCategory(1)
        }
        binding.tvFashion.setOnClickListener {
            checkCategory(2)
        }
        binding.tvNature.setOnClickListener {
            checkCategory(3)
        }
        binding.tvScience.setOnClickListener {
            checkCategory(4)
        }
        binding.tvEducation.setOnClickListener {
            checkCategory(5)
        }
        binding.tvFeeling.setOnClickListener {
            checkCategory(6)
        }
        binding.tvHealth.setOnClickListener {
            checkCategory(7)
        }
        binding.tvPeople.setOnClickListener {
            checkCategory(8)
        }

        //check Heigh and Width
        binding.edHeigh.setOnClickListener {
            Common.setMinHeight(requireContext(),binding.edHeigh.text.toString().toInt())
        }
        binding.edWidth.setOnClickListener {
            Common.setMinWidth(requireContext(),binding.edWidth.text.toString().toInt())
        }

        //Check Color
        binding.tvWhiteBlack.setOnClickListener {
            Common.setColors(requireContext(),"grayscale")
            binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red)
            binding.tvTransparent.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.white))
            binding.tvTransparent.setTextColor(resources.getColor(R.color.bottom_un_checked))
            colorAdapter!!.updateData(-1)
        }
        binding.tvTransparent.setOnClickListener {
            Common.setColors(requireContext(),"transparent")
            binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvTransparent.setBackgroundResource(R.drawable.btn_red)
            binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvTransparent.setTextColor(resources.getColor(R.color.white))
            colorAdapter!!.updateData(-1)
        }



        binding.tvApply.setOnClickListener {
          this.dismiss()
            val intent = Intent(requireContext(), ResultActivity::class.java).putExtra("filter", true)
            startActivity(intent)

        }
        binding.tvReset.setOnClickListener {
            Common.setOrder(requireContext(),"")
            Common.setColors(requireContext(),"")
            Common.setCategory(requireContext(),"")
            Common.setOrientation(requireContext(),"")
            Common.setMinWidth(requireContext(),0)
            Common.setMinHeight(requireContext(),0)
            binding.tvPopular.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvLastes.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvAll.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvHorizontal.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvVertical.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvTransparent.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
            binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

            binding.tvPopular.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvLastes.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvAll.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvHorizontal.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvVertical.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvTransparent.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
            binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))

            binding.edHeigh.setText("")
            binding.edWidth.setText("")


        }

        check()

    }
    fun check(){
        //Orientation
        when(Common.getOrientation(requireContext())){
            "all"->{
                binding.tvAll.setBackgroundResource(R.drawable.btn_red)
                binding.tvAll.setTextColor(resources.getColor(R.color.white))
            }
            "horizontal" ->{
                binding.tvHorizontal.setBackgroundResource(R.drawable.btn_red)
                binding.tvHorizontal.setTextColor(resources.getColor(R.color.white))
            }
            "vertical"->{
                binding.tvVertical.setBackgroundResource(R.drawable.btn_red)
                binding.tvVertical.setTextColor(resources.getColor(R.color.white))
            }
        }

        //Order
        when(Common.getOrder(requireContext())){
            "popular" ->{
                binding.tvPopular.setBackgroundResource(R.drawable.btn_red)
                binding.tvPopular.setTextColor(resources.getColor(R.color.white))
            }
            "latest"->{
                binding.tvLastes.setBackgroundResource(R.drawable.btn_red)
                binding.tvLastes.setTextColor(resources.getColor(R.color.white))
            }
        }

        //Category
        when(Common.getCategory(requireContext())){
            "backgrounds"->{
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red)
                binding.tvBackGround.setTextColor(resources.getColor(R.color.white))

            }
            "fashion"->{
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red)
                binding.tvFashion.setTextColor(resources.getColor(R.color.white))

            }
            "nature"->{
                binding.tvNature.setBackgroundResource(R.drawable.btn_red)
                binding.tvNature.setTextColor(resources.getColor(R.color.white))

            }
            "science"->{
                binding.tvScience.setBackgroundResource(R.drawable.btn_red)
                binding.tvScience.setTextColor(resources.getColor(R.color.white))
            }
            "education"->{
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red)
                binding.tvEducation.setTextColor(resources.getColor(R.color.white))
            }
            "feelings"->{
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red)
                binding.tvFeeling.setTextColor(resources.getColor(R.color.white))
            }
            "health"->{
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red)
                binding.tvHealth.setTextColor(resources.getColor(R.color.white))
            }
            "people"->{
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red)
                binding.tvPeople.setTextColor(resources.getColor(R.color.white))

            }
        }

        //Color
        when(Common.getColors(requireContext())){
            "grayscale" ->{
                binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red)
                binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.white))
            }
            "transparent" ->{
                binding.tvTransparent.setBackgroundResource(R.drawable.btn_red)
                binding.tvTransparent.setTextColor(resources.getColor(R.color.white))
            }
        }

        //Heigh and Width
        binding.edHeigh.setText(Common.getMinHeight(requireContext()).toString())
        binding.edWidth.setText(Common.getMinWidth(requireContext()).toString())
    }

    fun checkOrientation(int: Int){
        when(int){
            1 -> {
                Common.setOrientation(requireContext(),"all")
                binding.tvAll.setBackgroundResource(R.drawable.btn_red)
                binding.tvHorizontal.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvVertical.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvAll.setTextColor(resources.getColor(R.color.white))
                binding.tvHorizontal.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvVertical.setTextColor(resources.getColor(R.color.bottom_un_checked))

            }
            2 -> {
                Common.setOrientation(requireContext(),"horizontal")
                binding.tvAll.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHorizontal.setBackgroundResource(R.drawable.btn_red)
                binding.tvVertical.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvAll.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHorizontal.setTextColor(resources.getColor(R.color.white))
                binding.tvVertical.setTextColor(resources.getColor(R.color.bottom_un_checked))

            }
            3 -> {
                Common.setOrientation(requireContext(),"vertical")
                binding.tvAll.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHorizontal.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvVertical.setBackgroundResource(R.drawable.btn_red)

                binding.tvAll.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHorizontal.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvVertical.setTextColor(resources.getColor(R.color.white))
            }

        }
    }
    fun checkOrder(int : Int){
        when(int){
            1 -> {
                Common.setOrder(requireContext(), "popular")
                binding.tvPopular.setBackgroundResource(R.drawable.btn_red)
                binding.tvLastes.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvPopular.setTextColor(resources.getColor(R.color.white))
                binding.tvLastes.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            2 ->{
                Common.setOrder(requireContext(),"latest")
                binding.tvPopular.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvLastes.setBackgroundResource(R.drawable.btn_red)

                binding.tvPopular.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvLastes.setTextColor(resources.getColor(R.color.white))

            }
        }
    }

    fun checkCategory(int: Int){
        when(int){
            1->{
                Common.setCategory(requireContext(),"backgrounds")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.white))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            2->{
                Common.setCategory(requireContext(),"fashion")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.white))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            3->{
                Common.setCategory(requireContext(),"nature")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.white))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            4->{
                Common.setCategory(requireContext(),"science")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.white))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            5->{
                Common.setCategory(requireContext(),"education")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.white))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            6->{
                Common.setCategory(requireContext(),"feelings")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.white))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            7->{
                Common.setCategory(requireContext(),"health")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red_uncheck)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.white))
                binding.tvPeople.setTextColor(resources.getColor(R.color.bottom_un_checked))
            }
            8->{
                Common.setCategory(requireContext(),"people")
                binding.tvBackGround.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFashion.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvNature.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvScience.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvEducation.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvFeeling.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvHealth.setBackgroundResource(R.drawable.btn_red_uncheck)
                binding.tvPeople.setBackgroundResource(R.drawable.btn_red)

                binding.tvBackGround.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFashion.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvNature.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvScience.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvEducation.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvFeeling.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvHealth.setTextColor(resources.getColor(R.color.bottom_un_checked))
                binding.tvPeople.setTextColor(resources.getColor(R.color.white))
            }
        }
    }
//    fun checkColor(int: Int){
//        when(int){
//            1 -> {
//                Common.setColors(requireContext(), "grayscale")
//                binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red)
//                binding.tvTransparent.setBackgroundResource(R.drawable.btn_red_uncheck)
//
//                binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.white))
//                binding.tvTransparent.setTextColor(resources.getColor(R.color.bottom_un_checked))
//            }
//            2 ->{
//                Common.setColors(requireContext(),"transparent")
//                binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red_uncheck)
//                binding.tvTransparent.setBackgroundResource(R.drawable.btn_red)
//
//                binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.bottom_un_checked))
//                binding.tvTransparent.setTextColor(resources.getColor(R.color.white))
//
//            }
//        }
//    }






    override fun itemClick(hits: Hits?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun itemImageClick(string: String?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun itemColorClick(color: Color?, position: Int) {
        Common.setColors(requireContext(),color!!.name)
        Common.setColorInt(requireContext(),position)
        binding.tvWhiteBlack.setTextColor(resources.getColor(R.color.bottom_un_checked))
        binding.tvTransparent.setTextColor(resources.getColor(R.color.bottom_un_checked))
        binding.tvWhiteBlack.setBackgroundResource(R.drawable.btn_red_uncheck)
        binding.tvTransparent.setBackgroundResource(R.drawable.btn_red_uncheck)

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

}

