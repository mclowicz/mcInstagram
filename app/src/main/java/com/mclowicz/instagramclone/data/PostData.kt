package com.mclowicz.instagramclone.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostData(
    var postId: String? = null,
    var userId: String? = null,
    var username: String? = null,
    var userImage: String? = null,
    var postImage: String? = null,
    var postDescription: String? = null,
    var time: Long? = null,
    var likes: List<String>? = null,
    var searchTerms: List<String>? = null
): Parcelable