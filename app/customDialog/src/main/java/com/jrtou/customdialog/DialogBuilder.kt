package com.jrtou.customdialog

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

class DialogBuilder(private val activity: AppCompatActivity) {
    companion object {
        /**
         * Return the width of screen, in pixel.
         *
         * @return the width of screen, in pixel
         */
        fun getScreenWidth(@NonNull activity: Activity): Int {
            val windowManager: WindowManager =
                activity.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) windowManager.defaultDisplay.getRealSize(
                point
            )
            else windowManager.defaultDisplay.getSize(point)
            return point.x
        }

        /**
         * Return the width of screen, in pixel.
         *
         * @return the width of screen, in pixel
         */
        fun getScreenWidth(@NonNull activity: Context): Int {
            val windowManager: WindowManager =
                activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) windowManager.defaultDisplay.getRealSize(
                point
            )
            else windowManager.defaultDisplay.getSize(point)
            return point.x
        }
    }

    private val layoutParam: LayoutParam = LayoutParam()

    private var title: String = ""
    private var message: String = ""

    private var positiveText: String = ""
    private var positiveListener: IDialog.OnClickListener? = null
    private var isShowPositive: Boolean = false

    private var isShowNegative: Boolean = false
    private var negativeText: String = ""
    private var negativeListener: IDialog.OnClickListener? = null

    private var buildListener: IDialog.OnBuildListener? = null
    private var dismissListener: IDialog.OnDismissListener? = null
    private var checkDataListener: IDialog.onCheckDataListener? = null

    fun setTitle(@StringRes title: Int) = apply { this.title = activity.application.getString(title) }
    fun setTitle(title: String) = apply { this.title = title }

    fun setMessage(@StringRes message: Int) = apply { this.message = activity.resources.getString(message) }
    fun setMessage(message: String) = apply { this.message = message }

    fun setGravity(gravity: Int) = apply { this.layoutParam.gravity = gravity }

    fun setDialogView(@LayoutRes layoutRes: Int) = apply { this.layoutParam.customViewId = layoutRes }

    fun setCancelable(isCancelable: Boolean) = apply { this.layoutParam.isCancelableOutside = isCancelable }

    fun setDismissListener(dismissListener: IDialog.OnDismissListener) = apply { this.dismissListener = dismissListener }

    fun setCheckDataListener(checkDataListener: IDialog.onCheckDataListener?) = apply { this.checkDataListener = checkDataListener }

    /**
     * 設置 寬 為螢幕的多少比例
     * @param width range 0-1
     */
    fun setWidth(width: Float) = apply { this.layoutParam.width = (getScreenWidth(this.activity) * width).toInt() }

    /**
     * 設置 高 為螢幕的多少比例
     * @param height range 0-1
     */
    fun setHeight(height: Float) = apply { this.layoutParam.height = (getScreenWidth(this.activity) * height).toInt() }

    fun setSize(
        width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
        height: Int = WindowManager.LayoutParams.WRAP_CONTENT
    ) = apply {
        this.layoutParam.width = width
        this.layoutParam.height = height
    }

    /**
     * 客製化 ui listener
     */
    fun setBuildListener(buildListener: IDialog.OnBuildListener) = apply { this.buildListener = buildListener }

    fun setPositiveButton(@StringRes text: Int, listener: IDialog.OnClickListener? = null) = apply {
        this.positiveText = this.activity.resources.getString(text)
        this.positiveListener = listener
        this.isShowPositive = true
    }

    fun setPositiveButton(text: String, listener: IDialog.OnClickListener? = null) = apply {
        this.positiveText = text
        this.positiveListener = listener
        this.isShowPositive = true
    }

    fun setNegativeButton(@StringRes text: Int, listener: IDialog.OnClickListener? = null) = apply {
        this.negativeText = this.activity.resources.getString(text)
        this.negativeListener = listener
        this.isShowNegative = true
    }

    fun setNegativeButton(text: String, listener: IDialog.OnClickListener? = null) = apply {
        this.negativeText = text
        this.negativeListener = listener
        this.isShowNegative = true
    }

    fun create(): IDialog {
        val dialog = CDialog.newInstance(layoutParam)
        dialog.title = title
        dialog.message = message

        dialog.isShowPositive = isShowPositive
        dialog.positiveText = positiveText
        dialog.positiveButtonListener = positiveListener

        dialog.isShowNegative = isShowNegative
        dialog.negativeText = negativeText
        dialog.negativeButtonListener = negativeListener

        dialog.buildListener = buildListener
        dialog.dismissListener = dismissListener
        dialog.checkDataListener = checkDataListener

        return dialog
    }

    fun show(): IDialog {
        val dialog = create()
        dialog.showDialog(activity.supportFragmentManager)
        return dialog
    }
}