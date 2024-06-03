package com.app.shopfee.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.adapter.VoucherAdapter
import com.app.shopfee.event.VoucherSelectedEvent
import com.app.shopfee.model.Voucher
import com.app.shopfee.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class VoucherActivity : BaseActivity() {

    private var rcvVoucher: RecyclerView? = null
    private var listVoucher: MutableList<Voucher>? = null
    private var voucherAdapter: VoucherAdapter? = null
    private var amount = 0
    private var voucherSelectedId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)
        getDataIntent()
        initToolbar()
        initUi()
        getListVoucherFromFirebase()
    }

    private fun getDataIntent() {
        val bundle = intent.extras ?: return
        voucherSelectedId = bundle.getInt(Constant.VOUCHER_ID, 0)
        amount = bundle.getInt(Constant.AMOUNT_VALUE, 0)
    }

    private fun initUi() {
        rcvVoucher = findViewById(R.id.rcv_voucher)
        val linearLayoutManager = LinearLayoutManager(this)
        rcvVoucher?.layoutManager = linearLayoutManager

        listVoucher = ArrayList()
        voucherAdapter = VoucherAdapter(this, listVoucher, amount,
                object : VoucherAdapter.IClickVoucherListener {
            override fun onClickVoucherItem(voucher: Voucher) {
                handleClickVoucher(voucher)
            }
        })
        rcvVoucher?.adapter = voucherAdapter
    }

    private fun initToolbar() {
        val imgToolbarBack = findViewById<ImageView>(R.id.img_toolbar_back)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        imgToolbarBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = getString(R.string.title_voucher)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListVoucherFromFirebase() {
        showProgressDialog(true)
        MyApplication[this].getVoucherDatabaseReference()
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showProgressDialog(false)

                    resetListVoucher()
                    for (dataSnapshot in snapshot.children) {
                        val voucher = dataSnapshot.getValue(Voucher::class.java)
                        if (voucher != null) {
                            listVoucher!!.add(0, voucher)
                        }
                    }

                    if (voucherSelectedId > 0 && listVoucher != null && listVoucher!!.isNotEmpty()) {
                        for (voucher in listVoucher!!) {
                            if (voucher.id == voucherSelectedId) {
                                voucher.isSelected = true
                                break
                            }
                        }
                    }
                    voucherAdapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    showProgressDialog(false)
                    showToastMessage(getString(R.string.msg_get_date_error))
                }
            })
    }

    private fun resetListVoucher() {
        if (listVoucher != null) {
            listVoucher!!.clear()
        } else {
            listVoucher = ArrayList()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleClickVoucher(voucher: Voucher) {
        EventBus.getDefault().post(VoucherSelectedEvent(voucher))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        voucherAdapter?.release()
    }
}