package rest.consumer.springconsumer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import rest.consumer.springconsumer.domain.TwitterTweet;
import rest.consumer.springconsumer.utils.TwitterTweetDtoDeserializer;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize(using = TwitterTweetDtoDeserializer.class)
public class TwitterTweetDto {

    private List<TwitterTweet> tweetList;

    public TwitterTweetDto() {
        tweetList = new ArrayList<>();
    }
}
