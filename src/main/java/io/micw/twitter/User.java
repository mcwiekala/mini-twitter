package io.micw.twitter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Slf4j
class User {

    static final String DELIMITER = ", ";

    User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    private UUID id;
    private String name;

    private List<User> usersToFollow = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    private String latestTweet;
    private List<Tweet> myFeed = new ArrayList<>();
    private List<Tweet> newsFeed = new ArrayList<>();

    Tweet writeTweet(String tweet) {
        Tweet newTweet = new Tweet(tweet, this);
        myFeed.add(newTweet);
        newsFeed.add(newTweet);
        notifyFollowers(newTweet);
        return newTweet;
    }

    void likeUser(User user) {
        usersToFollow(user);
        user.registerFollower(this);
    }

    void unlikeUser(User user) {
        usersToUnfollow(user);
        user.unregisterFollower(this);
    }

    void usersToFollow(User user) {
        usersToFollow.add(user);
    }

    void usersToUnfollow(User user) {
        usersToFollow.remove(user);
    }

    void registerFollower(User user) {
        followers.add(user);
    }

    void unregisterFollower(User user) {
        followers.remove(user);
    }

    void notifyFollowers(Tweet tweet) {
        followers.forEach(user -> user.handleTweetFromUserIFollow(tweet));
    }
    
    void handleTweetFromUserIFollow(Tweet message) {
        newsFeed.add(message);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersToFollow=" + usersToFollow.stream().map(User::getName).collect(Collectors.toList()) +
                ", followers=" + followers.stream().map(User::getName).collect(Collectors.toList()) +
                ", latestTweet='" + latestTweet + '\'' +
                ", myFeed=" + myFeed +
                ", newsFeed=" + newsFeed +
                '}';
    }
}
