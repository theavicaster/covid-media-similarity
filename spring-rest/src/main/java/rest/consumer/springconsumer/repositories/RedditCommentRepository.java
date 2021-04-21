package rest.consumer.springconsumer.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rest.consumer.springconsumer.domain.RedditComment;

@Repository
public interface RedditCommentRepository extends MongoRepository<RedditComment, String> {
}
