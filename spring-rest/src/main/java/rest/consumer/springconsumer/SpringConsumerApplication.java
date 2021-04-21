package rest.consumer.springconsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rest.consumer.springconsumer.domain.RedditComment;
import rest.consumer.springconsumer.domain.TwitterTweet;
import rest.consumer.springconsumer.dto.RedditCommentDto;
import rest.consumer.springconsumer.dto.TwitterTweetDto;
import rest.consumer.springconsumer.services.RedditCommentService;
import rest.consumer.springconsumer.services.TwitterTweetService;

@Component
@RequiredArgsConstructor
@SpringBootApplication
public class SpringConsumerApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConsumerApplication.class);

    private final RedditCommentService redditCommentService;
    private final TwitterTweetService twitterTweetService;

    private String TWITTER_BEARER_TOKEN;

    public static void main(String[] args) {
        SpringApplication.run(SpringConsumerApplication.class, args);
    }

    @Value("${twitter.token}")
    public void setTwitterBearerToken(String twitterBearerToken) {
        TWITTER_BEARER_TOKEN = twitterBearerToken;
    }

    @Override
    public void run(String... args) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders redditHeaders = new HttpHeaders();
        redditHeaders.set("User-Agent", "theavicaster");
        HttpEntity<String> redditEntity = new HttpEntity<>(redditHeaders);

        String[] subreddits = {"Coronavirus", "india", "news"};

        for (String subreddit : subreddits) {
            ResponseEntity<String> redditResponse = restTemplate.exchange(String.format("https://www.reddit.com/r/%s/comments.json?limit=100", subreddit),
                    HttpMethod.GET, redditEntity, String.class);

            RedditCommentDto redditDto = new ObjectMapper()
                    .readValue(redditResponse.getBody(), RedditCommentDto.class);

            for (RedditComment comment : redditDto.getCommentList()) {
                RedditComment savedComment = redditCommentService.saveComment(comment);
                LOGGER.info(savedComment.toString() + " was saved");
            }
        }


        HttpHeaders twitterHeaders = new HttpHeaders();
        twitterHeaders.set("Authorization", String.format("Bearer %s", TWITTER_BEARER_TOKEN));
        HttpEntity<String> twitterEntity = new HttpEntity<>(twitterHeaders);

        String[] searchTerms = {"coronavirus", "covid", "mask", "vaccine", "covid-19", "pandemic"};

        for (String searchTerm : searchTerms) {

            ResponseEntity<String> twitterResponse = restTemplate
                    .exchange(String.format("https://api.twitter.com/2/tweets/search/recent?query=%s&tweet.fields=created_at&max_results=25", searchTerm),
                            HttpMethod.GET, twitterEntity, String.class);

            TwitterTweetDto twitterDto = new ObjectMapper()
                    .readValue(twitterResponse.getBody(), TwitterTweetDto.class);

            for (TwitterTweet tweet : twitterDto.getTweetList()) {
                TwitterTweet savedTweet = twitterTweetService.saveTweet(tweet);
                LOGGER.info(savedTweet.toString() + " was saved");
            }
        }

    }
}
