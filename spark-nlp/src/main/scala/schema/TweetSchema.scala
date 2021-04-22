package text.similarity
package schema

import java.sql.Timestamp

case class TweetSchema(_id: String,
                       createdAt: Timestamp,
                       downloadedAt: Timestamp,
                       body: String,
                       _class: String)
