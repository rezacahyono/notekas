package com.kmmi.notekas.ui.addnote;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.kmmi.notekas.R;
import com.kmmi.notekas.db.DatabaseContract;
import com.kmmi.notekas.db.notekashelper.NotekasHelper;
import com.kmmi.notekas.model.Notekas;
import com.kmmi.notekas.preferences.AppPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddNoteFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText edtTitle, edtDescription, edtAmount;
    private AutoCompleteTextView tvTypeInput;
    private ImageButton btnDelete;

    private boolean isEdit;
    private Notekas notekas;
    private NotekasHelper notekasHelper;
    private static int EXTRA_POSITION = 0;
    private final static int ACTION_ADD = 1;
    private static final int ACTION_UPDATE = 2;
    private static final int ACTION_DELETE = 3;
    private static final int ACTION_CLOSE = 4;

    private int ACTION_BACK;

    private AppPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("Add note");
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.main_toolbar);
        btnDelete = view.findViewById(R.id.btn_delete);
        requireActivity().setActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            dialogAction(ACTION_CLOSE, view);
        });

        edtTitle = view.findViewById(R.id.edt_title);
        edtDescription = view.findViewById(R.id.edt_description);
        edtAmount = view.findViewById(R.id.edt_amount);
        tvTypeInput = view.findViewById(R.id.type_input);

        ACTION_BACK = AddNoteFragmentArgs.fromBundle(getArguments()).getIsBack();

        String[] arrayActivity = getResources().getStringArray(R.array.type_input);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.item_type_down, arrayActivity);
        tvTypeInput.setAdapter(adapter);

        Button btnAddNote = view.findViewById(R.id.btn_add);
        btnAddNote.setOnClickListener(this);

        notekasHelper = NotekasHelper.getInstance(requireActivity());
        notekasHelper.open();
        preferences = new AppPreferences(requireActivity());

        notekas = AddNoteFragmentArgs.fromBundle(getArguments()).getNote();
        if (notekas != null) {
            EXTRA_POSITION = AddNoteFragmentArgs.fromBundle(getArguments()).getPosition();
            isEdit = true;
        } else {
            notekas = new Notekas();
        }

        showViewDelete(view);
        String actionBarTitle;
        String btnTitle;
        if (isEdit) {
            actionBarTitle = "Change note";
            btnTitle = "Change";
            if (notekas != null) {
                tvTypeInput.setText(notekas.getTypeInput());
                edtTitle.setText(notekas.getTitle());
                edtDescription.setText(notekas.getDescription());
                edtAmount.setText(String.valueOf(notekas.getAmount()));
            }
        } else {
            actionBarTitle = "Add note";
            btnTitle = "Add";
        }
        requireActivity().setTitle(actionBarTitle);
        btnAddNote.setText(btnTitle);
    }

    @Override
    public void onClick(View view) {
        AddNoteFragmentDirections.ActionAddNoteFragmentToHomeFragment toHomeFragment = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment();
        AddNoteFragmentDirections.ActionAddNoteFragmentToDetailNoteFragment toDetailNoteFragment = AddNoteFragmentDirections.actionAddNoteFragmentToDetailNoteFragment();
        if (view.getId() == R.id.btn_add) {
            String typeInput = tvTypeInput.getText().toString();
            String title = Objects.requireNonNull(edtTitle.getText()).toString();
            String description = Objects.requireNonNull(edtDescription.getText()).toString();
            double amount = Double.parseDouble(Objects.requireNonNull(edtAmount.getText()).toString());

            if (title.isEmpty()) {
                edtTitle.setError(requireActivity().getString(R.string.cannot_empty));
            } else if (description.isEmpty()) {
                edtDescription.setText(requireActivity().getString(R.string.cannot_empty));
            }

            String date = getCurrentDate();
            int idUser = preferences.getIdUserPrefs();

            notekas.setTypeInput(typeInput);
            notekas.setTitle(title);
            notekas.setDescription(description);
            notekas.setAmount(amount);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.NoteKasColumns.TYPE_INPUT, typeInput);
            contentValues.put(DatabaseContract.NoteKasColumns.TITLE, title);
            contentValues.put(DatabaseContract.NoteKasColumns.DESCRIPTION, description);
            contentValues.put(DatabaseContract.NoteKasColumns.AMOUNT, amount);
            contentValues.put(DatabaseContract.NoteKasColumns.DATE, date);

            if (isEdit) {
                long result = notekasHelper.update(String.valueOf(notekas.getId()), contentValues);
                if (result > 0) {
                    Toast.makeText(requireActivity(), "Berhasil Ubah", Toast.LENGTH_SHORT).show();
                    if (ACTION_BACK==1) {
                        toHomeFragment.setIsAction(ACTION_UPDATE);
                        toHomeFragment.setNotekas(notekas);
                        toHomeFragment.setPosition(EXTRA_POSITION);
                        Navigation.findNavController(view).navigate(toHomeFragment);
                    }else if (ACTION_BACK == 2) {
                        toDetailNoteFragment.setIsAction(ACTION_UPDATE);
                        toDetailNoteFragment.setNotekas(notekas);
                        toDetailNoteFragment.setPosition(EXTRA_POSITION);
                        Navigation.findNavController(view).navigate(toDetailNoteFragment);
                    }
                } else {
                    Toast.makeText(requireActivity(), "Gagal Ubah", Toast.LENGTH_SHORT).show();
                }
            } else {
                contentValues.put(DatabaseContract.NoteKasColumns.ID_USER, idUser);
                long result = notekasHelper.insert(contentValues);
                if (result > 0) {
                    notekas.setId((int) result);
                    Toast.makeText(requireActivity(), "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                    toHomeFragment.setIsAction(ACTION_ADD);
                    toHomeFragment.setNotekas(notekas);
                    toHomeFragment.setPosition(EXTRA_POSITION);
                    Navigation.findNavController(view).navigate(toHomeFragment);
                } else {
                    Toast.makeText(requireActivity(), "Gagal ditambah", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void showViewDelete(View view) {
        if (isEdit) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                dialogAction(ACTION_DELETE, view);
            });
        } else {
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void dialogAction(int type, View view) {
        boolean isDialogAction = type == ACTION_CLOSE;
        String dialogTitle, dialogMessage;
        if (isDialogAction) {
            dialogTitle = "Cancel";
            dialogMessage = "Apakah anda yakin untuk keluar dari form";
        } else {
            dialogTitle = "Delete note";
            dialogMessage = "Apakah anda yakin menghapus item ini";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton("Ya", (dialog, id) -> {
                    if (type == ACTION_CLOSE) {
                        if (ACTION_BACK == 1) {
                            Navigation.findNavController(view).navigate(R.id.action_addNoteFragment_to_homeFragment);
                        }else if (ACTION_BACK == 2) {
                            Navigation.findNavController(view).navigate(R.id.action_addNoteFragment_to_detailNoteFragment);
                        }
                    } else if (type == ACTION_DELETE) {
                        AddNoteFragmentDirections.ActionAddNoteFragmentToHomeFragment toHomeFragment = AddNoteFragmentDirections.actionAddNoteFragmentToHomeFragment();
                        AddNoteFragmentDirections.ActionAddNoteFragmentToDetailNoteFragment toDetailNoteFragment = AddNoteFragmentDirections.actionAddNoteFragmentToDetailNoteFragment();
                        long result = notekasHelper.deleteById(String.valueOf(notekas.getId()));
                        if (result > 0) {
                            if (ACTION_BACK == 1) {
                                toHomeFragment.setPosition(EXTRA_POSITION);
                                toHomeFragment.setNotekas(notekas);
                                toHomeFragment.setIsAction(ACTION_DELETE);
                                Navigation.findNavController(view).navigate(toHomeFragment);
                            }else if (ACTION_BACK == 2) {
                                toDetailNoteFragment.setPosition(EXTRA_POSITION);
                                toDetailNoteFragment.setNotekas(notekas);
                                toDetailNoteFragment.setIsAction(ACTION_DELETE);
                                Navigation.findNavController(view).navigate(toDetailNoteFragment);
                            }
                        } else {
                            Toast.makeText(requireActivity(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", (dialog, id) -> {
                    dialog.cancel();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}