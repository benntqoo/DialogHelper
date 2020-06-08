package com.jrtou.customdialog

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager

interface IDialog {
    /**
     * 關閉 dialog
     */
    fun dismissDialog()

    /**
     * 開啟dialog
     */
    fun showDialog(fragmentManager: FragmentManager, tag: String? = IDialog::class.java.simpleName)

    fun getDialogView(): View?

    /**
     * 客製化 view 回調
     */
    interface OnBuildListener {
        fun onCustomViewCreate(dialog: IDialog, view: View, @LayoutRes layoutRes: Int)
    }

    interface onCheckDataListener {
        fun onCheckData(dialog: IDialog): Boolean
    }

    /**
     * click 接口
     */
    interface OnClickListener {
        fun onClick(dialog: IDialog)
    }

    /**
     * dialog dismiss　回調
     */
    interface OnDismissListener {
        fun onDismiss(dialog: IDialog)
    }
}