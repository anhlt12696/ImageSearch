package com.example.imagesearch.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.ItemHistory
import com.example.imagesearch.Model.Response
import com.example.imagesearch.adapter.HistoryAdapter
import com.example.imagesearch.adapter.HistoryAdapter.ClickItem
import com.example.imagesearch.adapter.ImageAdapter
import com.example.imagesearch.apiClient.RetrofitClient
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.database.HistoryDatabase
import com.example.imagesearch.databinding.ActivityResultBinding
import com.example.imagesearch.fragment.BottomSheetFragment
import retrofit2.Call
import retrofit2.Callback


class ResultActivity : BaseActivity(), ClickItem , AdapterClick{

    private lateinit var binding : ActivityResultBinding
    private var imageList: List<Hits>? = null


    var historyAdapter: HistoryAdapter? = null

    var mlistHistory: List<ItemHistory>? = null

    private var query: String? = null

    var image_type : String? = null
    var orientation : String? = null
    var category : String? = null
    var min_width = 0;
    var min_height = 0
    var color : String? = ""
  //  private lateinit var bottomSheetFragment : BottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory




        historyAdapter = HistoryAdapter(this,mlistHistory,this)
        historyAdapter!!.setData(mlistHistory)
        imageList = ArrayList()
        binding.rvGallery.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvGallery.layoutManager = gridLayoutManager

        color = Common.getColors(this)
        image_type = Common.getImageType(this)
        orientation = Common.getOrientation(this)
        min_height = Common.getMinHeight(this)
        min_width = Common.getMinWidth(this)
        category = Common.getCategory(this)


        val intent = intent
        if (intent != null) {
             query = intent.getStringExtra("query")
        }
        query?.let { getThumbnail(it) }

        binding.floatingSearchView.setShowMoveUpSuggestion(true)
        binding.floatingSearchView.setSearchText(query)
        binding.searchButton.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.floatingSearchView.windowToken, 0)
            getThumbnail(binding.floatingSearchView.query)
            addHistory()
        }

        binding.backArrow.setOnClickListener {
            finish()
        }
        binding.filter.setOnClickListener {
            finish()
        }
        //  binding.searchButton.imageTintList = getColorStateList(R.color.red)
        binding.floatingSearchView.setOnSearchListener(object :
            FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String) {
                //Log.d("Mor","onSearchAction( currentQuery ="+currentQuery+"  )");

                // Make the network call.
                getThumbnail(currentQuery)
            }

            override fun onSuggestionClicked(currentText: SearchSuggestion) {
                //Log.d("Mor","onSuggestionClicked( currentText ="+currentText+"  )");
            }
        })

        binding.filter.setOnClickListener {
            showBottomSheetDialog()
        }

    }

    fun showBottomSheetDialog() {
        BottomSheetFragment().show(supportFragmentManager,"BottomSheetFragment")

    }
    private fun getThumbnail(query: String) {
        val string: String = "Heigh: " + Common.getMinHeight(this) +"---Width: "+ Common.getMinWidth(this)
        Log.d("===Str" , string)
        val responseCall = RetrofitClient.getInstance().myApi.getImages(
            1,
            50,
            query,
            Common.getOrientation(this),
            Common.getCategory(this),
            Common.getMinWidth(this),
            Common.getMinHeight(this),
            Common.getColors(this),
            Common.getOrder(this)
        )
        responseCall.enqueue(object : Callback<Response?> {
            override fun onResponse(
                call: Call<Response?>,
                response: retrofit2.Response<Response?>
            ) {
                if (response.isSuccessful) {
                    val myResponse = response.body()
                    if (myResponse != null) {
                        imageList = myResponse.hits
                        if (imageList != null && imageList!!.size > 0) {
                            val adapter =
                                ImageAdapter(this@ResultActivity, imageList, this@ResultActivity, 0)
                            binding.rvGallery.adapter = adapter
                            binding.pbGallery.onVisibilityAggregated(false)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Response?>, t: Throwable) {
                Toast.makeText(this@ResultActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
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
    override fun itemClick(hits: Hits, position: Int) {
        val intent = Intent(this@ResultActivity, FullScreenImageActivity::class.java)
        intent.putExtra("position", hits.largeImageURL)
        intent.putExtra("query",query)
        Log.d("test_url", "url: " + hits.largeImageURL)
        startActivity(intent)
    }

    override fun itemImageClick(string: String?, position: Int) {
        TODO("Not yet implemented")
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

    }

    override fun upDateData() {
        mlistHistory = HistoryDatabase.getInstance(this).historyDao().listHistory
        historyAdapter!!.setData(mlistHistory)
    }


}