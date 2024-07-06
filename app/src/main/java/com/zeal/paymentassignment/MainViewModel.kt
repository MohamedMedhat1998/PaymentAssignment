package com.zeal.paymentassignment

import androidx.lifecycle.ViewModel
import com.zeal.paymentassignment.data.LoyaltyRepo

class MainViewModel(
    private val repo: LoyaltyRepo
) : ViewModel() {
    fun getAmountAfterDiscount(cardNumber: String, amount: Float) =
        repo.getAmountAfterDiscount(cardNumber, amount)

    fun increaseCardPurchasesCount(cardNumber: String) = repo.increaseCardPurchasesCount(cardNumber)
}