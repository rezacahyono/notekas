package com.kmmi.notekas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.kmmi.notekas.R;
import com.kmmi.notekas.model.SlidePage;

import java.util.ArrayList;

public class SlidePagerAdapter extends RecyclerView.Adapter<SlidePagerAdapter.SlideViewHolder> {

    private final ArrayList<SlidePage> listSlider;
    private final ViewPager2 viewPager;

    public SlidePagerAdapter(ArrayList<SlidePage> listSlider, ViewPager2 viewPager) {
        this.listSlider = listSlider;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_pager,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.bind(listSlider.get(position));
    }

    @Override
    public int getItemCount() {
        return listSlider.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivSlider;
        private final TextView tvSlogan;
        private final Button btnSkip;
        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlider = itemView.findViewById(R.id.iv_slide);
            tvSlogan = itemView.findViewById(R.id.tv_slogan_slide);
            btnSkip = itemView.findViewById(R.id.btn_skip);
        }

        private void bind(SlidePage slidePage) {
            ivSlider.setImageResource(slidePage.getImage());
            tvSlogan.setText(slidePage.getSlogan());
            if (viewPager.getCurrentItem() == listSlider.size() - 1) {
                btnSkip.setText(itemView.getResources().getString(R.string.start));
            }
            btnSkip.setOnClickListener(v ->{
                if (viewPager.getCurrentItem() == listSlider.size()-1) {
                    Navigation.findNavController(itemView).navigate(R.id.action_slidePagerFragment_to_signinFragment);
                }else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });
        }
    }
}
