package io.micw.twitter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@EqualsAndHashCode
@Getter
class Tweet {

    private final UUID uuid;
    private final UUID authorID;
    private String authorName;
    private String text;

    Tweet(String text, User author) {
        this.uuid = UUID.randomUUID();
        this.authorID = author.getId();
        this.authorName = author.getName();
        this.text = text;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getAuthorID() {
        return authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }
}
