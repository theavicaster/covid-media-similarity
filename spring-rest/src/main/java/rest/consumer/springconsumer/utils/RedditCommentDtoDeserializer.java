package rest.consumer.springconsumer.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import rest.consumer.springconsumer.domain.RedditComment;
import rest.consumer.springconsumer.dto.RedditCommentDto;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

public class RedditCommentDtoDeserializer extends StdDeserializer<RedditCommentDto> {

    public RedditCommentDtoDeserializer() {
        this(null);
    }

    public RedditCommentDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RedditCommentDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        RedditCommentDto dto = new RedditCommentDto();

        JsonNode rootNode = jp.getCodec()
                .readTree(jp);

        JsonNode commentArray = rootNode
                .get("data")
                .get("children");

        Iterator<JsonNode> itr = commentArray.elements();
        while (itr.hasNext()) {
            JsonNode commentNode = itr.next().
                    get("data");

            RedditComment comment = new RedditComment();

            comment.setBody(commentNode.get("body").asText());
            comment.setPermalink(commentNode.get("permalink").asText());
            comment.setLinkTitle(commentNode.get("link_title").asText());
            comment.setId(commentNode.get("id").asText());

            Double utcTime = (Double) commentNode.get("created_utc").numberValue();
            comment.setCreatedAt(new Date((long) (utcTime * 1000)));

            dto.getCommentList().add(comment);
        }

        return dto;
    }
}