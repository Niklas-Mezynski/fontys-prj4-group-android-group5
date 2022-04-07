package controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import org.die6sheeshs.projectx.R;

public class TicketsController extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets);
        init();
    }

    private void init(){
        ScrollView scrollView = findViewById(R.id.scroll);
        LinearLayout linearLayout = findViewById(R.id.linlayV);
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(linearLayout.getLayoutParams());
        btnTag.setText("Button");


        //add button to the layout
        linearLayout.addView(btnTag);
    }
}
