package rest.consumer.springconsumer.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import rest.consumer.springconsumer.domain.TwitterTweet;
import rest.consumer.springconsumer.dto.TwitterTweetDto;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;

public class TwitterTweetDtoDeserializer extends StdDeserializer<TwitterTweetDto> {

    public TwitterTweetDtoDeserializer() {
        this(null);
    }

    public TwitterTweetDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TwitterTweetDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        TwitterTweetDto dto = new TwitterTweetDto();

        JsonNode rootNode = jp.getCodec()
                .readTree(jp);

        JsonNode commentArray = rootNode
                .get("data");

        Iterator<JsonNode> itr = commentArray.elements();
        while (itr.hasNext()) {
            JsonNode tweetNode = itr.next();

            TwitterTweet tweet = new TwitterTweet();
            tweet.setBody(tweetNode.get("text").asText());
            tweet.setId(tweetNode.get("id").asText());

            String utcTime = tweetNode.get("created_at").asText();
            tweet.setCreatedAt(Date.from(Instant.parse(utcTime)));

            dto.getTweetList().add(tweet);
        }

        return dto;
    }
}