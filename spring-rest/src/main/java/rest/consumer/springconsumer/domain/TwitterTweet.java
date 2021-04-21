package rest.consumer.springconsumer.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "twitterTweets")
public class TwitterTweet {

    @Id
    private String id;

    private Date createdAt;
    private Date downloadedAt;

    private String body;
}
