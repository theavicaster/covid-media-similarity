package rest.consumer.springconsumer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rest.consumer.springconsumer.domain.TwitterTweet;
import rest.consumer.springconsumer.repositories.TwitterTweetRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TwitterTweetService {

    private final TwitterTweetRepository twitterTweetRepository;

    public TwitterTweet saveTweet(TwitterTweet tweet) {

        tweet.setDownloadedAt(new Date());
        return twitterTweetRepository.save(tweet);
    }
}
