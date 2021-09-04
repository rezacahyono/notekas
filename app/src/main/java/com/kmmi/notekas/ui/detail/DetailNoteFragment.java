package com.kmmi.notekas.ui.detail;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmmi.notekas.R;
import com.kmmi.notekas.adapter.ListNotekasAdapter;
import com.kmmi.notekas.db.notekashelper.NotekasHelper;
import com.kmmi.notekas.load.LoadNoteAsync;
import com.kmmi.notekas.load.LoadNotekasCallback;
import com.kmmi.notekas.model.Notekas;
import com.kmmi.notekas.preferences.AppPreferences;
import com.kmmi.notekas.ui.home.HomeFragmentArgs;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailNoteFragment extends Fragment implements LoadNotekasCallback {
    private boolean inputType;
    private View views;
    private RecyclerView rvDetail;
    private TextView tvAmount;

    private ListNotekasAdapter adapter;
    private NotekasHelper notekasHelper;

    private final static String EXTRA_STATES = "extra_state";
    private final static String TYPE = "type";
    private static int ACTION = 0;
    AppPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        views = view.findViewById(R.id.view);

        Toolbar toolbar = view.findViewById(R.id.main_toolbar);
        requireActivity().setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        tvAmount = view.findViewById(R.id.tv_amount);

        ACTION = DetailNoteFragmentArgs.fromBundle(getArguments()).getIsAction();

        preferences = new AppPreferences(requireActivity());
        notekasHelper = NotekasHelper.getInstance(requireActivity());

        adapter = new ListNotekasAdapter(2);
        rvDetail = view.findViewById(R.id.rv_detail);
        rvDetail.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvDetail.setHasFixedSize(true);
        rvDetail.setAdapter(adapter);
        inputType = preferences.getType();

        viewAmount();
        if (savedInstanceState == null) {
            if (inputType) {
                new LoadNoteAsync(requireActivity(), this).executeIncome(preferences.getIdUserPrefs());
            }else {
                new LoadNoteAsync(requireActivity(),this).executeExpenditure(preferences.getIdUserPrefs());
            }
        }else {
            ArrayList<Notekas> list = savedInstanceState.getParcelableArrayList(EXTRA_STATES);
            if (list != null) {
                adapter.setListNotekas(list);
                viewAction();
            }
        }
        setViewType();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter.getListNotekas() != null){
            outState.putParcelableArrayList(EXTRA_STATES, adapter.getListNotekas());
            outState.putBoolean(TYPE, inputType);
        }
    }

    private void viewAmount() {
        Locale localeID = new Locale("in", "ID");
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(localeID);
        Cursor cursor = notekasHelper.getAmount(String.valueOf(preferences.getIdUserPrefs()));
        cursor.moveToFirst();
        int amount;
        if (inputType) {
            amount = cursor.getInt(1);
        }else {
            amount = cursor.getInt(2);
        }
        tvAmount.setText(moneyFormat.format(amount));
    }

    private void setViewType() {
        if (inputType) {
            views.setBackgroundColor(requireActivity().getResources().getColor(R.color.green));
            requireActivity().setTitle("Detail income");
        } else {
            views.setBackgroundColor(requireActivity().getResources().getColor(R.color.red));
            requireActivity().setTitle("Detail Expenditure");
        }
    }

    private void viewAction() {
        Notekas notekas;
        switch (ACTION) {
            case 1: {
                notekas = DetailNoteFragmentArgs.fromBundle(getArguments()).getNotekas();
                adapter.addItem(notekas);
                rvDetail.smoothScrollToPosition(adapter.getItemCount() - 1);
                break;
            }
            case 2: {
                notekas = DetailNoteFragmentArgs.fromBundle(getArguments()).getNotekas();
                int position = DetailNoteFragmentArgs.fromBundle(getArguments()).getPosition();
                adapter.updateItem(position, notekas);
                rvDetail.smoothScrollToPosition(position);
            }
            case 3: {
                int position = DetailNoteFragmentArgs.fromBundle(getArguments()).getPosition();
                adapter.removeItem(position);
            }

        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<Notekas> notekas) {
        if (notekas.size() > 0) {
            adapter.setListNotekas(notekas);
        }else {
            adapter.setListNotekas(new ArrayList<>());
        }
    }

}