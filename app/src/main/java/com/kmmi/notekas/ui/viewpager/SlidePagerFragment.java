package com.kmmi.notekas.ui.viewpager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmmi.notekas.R;
import com.kmmi.notekas.adapter.SlidePagerAdapter;
import com.kmmi.notekas.model.SlidePage;
import com.kmmi.notekas.preferences.AppPreferences;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class SlidePagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        CircleIndicator3 indicator = view.findViewById(R.id.slide_indicator);
        ArrayList<SlidePage> listSlider = new ArrayList<>();
        listSlider.add(new SlidePage(R.drawable.ic_page_one, requireActivity().getResources().getString(R.string.slogan_one)));
        listSlider.add(new SlidePage(R.drawable.ic_page_two, requireActivity().getResources().getString(R.string.slogan_two)));
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(new SlidePagerAdapter(listSlider, viewPager));
        AppPreferences preferences = new AppPreferences(requireActivity());
        preferences.setNextPage(true);
        indicator.setViewPager(viewPager);
    }
}