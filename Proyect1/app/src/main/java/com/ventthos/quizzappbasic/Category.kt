package com.ventthos.quizzappbasic

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Category(
    @StringRes val categoryText: Int,
    @ColorRes val colorCode: Int,
    @DrawableRes val image: Int
) {
    SCIENCE(R.string.topic1, R.color.greenCorrect, R.drawable.science_icon),
    GEOGRAPHY(R.string.topic2, R.color.blue, R.drawable.geography_icon),
    HISTORY(R.string.topic3, R.color.yellow, R.drawable.history_icon),
    MOVIES(R.string.topic4, R.color.purple, R.drawable.pop),
    CULTURE(R.string.topic5, R.color.error_color, R.drawable.culture_icon)
}