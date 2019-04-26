package com.studioseven.postcard.Models

class Postcard(
    var userName: String,
    var location: String,
    var numOfLikes: String,
    var hashTagList: List<String>,
    var timeAgo: String,
    var isLiked: Boolean,
    var mediaLinkList: List<Image>,
    var userImageLink: String
)