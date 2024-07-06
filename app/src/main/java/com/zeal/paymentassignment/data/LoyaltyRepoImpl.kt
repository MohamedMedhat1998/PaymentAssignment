package com.zeal.paymentassignment.data

import android.content.ContentResolver
import android.net.Uri
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import com.zeal.paymentassignment.core.LoyaltyContract.AMOUNT_AFTER_DISCOUNT_PATH
import com.zeal.paymentassignment.core.LoyaltyContract.AUTHORITY
import com.zeal.paymentassignment.core.LoyaltyContract.CursorColumns.ADJUSTED_AMOUNT
import com.zeal.paymentassignment.core.LoyaltyContract.CursorColumns.RESULT
import com.zeal.paymentassignment.core.LoyaltyContract.INCREASE_CARD_PURCHASES_COUNT
import com.zeal.paymentassignment.core.LoyaltyContract.OP_SUCCESS
import com.zeal.paymentassignment.core.LoyaltyContract.QueryParameters.CARD_NUMBER
import com.zeal.paymentassignment.core.LoyaltyContract.QueryParameters.ORIGINAL_AMOUNT

class LoyaltyRepoImpl(
    private val contentResolver: ContentResolver
) : LoyaltyRepo {
    override fun getAmountAfterDiscount(cardNumber: String, amount: Float): Float {
        val uri =
            Uri.parse("content://$AUTHORITY/$AMOUNT_AFTER_DISCOUNT_PATH")
                .buildUpon()
                .appendQueryParameter(CARD_NUMBER, cardNumber)
                .appendQueryParameter(ORIGINAL_AMOUNT, amount.toString())
                .build()

        val cursor =
            contentResolver.query(uri, null, null, null, null)

        return cursor?.use {
            it.moveToFirst()
            it.getFloatOrNull(it.getColumnIndexOrThrow(ADJUSTED_AMOUNT))
        } ?: amount
    }

    override fun increaseCardPurchasesCount(cardNumber: String): Boolean {
        val uri = Uri.parse("content://$AUTHORITY/$INCREASE_CARD_PURCHASES_COUNT")
            .buildUpon()
            .appendQueryParameter(CARD_NUMBER, cardNumber)
            .build()

        val cursor = contentResolver.query(uri, null, null, null, null)

        return cursor?.use {
            it.moveToFirst()
            when (it.getIntOrNull(it.getColumnIndexOrThrow(RESULT))) {
                OP_SUCCESS -> true
                else -> false
            }
        } ?: false
    }
}