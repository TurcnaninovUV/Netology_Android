package ru.netology.model

import ru.netology.dto.Post
import java.util.Collections.emptyList

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)