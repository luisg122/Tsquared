package com.example.tsquared.NewsWebView

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.rometools.rome.feed.synd.SyndFeed
import kotlinx.android.parcel.Parcelize


val DELIMITERS = arrayOf(" ", "-", "&", ":", "|")

@Parcelize
@Entity(tableName = "feeds",
    indices = [(Index(value = ["groupId"])), (Index(value = ["feedId", "feedLink"], unique = true))],
    foreignKeys = [(ForeignKey(entity = Feed::class,
        parentColumns = ["feedId"],
        childColumns = ["groupId"],
        onDelete = ForeignKey.CASCADE))])
data class Feed(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "feedId")
    var id: Long = 0L,
    @ColumnInfo(name = "feedLink")
    var link: String = "",
    @ColumnInfo(name = "feedTitle")
    var title: String? = null,
    @ColumnInfo(name = "feedImageLink")
    var imageLink: String? = null,
    var fetchError: Boolean = false,
    var retrieveFullText: Boolean = false,
    var isGroup: Boolean = false,
    var groupId: Long? = null,
    var displayPriority: Int = 0,
    @Deprecated("Not used anymore")
    var lastManualActionUid: String = "") : Parcelable {

    companion object {

        const val ALL_ENTRIES_ID = -1L

        fun getLetterDrawable(feedId: Long, feedTitle: String?, rounded: Boolean = false): TextDrawable {
            val feedName = feedTitle.orEmpty()

            val color = ColorGenerator.MATERIAL.getColor(feedId) // The color is specific to the feedId (which shouldn't change)
            val lettersForName = getLettersForName(feedName)
            return if (rounded) {
                TextDrawable.builder().buildRound(lettersForName, color)
            } else {
                TextDrawable.builder().buildRect(lettersForName, color)
            }
        }

        internal fun getLettersForName(feedName: String): String {
            val split = feedName.split(*DELIMITERS).filter { it != "" }     // filtering empty strings that occur when multiple delimiters are matched, e. g. colon-whitespace: ": "

            val letters = when {
                split.size >= 2 -> String(charArrayOf(split[0][0], split[1][0]))    // first letter of first and second word
                split.isEmpty() -> ""
                else -> split[0][0].toString()
            }

            return letters.toUpperCase()
        }
    }

    fun update(feed: SyndFeed) {
        if (title == null) {
            title = feed.title
        }

        if (feed.image?.url != null) {
            imageLink = feed.image?.url
        }

        // no error anymore since we just got a feedWithCount
        fetchError = false
    }

    fun getLetterDrawable(rounded: Boolean = false): TextDrawable {
        return getLetterDrawable(id, title, rounded)
    }
}