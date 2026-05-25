package com.stupidbeauty.hxlauncher.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.stupidbeauty.hxlauncher.animations.ItemAnimation;
import com.stupidbeauty.hxlauncher.bean.Data;

public class AdapterListAnimation extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{



    //Context which this adapter is used in
    private Context mContext;

    //The animation type we want to use
    private int animation_type;

    //Our data to be shown
    private List<Data> mDataList;

    private int lastPosition = -1;

    private boolean on_attach = true;

    @Override
       public void onAttachedToRecyclerView(RecyclerView recyclerView) {
           recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
               @Override
               public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                   //When the user scrolls down, we change the translation of the animation
                   //This will make it look much better.
                   on_attach = false;
                   super.onScrollStateChanged(recyclerView, newState);
               }
           });
           super.onAttachedToRecyclerView(recyclerView);
       }

    private void setAnimation(View view, int position)
    {
        if (position > lastPosition) {
            //When we scroll down, on_attach will become false and so we return -1
            //-1 will change the animation effect, making it appear differently
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);

            lastPosition = position;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof OriginalViewHolder) {
                OriginalViewHolder view = (OriginalViewHolder) holder;

                Data data = mDataList.get(position);
                view.image.setBackgroundResource(data.getImageId());
                view.description.setText(data.getLoremIpsum());

                //set our animation for each view and it's associated position
                setAnimation(view.itemView, position);


            }
        }

    @Override
    public int getItemCount() {
        return 0;
    }

    public AdapterListAnimation(Context context, List<Data> dataList, int animation_type)
    {
        this.mDataList = dataList;
        mContext = context;
        this.animation_type = animation_type;
    }
}