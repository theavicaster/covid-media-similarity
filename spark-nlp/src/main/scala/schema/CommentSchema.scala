package text.similarity
package schema

import java.sql.Timestamp

case class CommentSchema(_id: String,
                         createdAt: Timestamp,
                         downloadedAt: Timestamp,
                         body: String,
                         _class: String,
                         linkTitle: String,
                         permalink: String)
