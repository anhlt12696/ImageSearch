package com.example.imagesearch.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.*
import com.example.imagesearch.adapter.HistoryAdapter
import com.example.imagesearch.adapter.HistoryAdapter.ClickItem
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.database.HistoryDatabase
import com.example.imagesearch.databinding.ActivitySearchBinding
import com.example.imagesearch.databinding.DialogNotficationBinding


class SearchActivity :  BaseActivity(), AdapterClick,ClickItem {

    private lateinit var binding : ActivitySearchBinding
    private var imageList: List<Hits>? = null


    var historyAdapter: HistoryAdapter? = null

    var mlistHistory: List<ItemHistory>? = null
    lateinit var bindingDialog : DialogNotficationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //history

        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
//        if (intent.getBooleanExtra("show_all_history",false)){
//            mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
//        }
//        else{
//            if((mlistHistory as MutableList<ItemHistory>?)!!.size > 5){
//                mlistHistory = (mlistHistory as MutableList<ItemHistory>?)!!.subList(0, 5);
//            }
//        }

//        val hashSet = HashSet<ItemHistory>()
//        (mlistHistory as MutableList<ItemHistory>?)?.let { hashSet.addAll(it) }
//        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
//        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(hashSet)

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        if((mlistHistory as MutableList<ItemHistory>?)!!.size > 5){
            mlistHistory = (mlistHistory as MutableList<ItemHistory>?)!!.subList(0, 5);
        }
        historyAdapter = HistoryAdapter(this,mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = linearLayoutManager
        binding.rvHistory.adapter = this.historyAdapter
        Log.d("===",mlistHistory!!.size.toString())

        binding.deleteAll.setOnClickListener {
            DialogCustom()

        }
        binding.showAllHistory.setOnClickListener {
//            mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
//            historyAdapter = HistoryAdapter(this,mlistHistory,this)
//            historyAdapter!!.setData(mlistHistory)
//            val linearLayoutManager = LinearLayoutManager(this)
//            binding.rvHistory.layoutManager = linearLayoutManager
//            binding.rvHistory.adapter = this.historyAdapter
            val intent = Intent(this@SearchActivity, SearchHistoryActivity::class.java)

            startActivity(intent)
        }


        binding.floatingSearchView.setShowMoveUpSuggestion(true)
        binding.searchButton.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.floatingSearchView.windowToken, 0)
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("query", binding.floatingSearchView.query)
            startActivity(intent)
            addHistory()
        }


        binding.backArrow.setOnClickListener {
            finish()
        }
        //  binding.searchButton.imageTintList = getColorStateList(R.color.red)
        binding.floatingSearchView.setOnSearchListener(object :
            FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String) {

            }

            override fun onSuggestionClicked(currentText: SearchSuggestion) {

            }
        })
    }

    private fun addHistory() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.floatingSearchView.windowToken, 0)
        if (binding.floatingSearchView.query.isEmpty()) {
            return
        }
        val itemHistory = ItemHistory(binding.floatingSearchView.query)

        HistoryDatabase.getInstance(this).historyDao().insertHistory(itemHistory)
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
        historyAdapter!!.setData(mlistHistory)

    }

    fun DialogCustom(){
        val dialog = Dialog(this)
        bindingDialog = DialogNotficationBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.getRoot())
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

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
        val intent = Intent(this@SearchActivity, FullScreenImageActivity::class.java)
        intent.putExtra("position", hits.largeImageURL)
        startActivity(intent)
    }

    override fun itemImageClick(string: String?, position: Int) {
        binding.floatingSearchView.setSearchText(mlistHistory!![position].historyItem)
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
        binding.floatingSearchView.setSearchText(history)
        val intent = Intent(this, ResultActivity::class.java).putExtra("query",
            history)
        startActivity(intent)
    }

    override fun upDateData() {
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
        historyAdapter!!.setData(mlistHistory)
        binding.layoutHistory.isVisible = (mlistHistory as MutableList<ItemHistory>?)!!.isNotEmpty()
    }

    override fun onResume() {
        super.onResume()
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory

        val uniqueElements: Set<ItemHistory> = HashSet<ItemHistory>(mlistHistory)
        (mlistHistory as MutableList<ItemHistory>?)!!.clear()
        (mlistHistory as MutableList<ItemHistory>?)!!.addAll(uniqueElements)

        if (intent.getBooleanExtra("show_all_history",false)){
            mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
        }
        else{
            if((mlistHistory as MutableList<ItemHistory>?)!!.size > 5){
                mlistHistory = (mlistHistory as MutableList<ItemHistory>?)!!.subList(0, 5);
            }
        }
        historyAdapter = HistoryAdapter(this,mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)
        binding.layoutHistory.isVisible = (mlistHistory as MutableList<ItemHistory>?)!!.isNotEmpty()
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = linearLayoutManager
        binding.rvHistory.adapter = this.historyAdapter
    }
}