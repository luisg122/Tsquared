package com.example.tsquared.NewsWebView

import android.content.Context
import android.os.Parcelable
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.core.text.HtmlCompat
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tsquared.R
import com.rometools.rome.feed.synd.SyndEntry
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "entries",
    indices = [(Index(value = ["feedId"])), (Index(value = ["link"], unique = true))],
    foreignKeys = [(ForeignKey(entity = Feed::class,
        parentColumns = ["feedId"],
        childColumns = ["feedId"],
        onDelete = ForeignKey.CASCADE))])
data class Entry(@PrimaryKey
                 var id: String = "",
                 var feedId: Long = 0L,
                 var link: String? = null,
                 var fetchDate: Date = Date(),
                 var publicationDate: Date = fetchDate, // important to know if the publication date has been set
                 var title: String? = null,
                 var description: String? = null,
                 var mobilizedContent: String? = null,
                 var imageLink: String? = null,
                 var author: String? = null,
                 var read: Boolean = false,
                 var favorite: Boolean = false) : Parcelable {

    fun getReadablePublicationDate(context: Context): String =
        if (DateUtils.isToday(publicationDate.time)) {
            DateFormat.getTimeFormat(context).format(publicationDate)
        } else {
            DateFormat.getMediumDateFormat(context).format(publicationDate) + ' ' +
                    DateFormat.getTimeFormat(context).format(publicationDate)
        }
}

fun SyndEntry.toDbFormat(context: Context, feed: Feed): Entry {
    val item = Entry()
    item.id = (feed.id.toString() + "_" + (link ?: uri ?: title
    ?: UUID.randomUUID().toString())).sha1()
    item.feedId = feed.id
    if (title != null) {
        item.title = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    } else {
        item.title = context.getString(R.string.app_name)
    }
    item.description = contents.getOrNull(0)?.value ?: description?.value
    item.link = link

    enclosures?.forEach { if (it.type.contains("image")) { item.imageLink = it.url } }
    if (item.imageLink == null) {
        foreignMarkup?.forEach {
            if (it.namespace?.prefix == "media" && it.name == "content") {
                it.attributes.forEach { mc ->
                    if (mc.name == "url") item.imageLink = mc.value
                }
            }
        }
    }

    item.author = author

    val date = publishedDate ?: updatedDate
    item.publicationDate = if (date?.before(item.publicationDate) == true) date else item.publicationDate

    return item
}