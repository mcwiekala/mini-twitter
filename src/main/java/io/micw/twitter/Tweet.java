package io.micw.twitter;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@ToString
@EqualsAndHashCode
class Tweet {

    UUID uuid;
    UUID authorID;
    String authorName;
    String text;

    Tweet(String text, User author) {
        this.uuid = UUID.randomUUID();
        this.authorID = author.getId();
        this.authorName = author.getName();
        this.text = text;
    }
}
