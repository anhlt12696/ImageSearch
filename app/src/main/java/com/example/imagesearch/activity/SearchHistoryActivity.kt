package com.example.imagesearch.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.ItemHistory
import com.example.imagesearch.adapter.HistoryAdapter
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.database.HistoryDatabase
import com.example.imagesearch.databinding.ActivitySearchHistoryBinding
import com.example.imagesearch.databinding.DialogNotficationBinding
import java.util.ArrayList

class SearchHistoryActivity :  BaseActivity(), AdapterClick, HistoryAdapter.ClickItem {

    private lateinit var binding : ActivitySearchHistoryBinding


    var historyAdapter: HistoryAdapter? = null

    var mlistHistory: List<ItemHistory>? = null

    lateinit var bindingDialog : DialogNotficationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //history

        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        historyAdapter = HistoryAdapter(this,mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = linearLayoutManager
        binding.rvHistory.adapter = this.historyAdapter
        Log.d("===",mlistHistory!!.size.toString())


        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.imgDelete.setOnClickListener {
            DialogCustom()
        }

    }


    fun DialogCustom(){
        val dialog = Dialog(this)
        bindingDialog = DialogNotficationBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        bindingDialog.tvCancel.setOnClickListener { view ->
            dialog.dismiss()
        }
        bindingDialog.tvPermit.setOnClickListener { view ->
            HistoryDatabase.getInstance(this).historyDao().delete()
            mlistHistory = ArrayList()
            historyAdapter!!.setData(mlistHistory)
            historyAdapter!!.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun itemClick(hits: Hits, position: Int) {

    }

    override fun itemImageClick(string: String?, position: Int) {

    }

    override fun itemColorClick(color: Color?, position: Int) {
        TODO("Not yet implemented")
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onClick(history: String?) {
        val intent = Intent(this, ResultActivity::class.java).putExtra("query",
            history)
        startActivity(intent)
    }

    override fun upDateData() {
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        historyAdapter!!.setData(mlistHistory)

    }

    override fun onResume() {
        super.onResume()
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        historyAdapter = HistoryAdapter(this,mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = linearLayoutManager
        binding.rvHistory.adapter = this.historyAdapter
    }
}