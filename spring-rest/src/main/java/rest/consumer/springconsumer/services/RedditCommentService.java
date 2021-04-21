package rest.consumer.springconsumer.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rest.consumer.springconsumer.domain.RedditComment;
import rest.consumer.springconsumer.repositories.RedditCommentRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RedditCommentService {

    private final RedditCommentRepository redditCommentRepository;

    public RedditComment saveComment(RedditComment comment) {

        comment.setDownloadedAt(new Date());
        return redditCommentRepository.save(comment);
    }
}
