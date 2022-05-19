package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.TicketRequest;
import org.die6sheeshs.projectx.restAPI.FriendsPersistence;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.TicketRequestPersistence;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ProfileNotificationsTab extends Fragment {

    private View view;
    private SwipeRefreshLayout refreshLayout;

    public static ProfileNotificationsTab newInstance(String userId) {
        ProfileNotificationsTab profileNotificationsTab = new ProfileNotificationsTab();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        profileNotificationsTab.setArguments(args);
        return profileNotificationsTab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_tab_notifications, container);
        FragmentManager fragmentManager = getChildFragmentManager();
        init(fragmentManager);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<Fragment> existingFrags = fragmentManager.getFragments();
                for (Fragment frag : existingFrags)
                {
                    fragmentManager.beginTransaction().remove(frag).commit();
                }
            }
        });
        return view;
    }

    private void init(FragmentManager fragmentManager) {
        LinearLayout notificationLayout = view.findViewById(R.id.notificationItems_layout);
        String userId = getArguments().getString("userId");

        Observable<List<Party>> partiesFromUser = PartyPersistence.getInstance().getPartiesFromUser(userId);
        partiesFromUser.subscribeOn(Schedulers.io())
                .subscribe(parties -> {
                    for (Party p : parties) {
                        Observable<List<TicketRequest>> ticketRequestsOfParty = TicketRequestPersistence.getInstance().getTicketRequestsOfParty(p.getId());
                        ticketRequestsOfParty.subscribeOn(Schedulers.io())
                                .subscribe(ticketRequests -> {
                                    for (TicketRequest tr : ticketRequests) {
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        TicketRequestListItem ticketRequestListItem = NotificationListItemProvider.newTicketRequestListItem(tr);
                                        ft.add(notificationLayout.getId(), ticketRequestListItem);
                                        ft.commit();
                                    }
                                });
                    }
                });


        /*FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        TicketRequest ticketRequest = new TicketRequest(LocalDateTime.of(2022, 4, 14, 13, 58, 27), "12", "def");
        TicketRequestListItem ticketRequestListItem = NotificationListItemProvider.newTicketRequestListItem(ticketRequest);

        if (fragmentManager.findFragmentById(ticketRequestListItem.getId()) == null) {
            ft.add(notificationLayout.getId(), ticketRequestListItem);
            ft.commit();
        }*/
    }
}
