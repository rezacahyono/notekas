package com.kmmi.notekas.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kmmi.notekas.R;
import com.kmmi.notekas.adapter.ListNotekasAdapter;
import com.kmmi.notekas.db.notekashelper.NotekasHelper;
import com.kmmi.notekas.load.LoadNoteAsync;
import com.kmmi.notekas.load.LoadNotekasCallback;
import com.kmmi.notekas.model.Notekas;
import com.kmmi.notekas.preferences.AppPreferences;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, LoadNotekasCallback {

    private ListNotekasAdapter listNotekasAdapter;
    private RecyclerView rvHistory;

    private static int ACTION = 0;
    private final static String EXTRA_STATE = "extra_state";

    private NotekasHelper notekasHelper;
    AppPreferences preferences;
    TextView tvSaldo, tvAmount;

    ArrayList<Notekas> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHistory = view.findViewById(R.id.rv_history);

        FloatingActionButton btnAdd = view.findViewById(R.id.fab_add);
        Button btnIncome = view.findViewById(R.id.btn_income);
        Button btnExpenditure = view.findViewById(R.id.btn_expenditur);
        tvSaldo = view.findViewById(R.id.tv_saldo);
        tvAmount = view.findViewById(R.id.tv_amount);

        notekasHelper = NotekasHelper.getInstance(requireActivity());

        preferences = new AppPreferences(requireActivity());
        int idUser = preferences.getIdUserPrefs();

        ACTION = HomeFragmentArgs.fromBundle(getArguments()).getIsAction();

        viewAmount();
        btnAdd.setOnClickListener(this);
        btnIncome.setOnClickListener(this);
        btnExpenditure.setOnClickListener(this);

        listNotekasAdapter = new ListNotekasAdapter(1);
        rvHistory.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvHistory.setHasFixedSize(true);
        rvHistory.setAdapter(listNotekasAdapter);

        if (savedInstanceState == null) {
            new LoadNoteAsync(requireActivity(), this).executeNoteskasAll(idUser);
        } else {
            ArrayList<Notekas> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                listNotekasAdapter.setListNotekas(list);
                this.list = list;
                viewAction();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, list);
    }

    @Override
    public void onClick(View view) {
        HomeFragmentDirections.ActionHomeFragmentToDetailNoteFragment toDetailNoteFragment = HomeFragmentDirections.actionHomeFragmentToDetailNoteFragment();
        if (view.getId() == R.id.fab_add) {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addNoteFragment);
        } else if (view.getId() == R.id.btn_income) {
            toDetailNoteFragment.setInputType(true);
            preferences.setType(true);
            Navigation.findNavController(view).navigate(toDetailNoteFragment);
        } else if (view.getId() == R.id.btn_expenditur) {
            toDetailNoteFragment.setInputType(false);
            preferences.setType(false);
            Navigation.findNavController(view).navigate(toDetailNoteFragment);
        }
    }

    private void viewAction() {
        Notekas notekas;
        switch (ACTION) {
            case 1: {
                notekas = HomeFragmentArgs.fromBundle(getArguments()).getNotekas();
                listNotekasAdapter.addItem(notekas);
                rvHistory.smoothScrollToPosition(listNotekasAdapter.getItemCount() - 1);
                break;
            }
            case 2: {
                notekas = HomeFragmentArgs.fromBundle(getArguments()).getNotekas();
                int position = HomeFragmentArgs.fromBundle(getArguments()).getPosition();
                listNotekasAdapter.updateItem(position, notekas);
                rvHistory.smoothScrollToPosition(position);
            }
            case 3: {
                int position = HomeFragmentArgs.fromBundle(getArguments()).getPosition();
                listNotekasAdapter.removeItem(position);
            }

        }
    }

    private void viewAmount() {
        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(localeID);
        Cursor cursor = notekasHelper.getAmount(String.valueOf(preferences.getIdUserPrefs()));
        cursor.moveToFirst();
        int amount = cursor.getInt(1) - cursor.getInt(2);
        tvAmount.setText(rupiahFormat.format(amount));
        tvSaldo.setText(rupiahFormat.format(amount));
    }

    @Override
    public void preExecute() {
    }

    @Override
    public void postExecute(ArrayList<Notekas> notekas) {
        if (notekas.size() > 0) {
            listNotekasAdapter.setListNotekas(notekas);
            list = notekas;
        } else {
            listNotekasAdapter.setListNotekas(new ArrayList<>());
        }
    }

}