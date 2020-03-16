package de.adorsys.mobile.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.adorsys.mobile.R
import de.adorsys.mobile.data.Person
import de.adorsys.mobile.extions.getButtonVisibilit

class ListAdapterBusinessCard(
    private val onEmailButtonAction: (Person) -> Unit,
    private val onCallButtonAction: (Person) -> Unit,
    private val onHomepageButtonAction: (Person) -> Unit,
    private val onEditButtonAction: (Person) -> Unit,
    private val onDeleteAction: (Person) -> Unit,
    private val onBarcodeAction: (Person) -> Unit
) :
    ListAdapter<Person, ListAdapterBusinessCard.ViewHolder>(
        PersonDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_business_card, parent, false)
        return ViewHolder(
            view,
            onEmailButtonAction,
            onCallButtonAction,
            onHomepageButtonAction,
            onEditButtonAction,
            onDeleteAction,
            onBarcodeAction
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = getItem(position)
        holder.bind(person)
    }

    class ViewHolder(
        itemView: View,
        private val onEmailButtonAction: (Person) -> Unit,
        private val onCallButtonAction: (Person) -> Unit,
        private val onHomepageButtonAction: (Person) -> Unit,
        private val onEditButtonAction: (Person) -> Unit,
        private val onDeleteAction: (Person) -> Unit,
        private val onBarcodeAction: (Person) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView = itemView.findViewById<ImageView>(R.id.photo_image_view)
        private val nameTextView = itemView.findViewById<TextView>(R.id.name_text_view)
        private val adressTextView = itemView.findViewById<TextView>(R.id.adress_text_view)
        private val companyTextView = itemView.findViewById<TextView>(R.id.company_text_view)
        private val emailButton = itemView.findViewById<TextView>(R.id.email_button)
        private val callButton = itemView.findViewById<TextView>(R.id.call_button)
        private val homepageButton = itemView.findViewById<TextView>(R.id.website_button)
        private val editButton = itemView.findViewById<ImageButton>(R.id.edit_button)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)
        private val barCodeButton = itemView.findViewById<ImageButton>(R.id.bar_code_button)


        fun bind(person: Person) {
            nameTextView.text =
                person.firstName + ' ' + person.secondName
            adressTextView.text = person.adress
            adressTextView.text = person.adress
            companyTextView.text = person.company
            emailButton.text = person.email
            callButton.text = person.phone
            homepageButton.text = person.url

            if (!person.imageUrl.isNullOrEmpty()) {
                photoImageView.setImageBitmap(BitmapFactory.decodeFile(person.imageUrl))
            } else {
                val drawable =
                    ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_contact_default_image, null)
                photoImageView.setImageDrawable(drawable)
            }

            callButton.visibility = person.phone.getButtonVisibilit()
            homepageButton.visibility = person.url.getButtonVisibilit()
            emailButton.visibility = person.email.getButtonVisibilit()

            homepageButton.setOnClickListener { onHomepageButtonAction.invoke(person) }
            emailButton.setOnClickListener { onEmailButtonAction.invoke(person) }
            callButton.setOnClickListener { onCallButtonAction.invoke(person) }
            editButton.setOnClickListener { onEditButtonAction.invoke(person) }
            deleteButton.setOnClickListener { onDeleteAction.invoke(person) }
            barCodeButton.setOnClickListener { onBarcodeAction.invoke(person) }
        }
    }

}

class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean = oldItem == newItem
}