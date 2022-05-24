package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Friend;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FriendsApi {

    @GET("/users/{id}/friends")
    public Observable<List<Friend>> getFriendsOfUser(@Path("id") String id);

    @GET("/users/search/{name}")
    public Observable<Friend> getFriendByNickName(@Path("name") String nickName);

}
