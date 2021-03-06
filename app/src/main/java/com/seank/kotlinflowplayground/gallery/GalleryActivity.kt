package com.seank.kotlinflowplayground.gallery

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.seank.kotlinflowplayground.R
import com.seank.kotlinflowplayground.app
import com.seank.kotlinflowplayground.gallery.di.GalleryModule
import javax.inject.Inject

class GalleryActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: GalleryViewModelFactory

    private lateinit var viewModel: GalleryViewModel

    private lateinit var viewPager: ViewPager
    private lateinit var loadingView: View
    private lateinit var errorView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.appComponent.gallerySubcomponent(GalleryModule()).inject(this)
        setContentView(R.layout.activity_gallery)
        viewPager = findViewById(R.id.viewpager)
        loadingView = findViewById(R.id.loading)
        errorView = findViewById(R.id.error)

        viewModel = ViewModelProvider(this, viewModelFactory).get(GalleryViewModel::class.java)

        viewModel.cards.observe(this, Observer {
            viewPager.adapter = CardViewPagerAdapter(it)
        })
        viewModel.showLoading.observe(this, Observer {
            loadingView.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.error.observe(this, Observer {
            errorView.visibility = if (it != null) View.VISIBLE else View.GONE
            errorView.text = it
        })
        viewModel.showContent.observe(this, Observer {
            viewPager.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.fetchCard()
    }
}
