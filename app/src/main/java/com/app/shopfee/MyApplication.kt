package com.app.shopfee

import android.app.Application
import android.content.Context
import com.app.shopfee.prefs.DataStoreManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyApplication : Application() {

    private var mFirebaseDatabase: FirebaseDatabase? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
        DataStoreManager.init(applicationContext)
    }

    fun getVoucherDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("voucher")
    }

    fun getAddressDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("address")
    }

    fun getPaymentMethodDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("payment")
    }

    fun getCategoryDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("category")
    }

    fun getDrinkDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("drink")
    }

    fun getDrinkDetailDatabaseReference(drinkId: Int): DatabaseReference? {
        return mFirebaseDatabase?.getReference("drink/$drinkId")
    }

    fun getToppingDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("topping")
    }

    fun getFeedbackDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("/feedback")
    }

    fun getOrderDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("order")
    }

    fun getRatingDrinkDatabaseReference(drinkId: String?): DatabaseReference? {
        return mFirebaseDatabase?.getReference("/drink/$drinkId/rating")
    }

    fun getOrderDetailDatabaseReference(orderId: Long): DatabaseReference? {
        return mFirebaseDatabase?.getReference("order/$orderId")
    }

    companion object {
        private const val FIREBASE_URL = "https://coffeeshop-ea3ce-default-rtdb.firebaseio.com"
        operator fun get(context: Context?): MyApplication {
            return context!!.applicationContext as MyApplication
        }
    }
}