package org.die6sheeshs.projectx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.TicketRequest;

import java.time.LocalDateTime;

public class ProfileNotificationsTab extends Fragment {

    private LinearLayout notificationLayout;
    private FragmentManager fragmentManager;

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
        notificationLayout = view.findViewById(R.id.notificationItems_layout);
        fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        TicketRequest ticketRequest = new TicketRequest(LocalDateTime.of(2022, 4, 14, 13, 58, 27), "12", "def");
        TicketRequestListItem ticketRequestListItem = NotificationListItemProvider.newTicketRequestListItem(ticketRequest);

        if (fragmentManager.findFragmentById(ticketRequestListItem.getId()) == null) {
            ft.add(notificationLayout.getId(), ticketRequestListItem);
            ft.commit();
        }

        return view;
    }
}
