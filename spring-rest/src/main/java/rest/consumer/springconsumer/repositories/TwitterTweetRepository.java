package rest.consumer.springconsumer.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rest.consumer.springconsumer.domain.TwitterTweet;

@Repository
public interface TwitterTweetRepository extends MongoRepository<TwitterTweet, String> {
}
