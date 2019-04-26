package com.studioseven.postcard.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.studioseven.postcard.Models.Options;
import com.studioseven.postcard.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private ArrayList<Options> mOptionList;
    private Context mContext;
    private SparseBooleanArray selectedItems;
    private int fragmentID;
    private Set<String> optionsSelected;
    private SharedPreferences sharedpreferences;

    public OptionAdapter(ArrayList<Options> mOptionList, Context mContext, int fragmentID) {
        this.mOptionList = mOptionList;
        this.mContext = mContext;
        selectedItems = new SparseBooleanArray();
        this.fragmentID = fragmentID;
        optionsSelected = new HashSet<>();
        sharedpreferences = mContext.getSharedPreferences("shared", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.question_card, viewGroup, false);

        return new OptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionViewHolder holder, final int i) {
        final Options article = mOptionList.get(i);
        holder.title.setText(article.getOption());
        Glide.with(mContext).load(article.getimageUrl()).into(holder.article_thumbnail);
        holder.cardView.setSelected(selectedItems.get(i, false));

        HashSet<String> set = new HashSet<>();
        addToSharedPreference(set, fragmentID);

        holder.article_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: " + i);
                if (selectedItems.get(i, false)) {
                    selectedItems.delete(i);
                    holder.cardView.setSelected(false);
                    optionsSelected = sharedpreferences.getStringSet(String.valueOf(fragmentID), null);
                    optionsSelected.remove(mOptionList.get(i).getOption());
                    addToSharedPreference(optionsSelected, fragmentID);
                    for(String option: optionsSelected) {
                        Log.d("TAG", option + " ");
                    }
                    //Glide.with(mContext).load(article.getThumbnail()).into(holder.article_thumbnail);
                }
                else {
                    selectedItems.put(i, true);
                    holder.cardView.setSelected(true);
                    optionsSelected = sharedpreferences.getStringSet(String.valueOf(fragmentID), null);
                    if(optionsSelected == null){
                        optionsSelected = new HashSet<>();
                    }
                    optionsSelected.add(mOptionList.get(i).getOption());
                    addToSharedPreference(optionsSelected, fragmentID);
                    for(String option: optionsSelected) {
                        Log.d("TAG", option + " ");
                    }
                    //Glide.with(mContext).load(R.drawable.select).into(holder.article_thumbnail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOptionList.size();
    }

    private void addToSharedPreference(Set<String> set, int fragmentID) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putStringSet(String.valueOf(fragmentID), set);
        editor.apply();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView article_thumbnail;
        RelativeLayout cardLayout;
        CardView cardView;

        OptionViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            article_thumbnail = view.findViewById(R.id.thumbnail);
            cardLayout = view.findViewById(R.id.card_layout);
            cardView = view.findViewById(R.id.card_view);
        }
    }
}
