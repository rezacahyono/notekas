package com.kmmi.notekas.ui.splash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kmmi.notekas.R;
import com.kmmi.notekas.preferences.AppPreferences;

public class SplashFragment extends Fragment {

    private final static long TIME_SPLASH = 1000L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppPreferences preferences = new AppPreferences(requireActivity());
        Handler handler = new Handler();
        if (preferences.getNextPage()) {
            if (preferences.getIsLogin()) {
                handler.postDelayed(() -> Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment),TIME_SPLASH);
            }else {
                handler.postDelayed(() -> Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_signinFragment), TIME_SPLASH);
            }
        }else  {
            handler.postDelayed(() -> Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_slidePagerFragment),TIME_SPLASH);
        }
    }
}