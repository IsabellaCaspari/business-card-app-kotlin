package de.adorsys.mobile.extions

import android.view.View
import com.google.firebase.firestore.QuerySnapshot
import de.adorsys.mobile.data.Person
import de.adorsys.mobile.storage.FirebaseProvider

fun String?.getButtonVisibilit() = if (this.isNullOrEmpty()) {
    View.GONE
} else {
    View.VISIBLE
}

fun Person.toHashMap() = hashMapOf(
    FirebaseProvider.ID_KEY to this.id,
    FirebaseProvider.ADDRESS_KEY to this.adress,
    FirebaseProvider.COMPANY_KEY to this.company,
    FirebaseProvider.EMAIL_KEY to this.email,
    FirebaseProvider.PHONE_KEY to this.phone,
    FirebaseProvider.SECOND_KEY to this.secondName,
    FirebaseProvider.FIRST_KEY to this.firstName,
    FirebaseProvider.URL_KEY to this.url,
    FirebaseProvider.IMAGE_URL_KEY to this.imageUrl
)

fun QuerySnapshot.mapQueryToPersonList() =
    this.map { document ->
        Person(
            document.data[FirebaseProvider.ID_KEY].toString(),
            document.data[FirebaseProvider.FIRST_KEY].toString(),
            document.data[FirebaseProvider.SECOND_KEY].toString(),
            document.data[FirebaseProvider.PHONE_KEY].toString(),
            document.data[FirebaseProvider.ADDRESS_KEY].toString(),
            document.data[FirebaseProvider.COMPANY_KEY].toString(),
            document.data[FirebaseProvider.EMAIL_KEY].toString(),
            document.data[FirebaseProvider.URL_KEY].toString(),
            document.data[FirebaseProvider.IMAGE_URL_KEY].toString()
        )
    }.toList()

