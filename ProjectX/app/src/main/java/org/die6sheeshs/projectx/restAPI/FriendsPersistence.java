package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Friend;

import java.util.List;

import io.reactivex.Observable;

public class FriendsPersistence implements RetrofitPersistence {

    private static final FriendsPersistence instance = new FriendsPersistence();

    public static FriendsPersistence getInstance() {
        return instance;
    }

    private FriendsApi friendsApi;

    private FriendsPersistence() {
        this.friendsApi = RetrofitService.getInstance().getRetrofitClient().create(FriendsApi.class);
        RetrofitService.getInstance().addPersistence(this);

    }

    public Observable<List<Friend>> getFriendsOfUser(String id){
        return this.friendsApi.getFriendsOfUser(id);
    }

    public Observable<Friend> getFriendByNickName(String nickName){
        return this.friendsApi.getFriendByNickName(nickName);
	}
	
    public Observable<Void> deleteFriend(String userId, String friendId) {
        return this.friendsApi.deleteFriend(userId, friendId);
    }

    @Override
    public void refreshApi() {
        this.friendsApi = RetrofitService.getInstance().getRetrofitClient().create(FriendsApi.class);
    }
}

