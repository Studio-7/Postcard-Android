package com.studioseven.postcard.Models

class Postcard(
    var userName: String,
    var location: String,
    var numOfLikes: Int,
    var numOfComments: Int,
    var hashTagList: List<String>,
    var timeAgo: String,
    var isLiked: Boolean,
    var mediaLinkList: List<String>,
    var userImageLink: String
)