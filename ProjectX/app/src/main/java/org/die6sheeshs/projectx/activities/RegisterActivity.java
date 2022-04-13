package org.die6sheeshs.projectx.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.CustomTextWatcher;
import org.die6sheeshs.projectx.helpers.IllegalUserInputException;
import org.die6sheeshs.projectx.helpers.InputVerification;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
    private TextView tv_registrationError;

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
        tv_registrationError = findViewById(R.id.tv_register_error);

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
        tv_registrationError.setText("");
        tv_registrationError.setVisibility(View.GONE);

        String firstName = firstNameField.getEditText().getText().toString();
        String lastName = lastNameField.getEditText().getText().toString();
        String nickname = nicknameField.getEditText().getText().toString();
        String email = emailField.getEditText().getText().toString();
        String password = passwordField.getEditText().getText().toString();
        String password2 = password2Field.getEditText().getText().toString();
        String dateString = birthdateField.getEditText().getText().toString();

        List<String> validationResult = validateUserInputs(firstName, lastName, nickname, email, dateString, password, password2);

        if (!validationResult.isEmpty()) {
            //Inputs are wrong... output a message and return
            Log.v("Registration", "Invalid inputs found");
            StringBuilder sb = new StringBuilder();
            validationResult.forEach(s -> sb.append(s + "\n"));
            tv_registrationError.setText(sb.toString());
            tv_registrationError.setVisibility(View.VISIBLE);

            return;
        }
        //Else all inputs are correct... submit the user

        LocalDateTime birthday = null;
        try {
            birthday = InputVerification.dateWithoutTime(dateString);
        } catch (IllegalUserInputException e) {
            e.printStackTrace();
        }

        Observable<User> response = userPersistence.createUser(firstName, lastName, email, nickname, birthday, "picUrl", "About me", password);
        response.subscribeOn(Schedulers.io())
                .subscribe(user -> {
                            //User registered successfully -> redirect him to the main activity
                            SessionManager.getInstance().setUserId(user.getId());
                            Intent intent = new Intent(this, LoginActivity.class);
                            intent.addCategory("register_success");
                            startActivity(intent);
                            Log.v("Registration", "New user created: " + user.getNick_name() + "   " + user.getId());
                        },//Handle the error
                        (error) -> {
                            Log.v("Registration", "User POST error: " + error.getMessage());
                            runOnUiThread(() -> Toast.makeText(this, "There was a server error setting up your account :/", Toast.LENGTH_SHORT).show());
                        });
    }

    private List<String> validateUserInputs(String firstName, String lastName, String nickname, String email, String dateString, String password, String password2) {
        List<String> errorMessages = new ArrayList<>();

        //Just a few string not empty checks
        try {
            InputVerification.stringNotEmpty(firstName);
        } catch (IllegalUserInputException e) {
            errorMessages.add("Please specify a first name");
        }

        try {
            InputVerification.stringNotEmpty(lastName);
        } catch (IllegalUserInputException e) {
            errorMessages.add("Please specify a last name");
        }

        try {
            InputVerification.stringNotEmpty(nickname);
        } catch (IllegalUserInputException e) {
            errorMessages.add("Please specify a nickname");
        }

        //Checking date and age
        LocalDateTime birthday = null;
        try {
            birthday = InputVerification.dateWithoutTime(dateString);
            if (birthday.plusYears(18).isAfter(LocalDateTime.now()))
                errorMessages.add("You must be at least 18 years old to register");

        } catch (IllegalUserInputException e) {
            errorMessages.add("The date format is invalid");
        }

        try {
            InputVerification.stringNotEmpty(email);
        } catch (IllegalUserInputException e) {
            errorMessages.add("Please specify an email address");
        }


        //Verifying the passwords
        try {
            InputVerification.stringNotEmpty(password);
            InputVerification.stringNotEmpty(password2);
            if (password.length() < 8) {
                errorMessages.add("Password needs to be at least 8 characters");
            } else if (!password.equals(password2)) {
                errorMessages.add("Passwords need to be equal");
            }

        } catch (IllegalUserInputException e) {
            errorMessages.add("Please enter a password ");
        }

        return errorMessages;
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