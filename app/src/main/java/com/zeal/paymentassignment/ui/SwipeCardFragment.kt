package com.zeal.paymentassignment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zeal.paymentassignment.MainViewModel
import com.zeal.paymentassignment.R
import com.zeal.paymentassignment.core.DialogHelper
import com.zeal.paymentassignment.core.FlowDataObject
import com.zeal.paymentassignment.databinding.FragmentSwipeFragment2Binding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SwipeCardFragment : Fragment() {

    private val viewModel by activityViewModel<MainViewModel>()

    private val binding by lazy {
        FragmentSwipeFragment2Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        DialogHelper.showPanDialog(requireContext(), { cardNumber ->
            Thread {
                // Check available discounts
                DialogHelper.showLoadingDialog(requireActivity(), "Checking Loyalty")
                Thread.sleep(5000)

                val oldAmount = FlowDataObject.getInstance().amount
                val newAmount = viewModel.getAmountAfterDiscount(cardNumber, oldAmount)

                if (newAmount != oldAmount) {
                    // This means that there is an available discount
                    // Setting the amount to the new amount
                    FlowDataObject.getInstance().amount = newAmount
                    DialogHelper.showLoadingDialog(
                        requireActivity(),
                        "Applying Discount\n" +
                                "OldPrice: $oldAmount\n" +
                                "NewPrice: ${FlowDataObject.getInstance().amount}"
                    )
                    Thread.sleep(5000)

                    // If the amount is 0, no need to call the bank
                    if (newAmount == 0f) {
                        // Proceed without calling the bank
                        DialogHelper.showLoadingDialog(
                            requireActivity(),
                            "Executing the operation without calling the bank"
                        )
                    } else {
                        // If it is not 0, we want to call the bank with the updated amount
                        DialogHelper.showLoadingDialog(
                            requireActivity(),
                            "Sending Transaction to The Bank\n" +
                                    "amount: ${FlowDataObject.getInstance().amount}"
                        )
                        Thread.sleep(5000)
                        DialogHelper.showLoadingDialog(requireActivity(), "Receiving Bank Response")
                    }
                } else {
                    // No available discount, proceed with the amount and call the bank
                    DialogHelper.showLoadingDialog(
                        requireActivity(), "Sending Transaction to The Bank\n" +
                                "amount: ${FlowDataObject.getInstance().amount}"
                    )
                    Thread.sleep(5000)
                    DialogHelper.showLoadingDialog(requireActivity(), "Receiving Bank Response")
                }
                Thread.sleep(5000)

                // Increasing card usage history
                DialogHelper.showLoadingDialog(requireActivity(), "Updating history")
                viewModel.increaseCardPurchasesCount(cardNumber)

                Thread.sleep(5000)
                DialogHelper.hideLoading(requireActivity())
                requireActivity().runOnUiThread {
                    findNavController().navigate(R.id.action_swipeCardFragment_to_printReceiptFragment)
                }
            }.start()
        }) {
            findNavController().popBackStack()
        }
        return binding.root
    }
}