package devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import devsam.com.gracesmssender.R
import devsam.com.gracesmssender.fragments.BaseFragment


class AboutFragment : BaseFragment(){

    companion object {
        fun getInstance() = AboutFragment()
    }

    override fun getLayoutResId() = R.layout.fragment_about

    override fun inOnCreateView(mRootView: View, container: ViewGroup?, savedInstanceState: Bundle?) {
    }
}