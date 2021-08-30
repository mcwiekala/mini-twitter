package io.micw.twitter

import spock.lang.Specification
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

class UserSpec extends Specification {

    User bob = new User("Bob");
    User ann = new User("Ann");
    User kate = new User("Kate");

    def "Bob likes Ann and Kate"() {
        when:
        bob.likeUser(ann)
        bob.likeUser(kate)
        then:
        bob.getFollowers().size() == 0
        bob.getUsersToFollow().size() == 2
        bob.getUsersToFollow().contains(ann)
        bob.getUsersToFollow().contains(kate)
        kate.getFollowers().size() == 1
        kate.getUsersToFollow().size() == 0
        ann.getFollowers().size() == 1
        ann.getUsersToFollow().size() == 0
    }

    def "Bob not like Ann anymore"() {
        given:
        bob.likeUser(ann)
        bob.likeUser(kate)
        expect:
        that bob.getUsersToFollow(), hasItem(ann)
        that bob.getUsersToFollow(), hasItem(kate)
        when:
        bob.unlikeUser(ann)
        then:
        expect bob.getUsersToFollow(), hasSize(1)
        expect bob.getUsersToFollow(), not(hasItem(ann))
        expect bob.getUsersToFollow(), hasItem(kate)
    }

    def "Ann writes some tweets"() {
        when:
        Tweet tweet1 = ann.writeTweet("Hello!")
        Tweet tweet2 = ann.writeTweet("IT")
        Tweet tweet3 = ann.writeTweet("IS")
        Tweet tweet4 = ann.writeTweet("ME! ;)")
        then:
        expect ann.getNewsFeed(), hasSize(4)
        expect ann.getNewsFeed(), hasItem(tweet1)
        expect ann.getNewsFeed(), hasItem(tweet2)
        expect ann.getNewsFeed(), hasItem(tweet3)
        expect ann.getNewsFeed(), hasItem(tweet4)
    }

    def "Bob follows Ann and Kate messages"() {
        given:
        bob.likeUser(ann)
        expect:
        that bob.getNewsFeed(), hasSize(0)
        when:
        bob.writeTweet("Hello World! It's Bob!")
        ann.writeTweet("Hello I'm Ann")
        ann.writeTweet("Ups I did it again!")
        bob.writeTweet("I like F1!! #Verstappen")
        then:
        println bob.getNewsFeed()
        expect bob.getNewsFeed(), hasSize(4)
        when: 'Bob started to follow Kate'
        Tweet tweetNotVisibleForBob = kate.writeTweet("Hi this is Kate!")
        bob.likeUser(kate)
        Tweet tweetVisibleForBob = kate.writeTweet("I like cats!")
        then: 'there is 5 messages! The first message from kate is not on Bob\' feed'
        expect bob.getNewsFeed(), hasSize(5)
        expect bob.getNewsFeed(), hasItem(tweetVisibleForBob)
        expect bob.getNewsFeed(), not(hasItem(tweetNotVisibleForBob))
    }

}
