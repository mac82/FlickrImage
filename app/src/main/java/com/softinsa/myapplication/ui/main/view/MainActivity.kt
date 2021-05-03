package com.softinsa.myapplication.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.softinsa.myapplication.data.model.ImageDetailModel
import com.softinsa.myapplication.data.model.ImageResponseModel
import com.softinsa.myapplication.data.model.ImageSizeDetailModel
import com.softinsa.myapplication.data.network.RetrofitBuilder
import com.softinsa.myapplication.enums.StatusNetworkCall.*
import com.softinsa.myapplication.ui.base.ViewModelFactory
import com.softinsa.myapplication.ui.main.adapters.MainAdapter
import com.softinsa.myapplication.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainAdapter.ItemClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    private var currentPage = 1
    private var totalPages = 1
    private var perPage = 1
    private var startingIndex = 0

    private val imageDetailModelList: ArrayList<ImageDetailModel> = ArrayList()
    private val imageSizeDetailList: ArrayList<ImageSizeDetailModel> = ArrayList()

    var mLayoutManager: GridLayoutManager? = null

    object Constants {
        const val GRID_COLUMNS = 2
        const val TAG = "birds"
        const val FILTER_SIZE_BY_LARGE_SQUARE = "Large Square"
        const val FILTER_SIZE_BY_LARGE = "Large"
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

        // Setup pagination when rv reaches bottom
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(Constants.TAG, "reach end of image list")
                    currentPage++
                    getMainImageList(Constants.TAG, currentPage)
                }
            }
        })

        getMainImageList(Constants.TAG, currentPage)
    }

    private fun getMainImageList(tags: String, page: Int) {
        viewModel.getBirdsImagesList(tags, page).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let {
                            //
                            totalPages = it.images.pages
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
        })
    }

    private fun getImageListByTag(imageId: String, imageTag:String) {
        viewModel.getSizeListByImageId(imageId).observe(this, Observer { it ->
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { photoSizesResponseModel ->

                            // Filter size by given label (Large Square)
                            var sizeListFiltered: List<ImageSizeDetailModel> =
                                    photoSizesResponseModel.sizes.imageSizeDetailList.filter { photoSizeDetailModel ->
                                        photoSizeDetailModel.label == imageTag

                                    }

                            // Handle full screen image
                            if (sizeListFiltered.size == 1 && imageTag == Constants.FILTER_SIZE_BY_LARGE) {
                                sizeListFiltered[0].imageId = imageId
                                retrieveFullscreenImage(sizeListFiltered[0])
                            // Handle large square image to show in the list
                            } else if (sizeListFiltered.size == 1 && imageTag == Constants.FILTER_SIZE_BY_LARGE_SQUARE) {
                                // Update size list with filtered size
                                sizeListFiltered[0].imageId = imageId
                                imageSizeDetailList.add(sizeListFiltered[0])
                                // Update rv adpater with new image
                                retrieveImageSizesList(imageSizeDetailList)
                            } else {
                                var tagNotFound = getString(R.string.image_by_tag_not_found_error, imageId)
                                Toast.makeText(this, tagNotFound, Toast.LENGTH_LONG).show()
                            }
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

    private fun retrievePhotoList(imageResponseModel: ImageResponseModel) {
        imageResponseModel.images.imageList?.let { currentPhotoDetailModelList ->
            imageDetailModelList.addAll(currentPhotoDetailModelList)
            Log.d(Constants.TAG, "List size -> ${imageDetailModelList.size}")

            perPage = imageResponseModel.images.perpage

            // get only new items from starting index to the end of the list
            for (i in startingIndex until imageDetailModelList.size) {
                getImageListByTag(imageDetailModelList[i].id, Constants.FILTER_SIZE_BY_LARGE_SQUARE)
            }

            //Update Starting Index
            Log.d(Constants.TAG, "List starting index -> ${startingIndex}")
            startingIndex+=perPage
        }
    }

    private fun retrieveImageSizesList(imageSizeDetailModel: List<ImageSizeDetailModel>) {
        adapter.apply {
            addImages(imageSizeDetailModel)
            notifyDataSetChanged()
        }
    }

    override fun onBirdItemClick(view: View?, position: Int) {
        Log.d(Constants.TAG, "Pressed image id -> ${imageSizeDetailList[position].imageId}")
        getImageListByTag(imageSizeDetailList[position].imageId, Constants.FILTER_SIZE_BY_LARGE)
    }

    private fun retrieveFullscreenImage(imageSizeDetailModel: ImageSizeDetailModel) {
        frFullScreenImageContainer.visibility = View.VISIBLE
        frFullScreenImageContainer.setOnClickListener {
            if (frFullScreenImageContainer.isVisible){
                frFullScreenImageContainer.visibility = View.GONE
            }
        }

        Glide.with(ivBirdFullScreen.context)
                .load(imageSizeDetailModel.source)
                .fitCenter()
                .into(ivBirdFullScreen)
    }
}