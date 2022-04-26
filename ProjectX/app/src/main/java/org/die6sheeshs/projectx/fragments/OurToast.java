package org.die6sheeshs.projectx.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;

public class OurToast extends Fragment {

    public OurToast() {

    }

    static void makeToast(String content, String color, int picture, Context context,LayoutInflater inflat){
        LayoutInflater inflater = inflat;
        View layout = inflater.inflate(R.layout.toast_layout,null);

        ImageView image = (ImageView) layout.findViewById(R.id.image);;
        image.setImageResource(picture);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(content);
        layout.setBackgroundColor(Color.parseColor(color));
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
