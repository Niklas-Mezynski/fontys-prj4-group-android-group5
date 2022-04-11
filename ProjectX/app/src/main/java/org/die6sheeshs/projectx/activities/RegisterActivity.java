package org.die6sheeshs.projectx.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobScheduler;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.CustomTextWatcher;
import org.die6sheeshs.projectx.helpers.IllegalUserInputException;
import org.die6sheeshs.projectx.helpers.InputVerification;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout firstNameField;
    private TextInputLayout lastNameField;
    private TextInputLayout nicknameField;
    private TextInputLayout birthdateField;
    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private TextInputLayout password2Field;
    private Button gotoToLogin;
    private Button submit;

    UserPersistence userPersistence = UserPersistence.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameField = findViewById(R.id.first_name_field);
        lastNameField = findViewById(R.id.last_name_field);
        nicknameField = findViewById(R.id.nick_name_field);
        birthdateField = findViewById(R.id.birthdate_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        password2Field = findViewById(R.id.password_repeat_field);
        gotoToLogin = findViewById(R.id.goto_login_view);
        submit = findViewById(R.id.submit_registration);

        //Add listeners to the input fields which indicate if the input is valid
        addVerificationListeners();


        submit.setOnClickListener(currentView -> {
            //TODO do the final verification of the input data

            submitUser();
        });

        gotoToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });


    }

    private void submitUser() {
        String firstName = firstNameField.getEditText().getText().toString();
        String lastName = lastNameField.getEditText().getText().toString();
        String nickname = nicknameField.getEditText().getText().toString();
        String email = emailField.getEditText().getText().toString();
        String password = passwordField.getEditText().getText().toString();
        String password2 = password2Field.getEditText().getText().toString();
        String dateString = birthdateField.getEditText().getText().toString();
        LocalDateTime birthday = LocalDateTime.parse(dateString + " 08:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Observable<User> response = userPersistence.createUser(firstName, lastName, email, nickname, birthday, "picUrl", "About me", password);
        response.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Registration", "User POST error: " + error.getMessage()))
                .subscribe(user -> {
                    //User registered successfully -> redirect him to the main activity
                    MainActivity.authUserId = user.getId();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.v("Registration", "New user created: " + user.getNick_name() + "   " + user.getId());
                });
    }

    private void addVerificationListeners() {

        firstNameField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            firstNameField.setBoxBackgroundColor(bgColor);
        });

        lastNameField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            lastNameField.setBoxBackgroundColor(bgColor);
        });

        nicknameField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            nicknameField.setBoxBackgroundColor(bgColor);
        });

        birthdateField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.dateWithoutTime(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            birthdateField.setBoxBackgroundColor(bgColor);
        });

        emailField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            emailField.setBoxBackgroundColor(bgColor);
        });

        passwordField.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            passwordField.setBoxBackgroundColor(bgColor);
            Log.v("Input change", editable.toString());
        });

        password2Field.getEditText().addTextChangedListener((CustomTextWatcher) editable -> {
            int bgColor = getResources().getColor(R.color.none);
            try {
                InputVerification.stringNotEmpty(editable.toString());
            } catch (IllegalUserInputException e) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            //Check if PW 1 and 2 are equal
            if (!passwordField.getEditText().getText().toString().equals(editable.toString())) {
                bgColor = getResources().getColor(R.color.errorRedAlpha);
            }
            password2Field.setBoxBackgroundColor(bgColor);
        });
    }
}