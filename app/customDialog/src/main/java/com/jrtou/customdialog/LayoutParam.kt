package com.jrtou.customdialog

import android.os.Parcelable
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.LayoutRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LayoutParam(
    var width: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    @LayoutRes var customViewId: Int = 0,
    var isCancelableOutside: Boolean = false,//是否點擊外部取消 dialog
    var dimAmount: Float = 0.2f,
    var gravity: Int = Gravity.CENTER,
    var isCustomView: Boolean = false//是否放置 客製化畫面
) : Parcelable