package com.app.shopfee.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.MyApplication
import com.app.shopfee.R
import com.app.shopfee.adapter.AddressAdapter
import com.app.shopfee.adapter.AddressAdapter.IClickAddressListener
import com.app.shopfee.event.AddressSelectedEvent
import com.app.shopfee.model.Address
import com.app.shopfee.utils.Constant
import com.app.shopfee.utils.GlobalFunction.hideSoftKeyboard
import com.app.shopfee.utils.GlobalFunction.showToastMessage
import com.app.shopfee.utils.StringUtil.isEmpty
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class AddressActivity : BaseActivity() {
    private var listAddress: MutableList<Address>? = null
    private var addressAdapter: AddressAdapter? = null
    private var addressSelectedId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        loadDataIntent()
        initToolbar()
        initUi()
        loadListAddressFromFirebase()
    }

    private fun loadDataIntent() {
        val bundle = intent.extras ?: return
        addressSelectedId = bundle.getLong(Constant.ADDRESS_ID, 0)
    }

    private fun initToolbar() {
        val imgToolbarBack = findViewById<ImageView>(R.id.img_toolbar_back)
        val tvToolbarTitle = findViewById<TextView>(R.id.tv_toolbar_title)
        imgToolbarBack.setOnClickListener { onBackPressed() }
        tvToolbarTitle.text = getString(R.string.address_title)
    }

    private fun initUi() {
        val rcvAddress = findViewById<RecyclerView>(R.id.rcv_address)
        val linearLayoutManager = LinearLayoutManager(this)
        rcvAddress.layoutManager = linearLayoutManager
        listAddress = ArrayList()
        addressAdapter = AddressAdapter(listAddress, object : IClickAddressListener {
            override fun onClickAddressItem(address: Address) {
                handleClickAddress(address)
            }
        })
        rcvAddress.adapter = addressAdapter
        val btnAddAddress = findViewById<Button>(R.id.btn_add_address)
        btnAddAddress.setOnClickListener { onClickAddAddress() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadListAddressFromFirebase() {
        showProgressDialog(true)
        MyApplication[this].getAddressDatabaseReference()
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        showProgressDialog(false)
                        resetListAddress()
                        for (dataSnapshot in snapshot.children) {
                            val address = dataSnapshot.getValue(Address::class.java)
                            if (address != null) {
                                listAddress!!.add(0, address)
                            }
                        }
                        if (addressSelectedId > 0 && listAddress != null && listAddress!!.isNotEmpty()) {
                            for (address in listAddress!!) {
                                if (address.id == addressSelectedId) {
                                    address.isSelected = true
                                    break
                                }
                            }
                        }
                        if (addressAdapter != null) addressAdapter!!.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showProgressDialog(false)
                        showToastMessage(getString(R.string.msg_get_date_error))
                    }
                })
    }

    private fun resetListAddress() {
        if (listAddress != null) {
            listAddress!!.clear()
        } else {
            listAddress = ArrayList()
        }
    }

    private fun handleClickAddress(address: Address) {
        EventBus.getDefault().post(AddressSelectedEvent(address))
        finish()
    }

    @SuppressLint("InflateParams, MissingInflatedId")
    fun onClickAddAddress() {
        val viewDialog = layoutInflater.inflate(R.layout.layout_bottom_sheet_add_address, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(viewDialog)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        // init ui
        val edtName = viewDialog.findViewById<TextView>(R.id.edt_name)
        val edtPhone = viewDialog.findViewById<TextView>(R.id.edt_phone)
        val edtAddress = viewDialog.findViewById<TextView>(R.id.edt_address)
        val tvCancel = viewDialog.findViewById<TextView>(R.id.tv_cancel)
        val tvAdd = viewDialog.findViewById<TextView>(R.id.tv_add)

        // Set listener
        tvCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        tvAdd.setOnClickListener {
            val strName = edtName.text.toString().trim { it <= ' ' }
            val strPhone = edtPhone.text.toString().trim { it <= ' ' }
            val strAddress = edtAddress.text.toString().trim { it <= ' ' }
            if (isEmpty(strName) || isEmpty(strPhone) || isEmpty(strAddress)) {
                showToastMessage(this, getString(R.string.message_enter_infor))
            } else {
                val id = System.currentTimeMillis()
                val address = Address(id, strName, strPhone, strAddress)
                MyApplication[this].getAddressDatabaseReference()
                        ?.child(id.toString())
                        ?.setValue(address) { _, _ ->
                            showToastMessage(this,
                                    getString(R.string.msg_add_address_success))
                            hideSoftKeyboard(this)
                            bottomSheetDialog.dismiss()
                        }
            }
        }
        bottomSheetDialog.show()
    }
}