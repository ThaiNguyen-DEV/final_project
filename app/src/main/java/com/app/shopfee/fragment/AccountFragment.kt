package com.app.shopfee.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.shopfee.R
import com.app.shopfee.activity.*
import com.app.shopfee.prefs.DataStoreManager
import com.app.shopfee.utils.GlobalFunction
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private var mView: View? = null
    private var layoutFeedback: LinearLayout? = null
    private var layoutContact: LinearLayout? = null
    private var layoutChangePassword: LinearLayout? = null
    private var layoutSignOut: LinearLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_account, container, false)
        initToolbar()
        initUi()
        initListener()
        return mView
    }

    private fun initToolbar() {
        val imgToolbarBack = mView?.findViewById<ImageView>(R.id.img_toolbar_back)
        val tvToolbarTitle = mView?.findViewById<TextView>(R.id.tv_toolbar_title)
        imgToolbarBack?.setOnClickListener { backToHomeScreen() }
        tvToolbarTitle?.text = getString(R.string.nav_account)
    }

    private fun backToHomeScreen() {
        val mainActivity = activity as MainActivity? ?: return
        mainActivity.viewPager2?.currentItem = 0
    }

    private fun initUi() {
        val tvUsername = mView?.findViewById<TextView>(R.id.tv_username)
        tvUsername?.text = DataStoreManager.user?.email
        layoutFeedback = mView?.findViewById(R.id.layout_feedback)
        layoutContact = mView?.findViewById(R.id.layout_contact)
        layoutChangePassword = mView?.findViewById(R.id.layout_change_password)
        layoutSignOut = mView?.findViewById(R.id.layout_sign_out)
    }

    private fun initListener() {
        layoutFeedback?.setOnClickListener {
            GlobalFunction.startActivity(
                activity, FeedbackActivity::class.java
            )
        }
        layoutContact?.setOnClickListener {
            GlobalFunction.startActivity(
                activity, ContactActivity::class.java
            )
        }
        layoutChangePassword?.setOnClickListener {
            GlobalFunction.startActivity(
                activity, ChangePasswordActivity::class.java
            )
        }
        layoutSignOut?.setOnClickListener { onClickSignOut() }
    }

    private fun onClickSignOut() {
        if (activity == null) return
        FirebaseAuth.getInstance().signOut()
        DataStoreManager.user = null
        GlobalFunction.startActivity(activity, LoginActivity::class.java)
        activity!!.finishAffinity()
    }
}