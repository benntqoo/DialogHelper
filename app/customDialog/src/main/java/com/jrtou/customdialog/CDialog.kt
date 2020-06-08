package com.jrtou.customdialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class CDialog : DialogFragment(), IDialog, View.OnClickListener {
    companion object {
        private const val TAG = "CDialog"
        private const val KEY_PARAM = "layoutParam"

        fun newInstance(layoutParam: LayoutParam) = CDialog().run {
            arguments = Bundle().apply { putParcelable(KEY_PARAM, layoutParam) }
        }
    }

    var layoutParam: LayoutParam? = null

    private var dialogView: View? = null
    var customView: View? = null


    var title: String = ""
    var message: String = ""

    private var buildListener: IDialog.OnBuildListener? = null
    private var dismissListener: IDialog.OnDismissListener? = null

    private var isShowPositive: Boolean = false
    private var positiveButtonListener: IDialog.OnClickListener? = null
    private var positiveText: String = ""

    private var isShowNegative: Boolean = false
    private var negativeButtonListener: IDialog.OnClickListener? = null
    private var negativeText: String = ""

    private var checkDataListener: IDialog.onCheckDataListener? = null


    override fun dismissDialog() {
        TODO("Not yet implemented")
    }

    override fun showDialog(fragmentManager: FragmentManager, tag: String?) {
        TODO("Not yet implemented")
    }

    override fun getDialogView(): View? {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}