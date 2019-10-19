package com.example.ems.Fragments;


import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.ems.Activity.MainActivity;
import com.example.ems.R;
import com.example.ems.Utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import static com.example.ems.Utils.Helper.SHORT;
import static com.example.ems.Utils.Helper.animateError;
import static com.example.ems.Utils.Helper.showSnackbar;

/**
 * Created by Prasoon Goswami
 * on 07-10-19
 * This is a fragment class which performs sign in function
 */
public class SignIn extends Fragment {

    private ImageView logo;
    private FloatingActionButton loginButton;
    private TextInputEditText email_edittext,password_edittext;
    private TextInputLayout emailTextInputLayout,psdTextInputLayout;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView forgotPassword,register;
    private RelativeLayout background;

    public SignIn() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);

        logo = view.findViewById(R.id.logo);
        loginButton = view.findViewById(R.id.signin);
        email_edittext = view.findViewById(R.id.email_login);
        password_edittext = view.findViewById(R.id.password_login);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        psdTextInputLayout = view.findViewById(R.id.psdTextInputLayout);
        progressBar = view.findViewById(R.id.progressBar);
        forgotPassword = view.findViewById(R.id.forgot_password);
        register = view.findViewById(R.id.register);
        background = view.findViewById(R.id.dim_back1);

        progressBar.setVisibility(View.GONE);
        background.setAlpha(1f);
        background.setBackgroundResource(android.R.color.transparent);

        mAuth = FirebaseAuth.getInstance();

        YoYo.with(Techniques.ZoomIn)
                .duration(1000)
                .playOn(logo);
        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .playOn(loginButton);


        loginButton.setOnClickListener(v->{
            userLogin();
        });

        register.setOnClickListener(view1 -> Helper.swapFragmentsWithBackStack(new Register(),R.id.container_frame,getFragmentManager()));

        forgotPassword.setOnClickListener(v->{
            sendPasswordReset();
        });

        return view;
    }

    //firebase login
    public void userLogin() {
        String email = email_edittext.getText().toString().trim();
        String password = password_edittext.getText().toString().trim();

        if (email.isEmpty()) {
            email_edittext.setError("Email is required");
            emailTextInputLayout.requestFocus();
            animateError(emailTextInputLayout,700);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edittext.setError("Please enter a valid email");
            emailTextInputLayout.requestFocus();
            animateError(emailTextInputLayout,700);
            return;
        }

        if (password.isEmpty()) {
            password_edittext.setError("Password is required");
            psdTextInputLayout.requestFocus();
            animateError(psdTextInputLayout,700);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        background.setAlpha(0.3f);
        background.setBackgroundColor(Color.parseColor("#99000000"));
        loginButton.setEnabled(false);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), ""+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                } else {

                    showSnackbar(task.getException().getMessage(),getActivity(),SHORT);
                }

                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                background.setAlpha(1f);
                background.setBackgroundResource(android.R.color.transparent);


            }
        });
    }


    // Password Reset Method
    public void sendPasswordReset() {
        // [START send_password_reset]
        String email = email_edittext.getText().toString().trim();

        if (email.isEmpty()) {
            email_edittext.setError("Email is required");
            emailTextInputLayout.requestFocus();
            animateError(emailTextInputLayout,700);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edittext.setError("Please enter a valid email");
            emailTextInputLayout.requestFocus();
            animateError(emailTextInputLayout,700);
            return;
        }


        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "We have sent you instructions in your registered email to reset your password!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // [END send_password_reset]
    }

}
