package com.softinsa.myapplication.ui.main.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softinsa.myapplication.R
import com.softinsa.myapplication.data.api.FlickrHelper
import com.softinsa.myapplication.data.model.PhotoDetailModel
import com.softinsa.myapplication.data.model.PhotoResponseModel
import com.softinsa.myapplication.data.model.PhotoSizeDetailModel
import com.softinsa.myapplication.data.network.RetrofitBuilder
import com.softinsa.myapplication.enums.StatusNetworkCall.*
import com.softinsa.myapplication.ui.base.ViewModelFactory
import com.softinsa.myapplication.ui.main.adapters.MainAdapter
import com.softinsa.myapplication.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.android.synthetic.main.item_layout.view.*


class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    private var currentPage = 1
    private var totalPages = 1
    private var perPage = 1
    private var startingIndex = 0

    private val photoDetailModelList: ArrayList<PhotoDetailModel> = ArrayList()
    private val photoSizeDetailList: ArrayList<PhotoSizeDetailModel> = ArrayList()

    var mLayoutManager: GridLayoutManager? = null

    object Constants {
        const val GRID_COLUMNS = 2
        const val TAG = "birds"
        const val FILTER_SIZE_BY_LABEL = "Large Square"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                ViewModelFactory(FlickrHelper(RetrofitBuilder.flickrService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView.layoutManager = GridLayoutManager(this, Constants.GRID_COLUMNS)
        adapter = MainAdapter(arrayListOf())
        adapter.setClickListener(this)
        recyclerView.adapter = adapter

        mLayoutManager = recyclerView.layoutManager as GridLayoutManager

        // Setup pagination when rv reach bottom
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(Constants.TAG, "reach end of list")
                    currentPage++
                    getPhotoList(Constants.TAG, currentPage)
                }
            }
        })

        getPhotoList(Constants.TAG, currentPage)
    }

    private fun getPhotoList(tags: String, page: Int) {
        viewModel.getBirdsImagesList(tags, page).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let {
                            //
                            totalPages = it.photos.pages
                            retrievePhotoList(it)
                        }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }

            if (viewModel.getBirdsImagesList(tags, page).hasActiveObservers()) {
                Log.d(Constants.TAG, " -- getBirdList has Active Observers -- ")
                viewModel.getBirdsImagesList(tags, page).removeObservers(this)
            } else {
                Log.d(Constants.TAG, " -- getBirdList does not have Active Observers -- ")
            }
        })
    }


    private fun getImageSizeList(imageId: String) {
        viewModel.getSizeListByImageId(imageId).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { photoSizesResponseModel ->

                            // Filter size by given label (Large Square)
                            var sizeListFiltered: List<PhotoSizeDetailModel> =
                                    photoSizesResponseModel.sizes.photoSizeDetailList.filter { photoSizeDetailModel ->
                                        photoSizeDetailModel.label == Constants.FILTER_SIZE_BY_LABEL

                                    }
                            // Update size list with filtered size
                            photoSizeDetailList.addAll(sizeListFiltered)
                            // Update rv adpater with new list
                            retrieveImageSizesList(photoSizeDetailList)
                        }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrievePhotoList(photoResponseModel: PhotoResponseModel) {
        photoResponseModel.photos.photoList?.let { currentPhotoDetailModelList ->
            photoDetailModelList.addAll(currentPhotoDetailModelList)
            Log.d(Constants.TAG, "List size -> ${photoDetailModelList.size}")

            perPage = photoResponseModel.photos.perpage

            // get only new items from starting index to the end of the list
            for (i in startingIndex until photoDetailModelList.size) {
                getImageSizeList(photoDetailModelList[i].id)
            }

            //Update Starting Index
            Log.d(Constants.TAG, "List starting index -> ${startingIndex}")
            startingIndex+=perPage
        }
    }

    private fun retrieveImageSizesList(photoSizeDetailModel: List<PhotoSizeDetailModel>) {
        adapter.apply {
            addPhotos(photoSizeDetailModel)
            notifyDataSetChanged()
        }
    }

    override fun onBirdItemClick(view: View?, position: Int) {

        frFullScreenImageContainer.visibility = View.VISIBLE
        frFullScreenImageContainer.setOnClickListener {
            if (frFullScreenImageContainer.isVisible){
                frFullScreenImageContainer.visibility = View.GONE
            }
        }

        Glide.with(ivBirdFullScreen.context)
                .load(photoSizeDetailList[position].source)
                .centerInside()
                .into(ivBirdFullScreen)

    }
}