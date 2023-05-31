package com.example.imagesearch.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.R
import com.example.imagesearch.activity.ZoomImageActivity
import com.example.imagesearch.adapter.ImageSavedAdapter
import com.example.imagesearch.databinding.DialogNotficationBinding
import com.example.imagesearch.databinding.FragmentSavedBinding
import java.io.File


class SavedFragment : Fragment() , AdapterClick {

    private lateinit var binding: FragmentSavedBinding
   private var imageList: ArrayList<String> = ArrayList()
    lateinit var bindingDialog : DialogNotficationBinding
    lateinit var adapter: ImageSavedAdapter
 //   private var imageList: ArrayList<ImageSaved>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageList = getListImageSaved()
        binding.noImage.isVisible = imageList.isEmpty()
        adapter = ImageSavedAdapter(requireContext(), imageList,this)
        adapter.setData(imageList)
        binding.rcvImageSaved.adapter = adapter
        binding.rcvImageSaved.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.rcvImageSaved.layoutManager = gridLayoutManager


        binding.imgDelete.setOnClickListener {
            DialogCustom()
        }


    }
    fun DialogCustom(){
        val dialog = Dialog(requireContext())
        bindingDialog = DialogNotficationBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.getRoot())
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        bindingDialog.tvCancel.setOnClickListener { view ->
            dialog.dismiss()
        }
        bindingDialog.tvPermit.setOnClickListener { view ->
           // val imagesDir = File(requireContext().filesDir,"/ImageSearch")
            val fdelete: File = File(requireContext().filesDir,"/ImageSearch")
            if (fdelete.exists()) {
               // fdelete.delete()
               for (i in fdelete.listFiles()!!){
                   i.delete()
               }
            }
            imageList = getListImageSaved()
            adapter.setData(imageList)
            binding.noImage.isVisible = imageList.isEmpty()
            binding.imgDelete.setImageResource(R.drawable.ic_recycle_bin_no_img)
            binding.imgDelete.isEnabled = false

            dialog.dismiss()
        }

        dialog.show()
    }

    fun getListImageSaved() : ArrayList<String> {
        val listImageSaved = ArrayList<String>()
        val imagesDir = File("${requireActivity().filesDir}/ImageSearch")
        if (imagesDir.exists()) {
            val listImage = imagesDir.listFiles()
            if (listImage != null) {
                for (i in listImage.indices) {
                    listImageSaved.add(listImage[i].path)
                }
            }
        }
        return listImageSaved
    }

    override fun itemClick(hits: Hits?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun itemImageClick(string: String?, position: Int) {
        binding.noImage.isVisible = imageList.isEmpty()
        val intent = Intent(requireContext(), ZoomImageActivity::class.java)
        intent.putExtra("url_image", imageList[position]).putExtra("saved",true)
        startActivity(intent)
    }

    override fun itemColorClick(color: Color?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        imageList = getListImageSaved()
        binding.noImage.isVisible = imageList.isEmpty()
        adapter = ImageSavedAdapter(requireContext(), imageList,this)
        adapter.setData(imageList)
        binding.rcvImageSaved.adapter = adapter
        binding.rcvImageSaved.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.rcvImageSaved.layoutManager = gridLayoutManager

    }


}


