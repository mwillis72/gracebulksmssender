package devsam.com.gracesmssender.activities.main.fragments

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import devsam.com.gracesmssender.R
import devsam.com.gracesmssender.enums.TypeFaceEnum
import devsam.com.gracesmssender.fragments.BaseFragment
import devsam.com.gracesmssender.util.getTypeface
import kotlinx.android.synthetic.main.fragment_splash_screen.*


class SplashScreenFragment : BaseFragment() {

    companion object {
        fun getInstance() = SplashScreenFragment()
    }

    override fun getLayoutResId() = R.layout.fragment_splash_screen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        splashScreenLogoImageView.setImageResource(R.drawable.pro_sms_sender_logo)
        requireActivity().getTypeface(TypeFaceEnum.WELCOME_MESSAGE_TYPEFACE).also {
            splashScreenWelcomeMessageTextView.typeface = it
        }
    }

    override fun inOnCreateView(mRootView: View, container: ViewGroup?, savedInstanceState: Bundle?) {
    }
}
