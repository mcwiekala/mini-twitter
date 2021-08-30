package io.micw.twitter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Getter
@EqualsAndHashCode
class User {

    static final String DELIMITER = ", ";

    User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    final private UUID id;
    private String name;

    private List<User> usersToFollow = new ArrayList<>();
    private List<User> followers = new ArrayList<>();

    private List<Tweet> myFeed = new ArrayList<>();
    private List<Tweet> newsFeed = new ArrayList<>();

    Tweet writeTweet(String tweet) {
        log.info(this.name + " writed: " + tweet);
        Tweet newTweet = new Tweet(tweet, this);
        myFeed.add(newTweet);
        newsFeed.add(newTweet);
        notifyFollowers(newTweet);
        return newTweet;
    }

    void likeUser(User user) {
        log.info(this.name + " liked " + user.name);
        followUser(user);
        user.registerFollower(this);
    }

    void unlikeUser(User user) {
        log.info(this.name + " unliked " + user.name);
        unfollowUser(user);
        user.unregisterFollower(this);
    }

    void followUser(User user) {
        log.info(this.name + " you started following: " + user.name);
        usersToFollow.add(user);
    }

    void unfollowUser(User user) {
        log.info(this.name + " you stopped following: " + user.name);
        usersToFollow.remove(user);
    }

    void registerFollower(User user) {
        log.info("Hey " + this.name + ", user " + user.name + " started following you!");
        followers.add(user);
    }

    void unregisterFollower(User user) {
        log.info("Hey " + this.name + ", user " + user.name + " stopped following you...");
        followers.remove(user);
    }

    void notifyFollowers(Tweet tweet) {
        log.info(this.name + " send notification about new tweet to his/her followers");
        followers.forEach(user -> user.handleTweetFromUserIFollow(tweet));
    }

    void handleTweetFromUserIFollow(Tweet message) {
        log.info("Hey " + this.name + " - " + message.getAuthorName() + " wrote a new tweet");
        newsFeed.add(message);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersToFollow=" + usersToFollow.stream().map(User::getName).collect(Collectors.toList()) +
                ", followers=" + followers.stream().map(User::getName).collect(Collectors.toList()) +
                ", myFeed=" + myFeed +
                ", newsFeed=" + newsFeed +
                '}';
    }
}
