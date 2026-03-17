package com.pax.android.supporthub.app.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.pax.android.supporthub.app.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ADD_BUTTON = 0;
    private static final int TYPE_SELECTED_IMAGE = 1;

    private List<Uri> selectedImages;
    private OnAddButtonClickListener onAddButtonClickListener;
    private OnRemoveButtonClickListener onRemoveButtonClickListener;

    public interface OnAddButtonClickListener {
        void onAddButtonClick();
    }

    public interface OnRemoveButtonClickListener {
        void onRemoveButtonClick(int position);
    }

    public ImageAdapter(List<Uri> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public List<Uri> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<Uri> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.onAddButtonClickListener = listener;
    }

    public void setOnRemoveButtonClickListener(OnRemoveButtonClickListener listener) {
        this.onRemoveButtonClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < selectedImages.size()) {
            return TYPE_SELECTED_IMAGE;
        } else {
            return TYPE_ADD_BUTTON;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ADD_BUTTON) {
            View view = inflater.inflate(R.layout.item_image_add, parent, false);
            return new AddButtonViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_image, parent, false);
            return new SelectedImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectedImageViewHolder) {
            Uri uri = selectedImages.get(position);
            ((SelectedImageViewHolder) holder).bind(uri);
        }

    }

    @Override
    public int getItemCount() {
        return selectedImages.size() + 1; // +1 for add button
    }

    class AddButtonViewHolder extends RecyclerView.ViewHolder {
        ImageView addButton;

        AddButtonViewHolder(View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.addButton);
            addButton.setOnClickListener(v -> {
                if (onAddButtonClickListener != null) {
                    onAddButtonClickListener.onAddButtonClick();
                }
            });
        }
    }

    class SelectedImageViewHolder extends RecyclerView.ViewHolder {
        ImageView selectedImage;
        ImageButton removeButton;

        SelectedImageViewHolder(View itemView) {
            super(itemView);
            selectedImage = itemView.findViewById(R.id.imageView_item);
            removeButton = itemView.findViewById(R.id.image_delete);

            removeButton.setOnClickListener(v -> {
                if (onRemoveButtonClickListener != null) {
                    onRemoveButtonClickListener.onRemoveButtonClick(getBindingAdapterPosition());
                }
            });
        }

        void bind(Uri imageUri) {
            Glide.with(itemView.getContext())
                    .load(imageUri)
                    .into(selectedImage);
        }
    }
}