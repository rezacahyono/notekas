package com.kmmi.notekas.ui.login.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.kmmi.notekas.R;
import com.kmmi.notekas.db.userhelper.UserHelper;
import com.kmmi.notekas.preferences.AppPreferences;

import java.util.Objects;

public class SigninFragment extends Fragment implements View.OnClickListener {
    private TextInputEditText edtUsername, edtPassword;
    private UserHelper userHelper;
    private AppPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = new AppPreferences(requireActivity());

        userHelper = UserHelper.getInstance(requireActivity());
        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);

        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        TextView tvSignup = view.findViewById(R.id.tv_sign_up);

        btnLogin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String username = Objects.requireNonNull(edtUsername.getText()).toString().trim();
            String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();
            boolean isLogin = userHelper.isLogin(username, password);
            if (isLogin) {
                int idUser = userHelper.getIdUser(username,password);
                preferences.setIdUserPrefs(idUser);
                preferences.setIsLogin(true);
                Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_homeFragment);
            } else {
                Toast.makeText(requireActivity(), "Login failed, user not found!", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btn_cancel) {
            edtUsername.setText("");
            edtPassword.setText("");
        } else if (view.getId() == R.id.tv_sign_up) {
            Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_signupFragment);
        }
    }
}