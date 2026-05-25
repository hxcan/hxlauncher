package com.stupidbeauty.hxlauncher.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@SuppressWarnings({"EmptyMethod", "unused"})
public class OriginalViewHolder extends RecyclerView.ViewHolder
{
	public OriginalViewHolder(@NonNull View itemView, TextView description, ImageView image) {
		super(itemView);
		this.description = description;
		this.image = image;
	}

	public TextView description;
	public ImageView image;

}
