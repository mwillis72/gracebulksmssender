package devsam.com.gracesmssender.util

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment



fun Fragment.getMutedColor(@ColorRes color: Int) = requireActivity().getMutedColor(color)

fun Fragment.getResourceString(@StringRes stringResource: Int): String =
    requireActivity().getResourceString(stringResource)
