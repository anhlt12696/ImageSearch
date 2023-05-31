package com.example.imagesearch.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.ItemHistory
import com.example.imagesearch.R
import com.example.imagesearch.activity.*
import com.example.imagesearch.adapter.HistoryAdapter
import com.example.imagesearch.adapter.HistoryAdapter.ClickItem
import com.example.imagesearch.base.visible
import com.example.imagesearch.database.HistoryDatabase
import com.example.imagesearch.databinding.DialogNotficationBinding
import com.example.imagesearch.databinding.FragmentSearchBinding
import java.util.ArrayList


class SearchFragment : Fragment(), AdapterClick,ClickItem {
    private lateinit var binding: FragmentSearchBinding
    var historyAdapter: HistoryAdapter? = null

    var mlistHistory: List<ItemHistory> = ArrayList()

    lateinit var bindingDialog : DialogNotficationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//
//        binding.search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            startActivity(intent)
//        }
        binding.searchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.putExtra("query", binding.searchInput.query.toString())
                startActivity(intent)
                addHistory()
                binding.searchInput.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        binding.imgSetting.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
        binding.icDelete.setOnClickListener {
            DialogCustom()
        }
        binding.showAllHistory.setOnClickListener {
            val intent = Intent(requireContext(), SearchHistoryActivity::class.java)

            startActivity(intent)
        }

    }

    private fun addHistory() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
        if (binding.searchInput.query.isEmpty()) {
            return
        }
        val itemHistory = ItemHistory(binding.searchInput.query.toString())

        HistoryDatabase.getInstance(requireContext()).historyDao().insertHistory(itemHistory)
        mlistHistory = HistoryDatabase.getInstance(requireContext()).historyDao().listHistory
        historyAdapter!!.setData(mlistHistory)

    }

    override fun itemClick(hits: Hits?, position: Int) {

    }

    override fun itemImageClick(string: String?, position: Int) {
        val intent = Intent(requireContext(), ResultActivity::class.java).putExtra("query",
            mlistHistory[position].historyItem)
        startActivity(intent)


    }

    override fun itemColorClick(color: Color?, position: Int) {

    }

    override fun onClick(history: String?) {
        val intent = Intent(requireContext(), ResultActivity::class.java).putExtra("query",
           history)
        startActivity(intent)
    }

    override fun upDateData() {
        mlistHistory = HistoryDatabase.getInstance(requireContext()).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        if((mlistHistory as MutableList<ItemHistory>?)!!.size > 5){
            mlistHistory = (mlistHistory as MutableList<ItemHistory>?)!!.subList(0, 5)
        }

        historyAdapter!!.setData(mlistHistory)
        if(mlistHistory.isNotEmpty()){
            binding.icDelete.isVisible = true
            binding.showAllHistory.isVisible = true
            binding.imgHistory.isVisible = false
        }else{
            binding.icDelete.isVisible = false
            binding.showAllHistory.isVisible = false
            binding.imgHistory.isVisible = true
        }
    }
    fun DialogCustom(){
        val dialog = Dialog(requireContext())
        bindingDialog = DialogNotficationBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        bindingDialog.tvCancel.setOnClickListener { view ->
            dialog.dismiss()
        }
        bindingDialog.tvPermit.setOnClickListener { view ->
            HistoryDatabase.getInstance(requireContext()).historyDao().delete()
            mlistHistory = ArrayList()
            historyAdapter!!.setData(mlistHistory)
            historyAdapter!!.notifyDataSetChanged()
            binding.imgHistory.isVisible = true
            binding.icDelete.isVisible = false
            binding.showAllHistory.isVisible = false
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        mlistHistory = HistoryDatabase.getInstance(requireContext()).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        if((mlistHistory as MutableList<ItemHistory>?)!!.size > 5){
            mlistHistory = (mlistHistory as MutableList<ItemHistory>?)!!.subList(0, 5)
        }

        historyAdapter = HistoryAdapter(requireContext(),mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)

        val linearLayoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        binding.rvHistory.layoutManager = linearLayoutManager
        binding.rvHistory.adapter = this.historyAdapter
        binding.imgHistory.isVisible = mlistHistory.isEmpty()
        if(mlistHistory.isNotEmpty()){
            binding.icDelete.isVisible = true
            binding.showAllHistory.isVisible = true
        }else{
            binding.icDelete.isVisible = false
            binding.showAllHistory.isVisible = false
        }

    }


}