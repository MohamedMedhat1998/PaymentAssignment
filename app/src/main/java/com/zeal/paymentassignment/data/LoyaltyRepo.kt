package com.zeal.paymentassignment.data

interface LoyaltyRepo {
    fun getAmountAfterDiscount(cardNumber: String, amount: Float): Float
    fun increaseCardPurchasesCount(cardNumber: String): Boolean
}