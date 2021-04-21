package rest.consumer.springconsumer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import rest.consumer.springconsumer.domain.RedditComment;
import rest.consumer.springconsumer.utils.RedditCommentDtoDeserializer;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize(using = RedditCommentDtoDeserializer.class)
public class RedditCommentDto {

    private List<RedditComment> commentList;

    public RedditCommentDto() {
        commentList = new ArrayList<>();
    }
}
