package com.app.shopfee.model

import java.io.Serializable

class RatingReview(var type: Int, var id: String) : Serializable {

    companion object {
        const val TYPE_RATING_REVIEW_DRINK = 1
        const val TYPE_RATING_REVIEW_ORDER = 2
    }
}