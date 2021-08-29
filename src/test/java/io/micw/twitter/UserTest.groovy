package io.micw.twitter

import spock.lang.Specification
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

class UserTest extends Specification {

    User bob = new User("Bob");
    User ann = new User("Ann");
    User kate = new User("Kate");

    def "Bob likes Ann and Kate"() {
        when:
        bob.likeUser(ann)
        bob.likeUser(kate)
        then:
        bob.followers.size() == 0
        bob.usersToFollow.size() == 2
        bob.usersToFollow.contains(ann)
        bob.usersToFollow.contains(kate)
        kate.followers.size() == 1
        kate.usersToFollow.size() == 0
        ann.followers.size() == 1
        ann.usersToFollow.size() == 0
    }

    def "Bob not like Ann anymore"() {
        given:
        bob.likeUser(ann)
        bob.likeUser(kate)
        expect:
        that bob.usersToFollow, hasItem(ann)
        that bob.usersToFollow, hasItem(kate)
        when:
        bob.unlikeUser(ann)
        then:
        expect bob.usersToFollow, hasSize(1)
        expect bob.usersToFollow, not(hasItem(ann))
        expect bob.usersToFollow, hasItem(kate)
    }

    def "Ann writes some tweets"() {
        when:
        Tweet tweet1 = ann.writeTweet("Hello!")
        Tweet tweet2 = ann.writeTweet("IT")
        Tweet tweet3 = ann.writeTweet("IS")
        Tweet tweet4 = ann.writeTweet("ME! ;)")
        then:
        expect ann.newsFeed, hasSize(4)
        expect ann.newsFeed, hasItem(tweet1)
        expect ann.newsFeed, hasItem(tweet2)
        expect ann.newsFeed, hasItem(tweet3)
        expect ann.newsFeed, hasItem(tweet4)
    }

    def "Bob follows Ann and Kate messages"() {
        given:
        bob.likeUser(ann)
        expect:
        that bob.newsFeed, hasSize(0)
        when:
        bob.writeTweet("Hello World! It's Bob!")
        ann.writeTweet("Hello I'm Ann")
        ann.writeTweet("Ups I did it again!")
        bob.writeTweet("I like F1!! #Verstappen")
        then:
        println bob.newsFeed
        expect bob.newsFeed, hasSize(4)
        when: 'Bob started to follow Kate'
        Tweet tweetNotVisibleForBob = kate.writeTweet("Hi this is Kate!")
        bob.likeUser(kate)
        Tweet tweetVisibleForBob = kate.writeTweet("I like cats!")
        then: 'there is 5 messages! The first message from kate is not on Bob\' feed'
        expect bob.newsFeed, hasSize(5)
        expect bob.newsFeed, hasItem(tweetVisibleForBob)
        expect bob.newsFeed, not(hasItem(tweetNotVisibleForBob))
    }
}
