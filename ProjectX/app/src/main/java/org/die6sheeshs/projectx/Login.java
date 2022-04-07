package org.die6sheeshs.projectx;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private Button submit;
    private EditText password;
    private EditText email;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        this.view = inflater.inflate(R.layout.fragment_login, container, false);
        submit = view.findViewById(R.id.buttonLogin);
        email = view.findViewById(R.id.loginEmailNickname);

        UserPersistence userPersistence = new UserPersistence();
        submit.setOnClickListener(listener -> {
            Call<List<User>> allUsers = userPersistence.getAllUsers();
            allUsers.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(!response.isSuccessful()){
                        Log.e("API Response: ", String.valueOf(response.code()));
                        return;
                    }

                    List<User> userList = response.body();
                    for (User user: userList){
                        Log.v("User found: ", user.getFirstName() + " " + user.getLastName());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("failed", t.toString());
                }
            });
        });


        return view;
    }
}