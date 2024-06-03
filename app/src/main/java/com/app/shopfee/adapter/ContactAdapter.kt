package com.app.shopfee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.shopfee.R
import com.app.shopfee.adapter.ContactAdapter.ContactViewHolder
import com.app.shopfee.model.Contact
import com.app.shopfee.utils.GlobalFunction

class ContactAdapter(
    private var context: Context?,
    private val listContact: List<Contact>?,
    private val iCallPhone: ICallPhone
) : RecyclerView.Adapter<ContactViewHolder>() {

    interface ICallPhone {
        fun onClickCallPhone()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = listContact!![position]
        holder.imgContact.setImageResource(contact.image)
        when (contact.id) {
            Contact.FACEBOOK -> holder.tvContact.text =
                context!!.getString(R.string.label_facebook)
            Contact.HOTLINE -> holder.tvContact.text =
                context!!.getString(R.string.label_call)
            Contact.GMAIL -> holder.tvContact.text =
                context!!.getString(R.string.label_gmail)
            Contact.SKYPE -> holder.tvContact.text =
                context!!.getString(R.string.label_skype)
            Contact.YOUTUBE -> holder.tvContact.text =
                context!!.getString(R.string.label_youtube)
            Contact.ZALO -> holder.tvContact.text =
                context!!.getString(R.string.label_zalo)
        }
        holder.layoutItem.setOnClickListener {
            when (contact.id) {
                Contact.FACEBOOK -> GlobalFunction.onClickOpenFacebook(
                    context
                )
                Contact.HOTLINE -> iCallPhone.onClickCallPhone()
                Contact.GMAIL -> GlobalFunction.onClickOpenGmail(context)
                Contact.SKYPE -> GlobalFunction.onClickOpenSkype(context)
                Contact.YOUTUBE -> GlobalFunction.onClickOpenYoutubeChannel(context)
                Contact.ZALO -> GlobalFunction.onClickOpenZalo(context)
            }
        }
    }

    override fun getItemCount(): Int {
        return listContact?.size ?: 0
    }

    fun release() {
        context = null
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutItem: LinearLayout
        val imgContact: ImageView
        val tvContact: TextView

        init {
            layoutItem = itemView.findViewById(R.id.layout_item)
            imgContact = itemView.findViewById(R.id.img_contact)
            tvContact = itemView.findViewById(R.id.tv_contact)
        }
    }
}