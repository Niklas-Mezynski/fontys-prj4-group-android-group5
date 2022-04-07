package org.die6sheeshs.projectx;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.LocalDateTimeConverter;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    private View view;
    private TextInputLayout firstNameField;
    private TextInputLayout lastNameField;
    private TextInputLayout nicknameField;
    private TextInputLayout birthdateField;
    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private TextInputLayout password2Field;
    private Button gotoToLogin;
    private Button submit;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_register, container, false);
        firstNameField = view.findViewById(R.id.first_name_field);
        lastNameField = view.findViewById(R.id.last_name_field);
        nicknameField = view.findViewById(R.id.nick_name_field);
        birthdateField = view.findViewById(R.id.birthdate_field);
        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        passwordField = view.findViewById(R.id.password_repeat_field);
        gotoToLogin = view.findViewById(R.id.goto_login_view);
        submit = view.findViewById(R.id.submit_registration);

        //TODO verify the inputs
        UserPersistence userPersistence = new UserPersistence();
        submit.setOnClickListener(currentView -> {

//            String firstName = firstNameField.getEditText().getText().toString();
//            String lastName = firstNameField.getEditText().getText().toString();
//            String nickname = firstNameField.getEditText().getText().toString();
//            String email = firstNameField.getEditText().getText().toString();
//            String password1 = firstNameField.getEditText().getText().toString();
//            String password2 = firstNameField.getEditText().getText().toString();
//            String dateString = birthdateField.getEditText().getText().toString();
//            Date date = Date.valueOf(dateString);
            Call<List<User>> allUsers = userPersistence.getAllUsers();
            allUsers.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        Log.e("API Response", String.valueOf(response.code()));
                        return;
                    }

                    List<User> body = response.body();
                    for (User user : body) {
                        Log.v("Snens", user.getNick_name());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("Failure getting users", t.toString());
                }
            });
            LocalDateTime birthday = LocalDateTime.of(2002, 04, 19, 10, 0, 0);
            userPersistence.createUser("Daniel", "Weinstein", "werofgk@kfw.de",
                    "MCDanDanHD", birthday, "someUrl",
                    "im cool", "123456789");


        });

        return view;
    }
}