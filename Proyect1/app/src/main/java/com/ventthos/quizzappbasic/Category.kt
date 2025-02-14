package com.ventthos.quizzappbasic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Category(
    @StringRes val categoryText: Int,
    val colorCode: String,
    @DrawableRes val image: Int
) {
    SCIENCE(R.string.topic1, "#FF5733", R.drawable.science),
    GEOGRAPHY(R.string.topic2, "#33FF57", R.drawable.geography),
    HISTORY(R.string.topic3, "#3357FF", R.drawable.history),
    MOVIES(R.string.topic4, "#3357FF", R.drawable.movies),
    CULTURE(R.string.topic5, "#3357FF", R.drawable.culture)
}