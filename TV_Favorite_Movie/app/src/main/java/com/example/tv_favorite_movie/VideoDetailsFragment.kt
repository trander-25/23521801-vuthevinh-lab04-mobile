package com.example.tv_favorite_movie

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.SparseArrayObjectAdapter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class VideoDetailsFragment : DetailsSupportFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movie = requireActivity().intent.getSerializableExtra(DetailsActivity.MOVIE) as? Movie
            ?: return

        val detailsRow = DetailsOverviewRow(movie)
        val actionsAdapter = SparseArrayObjectAdapter().apply {
            set(0, Action(0, "Play"))
            set(1, Action(1, "Add to Favorites"))
        }
        detailsRow.actionsAdapter = actionsAdapter

        val presenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter()).apply {
            backgroundColor = requireContext().getColor(R.color.tv_background)
        }

        val selector = ClassPresenterSelector().apply {
            addClassPresenter(DetailsOverviewRow::class.java, presenter)
        }

        val adapter = ArrayObjectAdapter(selector)
        adapter.add(detailsRow)
        this.adapter = adapter

        loadRowImage(movie, detailsRow)
    }

    private fun loadRowImage(movie: Movie, row: DetailsOverviewRow) {
        val uri = movie.getCardImageURI()?.toString() ?: return
        Picasso.get()
            .load(uri)
            .resize(Utils.convertDpToPixel(requireContext(), 274), Utils.convertDpToPixel(requireContext(), 386))
            .centerCrop()
            .placeholder(R.drawable.movie)
            .error(R.drawable.movie)
            .into(RowImageTarget(row))
    }

    private inner class RowImageTarget(private val row: DetailsOverviewRow) : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            val drawable = BitmapDrawable(resources, bitmap)
            row.setImageDrawable(drawable)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            row.setImageDrawable(errorDrawable)
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            row.setImageDrawable(placeHolderDrawable)
        }
    }
}

