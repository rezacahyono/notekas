package com.kmmi.notekas.ui.login.signup;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.kmmi.notekas.R;
import com.kmmi.notekas.db.userhelper.UserHelper;

public class SignupFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText edtUsername, edtEmail, edtPassword;
    private Button btnRegister;
    private UserHelper userHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userHelper = UserHelper.getInstance(requireActivity());
        edtUsername = view.findViewById(R.id.edt_username);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        btnRegister = view.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            String username = edtUsername.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty()) {
                edtUsername.setError(requireActivity().getString(R.string.cannot_empty));
            } else if (email.isEmpty()) {
                edtEmail.setError(requireActivity().getString(R.string.cannot_empty));
            } else if (!isValidEmail(email)) {
                edtEmail.setError(requireActivity().getString(R.string.valid_email));
            } else if (password.isEmpty()) {
                edtPassword.setError(requireActivity().getString(R.string.cannot_empty));
            } else {
                if (!userHelper.checkUser(username)) {
                    boolean insert = userHelper.insert(username, email, password);
                    dialogRegister(insert);
                } else {
                    dialogRegister(false);
                }
                edtUsername.setText("");
                edtEmail.setText("");
                edtPassword.setText("");
            }
            userHelper.close();
        }

    }

    private void dialogRegister(boolean isSuccessfull) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Status");
        if (isSuccessfull) {
            builder.setIcon(R.drawable.ic_baseline_check_24);
            builder.setMessage("Register Successfully");
        } else {
            builder.setIcon(R.drawable.ic_baseline_close_24);
            builder.setMessage("Register Failed");
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}