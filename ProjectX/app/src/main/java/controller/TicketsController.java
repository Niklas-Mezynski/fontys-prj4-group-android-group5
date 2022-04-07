package controller;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
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
        LinearLayout linearLayout = findViewById(R.id.linlay);
        Button btnTag = new Button(this);
        btnTag.setLayoutParams(linearLayout.getLayoutParams());
        btnTag.setText("Button");


        //add button to the layout
        linearLayout.addView(btnTag);
    }
}
