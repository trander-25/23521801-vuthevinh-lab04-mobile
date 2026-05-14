package com.example.tv_favorite_movie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.net.URI

class CardPresenter : Presenter() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        val cardView = ImageCardView(parent.context).apply {
            cardType = BaseCardView.CARD_TYPE_INFO_UNDER
            infoVisibility = BaseCardView.CARD_REGION_VISIBLE_ALWAYS
            isFocusable = true
            isFocusableInTouchMode = true
            setBackgroundColor(parent.context.getColor(R.color.fastlane_background))
        }
        return CardViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        val holder = viewHolder as CardViewHolder
        holder.setMovie(movie)
        holder.cardView.titleText = movie.title
        holder.cardView.contentText = movie.studio
        holder.cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        holder.updateCardViewImage(movie.getCardImageURI())
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val holder = viewHolder as CardViewHolder
        holder.cardView.mainImage = null
    }

    private inner class CardViewHolder(val cardView: ImageCardView) : ViewHolder(cardView) {
        private val defaultCardImage: Drawable =
            cardView.context.getDrawable(R.drawable.movie) ?: throw IllegalStateException("Missing placeholder")
        private val imageTarget = PicassoImageCardViewTarget(cardView)

        fun setMovie(movie: Movie) {
            cardView.tag = movie
        }

        fun updateCardViewImage(uri: URI?) {
            if (uri == null) {
                cardView.mainImage = defaultCardImage
                return
            }
            Picasso.get()
                .load(uri.toString())
                .resize(
                    Utils.convertDpToPixel(cardView.context, CARD_WIDTH),
                    Utils.convertDpToPixel(cardView.context, CARD_HEIGHT)
                )
                .centerCrop()
                .placeholder(defaultCardImage)
                .error(defaultCardImage)
                .into(imageTarget)
        }
    }

    private class PicassoImageCardViewTarget(
        private val imageCardView: ImageCardView
    ) : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            val bitmapDrawable = BitmapDrawable(imageCardView.context.resources, bitmap)
            imageCardView.mainImage = bitmapDrawable
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            imageCardView.mainImage = errorDrawable
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            imageCardView.mainImage = placeHolderDrawable
        }
    }

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }
}

