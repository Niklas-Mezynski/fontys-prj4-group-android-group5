package org.die6sheeshs.projectx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;

public class ProfileNotificationsTab extends Fragment {

    private ConstraintLayout notificationLayout;

    public static ProfileNotificationsTab getInstance() {
        return new ProfileNotificationsTab();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_notifications, container, false);
        /*TicketRequestListItem ticketRequestListItem = TicketRequestListItem.newInstance(null);
        notificationLayout = view.findViewById(R.id.notifications_layout);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().add(ticketRequestListItem, "noti1");*/
        return view;
    }
}
