package com.techtitans.usman.usmanmart.recyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.databinding.ItemReviewBinding;
import com.techtitans.usman.usmanmart.models.ReviewModel;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ReviewModel> reviewList;
    private final Context context;

    public ReviewAdapter(Context context) {
        this.reviewList = new ArrayList<>();
        this.context=context;
    }

    public ReviewAdapter(Context context,List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
        this.context=context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setReviews(List<ReviewModel> newReviews) {
        reviewList.clear();
        reviewList.addAll(newReviews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@org.jspecify.annotations.NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@org.jspecify.annotations.NonNull ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ItemReviewBinding binding;

        ReviewViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ReviewModel review) {
            binding.tvReviewerName.setText(review.getReviewerName());
            binding.tvReviewText.setText(review.getReviewText());
            binding.ratingBarReview.setRating(review.getRating());

            if (review.getTimestamp() > 0) {
                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                        review.getTimestamp());
                binding.tvReviewDate.setText(relativeTime);
            }

            if (review.getReviewerImageUrl() != null && !review.getReviewerImageUrl().isEmpty()) {
                Picasso.get().load(review.getReviewerImageUrl()).placeholder(R.drawable.image).into(binding.ivReviewerImage);
            }
        }
    }
}

