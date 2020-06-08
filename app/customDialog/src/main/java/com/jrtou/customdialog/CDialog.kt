package com.jrtou.customdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class CDialog : DialogFragment(), IDialog, View.OnClickListener {
    companion object {
        private const val TAG = "CDialog"
        private const val KEY_PARAM = "layoutParam"

        fun newInstance(layoutParam: LayoutParam): CDialog = CDialog().apply {
            arguments = Bundle().apply { putParcelable(KEY_PARAM, layoutParam) }
        }
    }

    var layoutParam: LayoutParam? = null

    private var dialogView: View? = null
    var customView: View? = null


    var title: String = ""
    var message: String = ""

    var buildListener: IDialog.OnBuildListener? = null
    var dismissListener: IDialog.OnDismissListener? = null

    var isShowPositive: Boolean = false
    var positiveButtonListener: IDialog.OnClickListener? = null
    var positiveText: String = ""

    var isShowNegative: Boolean = false
    var negativeButtonListener: IDialog.OnClickListener? = null
    var negativeText: String = ""

    var checkDataListener: IDialog.onCheckDataListener? = null

    @AnimRes
    var animRes: Int = 0

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_PARAM, layoutParam)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.custom_dialog, container, false)
        savedInstanceState?.let { dismissDialog() } ?: arguments?.let { layoutParam = it.getParcelable("layoutParam") }

        dialogView?.let { view ->
            layoutParam?.let { layoutParam ->
                if (layoutParam.customViewId != 0) {
                    val customContent: RelativeLayout = view.findViewById(R.id.ucaDialogView)
                    customView = LayoutInflater.from(context).inflate(layoutParam.customViewId, customContent, true)
                    customView?.let { buildListener?.onCustomViewCreate(this, it, layoutParam.customViewId) }
                }
            }
        }

        return dialogView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)//不顯示 title
            it.setCancelable(isCancelable)//false 屏蔽物理返回按鈕
            it.setCanceledOnTouchOutside(layoutParam?.isCancelableOutside ?: false)//點擊 dialog 外不消失
            it.setOnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN && !isCancelable }
        }

        layoutParam?.let { layoutParam ->
            if (!layoutParam.isCustomView) {
                val titleView: TextView = view.findViewById(R.id.ucaDialogTvTitle)
                titleView.text = title

                val messageView: TextView = view.findViewById(R.id.ucaDialogTvMessage)
                messageView.text = message

                if (isShowPositive) {
                    val positiveButton: Button = view.findViewById(R.id.ucaDialogBtSubmit)
                    positiveButton.text = positiveText
                    positiveButton.setOnClickListener(this)
                    positiveButton.visibility = View.VISIBLE
                }

                if (isShowNegative) {
                    val negativeButton: Button = view.findViewById(R.id.ucaDialogBtCancel)
                    negativeButton.text = negativeText
                    negativeButton.setOnClickListener(this)
                    negativeButton.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { viewWindow ->
            viewWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//設置背景透明
            if (animRes > 0) viewWindow.setWindowAnimations(animRes)//設置動畫

            layoutParam?.let { layoutParam ->
                val params = viewWindow.attributes
                params.width = layoutParam.width//設置寬度
                params.height = layoutParam.height//設置高度

                params.dimAmount = layoutParam.dimAmount//透明度設置 0.0f - 1.0 f
                params.gravity = layoutParam.gravity
                viewWindow.attributes = params
            }
        }
    }

    override fun onDestroy() {
        dismissListener?.onDismiss(this)
        super.onDestroy()
    }

    override fun showDialog(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        fragmentManager.findFragmentByTag(tag)?.also {
            Log.d(TAG, "移除已存在 dialog: $it")
            if (it is CDialog) it.dismissDialog()
            transaction.remove(it)
        } ?: let { Log.e(TAG, "showDialog: 無顯示 dialog") }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun onClick(v: View?) {
        checkDataListener?.let {
            when (v?.id) {
                R.id.ucaDialogBtSubmit -> {
                    if (it.onCheckData(this)) {
                        dismissDialog()
                        positiveButtonListener?.onClick(this)
                    } else Log.d(TAG, "onCheckData false: ")
                }
                R.id.ucaDialogBtCancel -> {
                    dismissDialog()
                    negativeButtonListener?.onClick(this)
                }
                else -> {
                }
            }
        } ?: run {
            dismissDialog()
            when (v?.id) {
                R.id.ucaDialogBtSubmit -> positiveButtonListener?.onClick(this)
                R.id.ucaDialogBtCancel -> negativeButtonListener?.onClick(this)
                else -> {
                }
            }
        }
    }

    override fun getDialogView(): View? = dialogView
}