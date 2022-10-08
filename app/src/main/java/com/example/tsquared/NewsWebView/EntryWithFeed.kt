package com.example.tsquared.NewsWebView

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.android.parcel.Parcelize

@Parcelize
class EntryWithFeed(
    @Embedded
    var entry: Entry,
    var feedTitle: String? = null,
    var feedLink: String = "",
    var feedImageLink: String? = null,
    var groupId: String? = null) : Parcelable
