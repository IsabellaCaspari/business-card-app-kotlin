
package de.adorsys.mobile.storage

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import de.adorsys.mobile.data.Person
import de.adorsys.mobile.extions.toHashMap


object FirebaseProvider {
    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION_PATH = "persons"
    const val TAG = "TAG_PERSON"
    const val ID_KEY = "id"
    const val ADDRESS_KEY = "adress"
    const val COMPANY_KEY = "company"
    const val EMAIL_KEY = "email"
    const val PHONE_KEY = "phone"
    const val SECOND_KEY = "secondName"
    const val FIRST_KEY = "firstName"
    const val URL_KEY = "url"
    const val IMAGE_URL_KEY = "imageUrl"

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings
    }

    fun writePerson(
        person: Person
    ) {
        // Add a new document with a generated ID
        db.collection(COLLECTION_PATH)
            .add(person.toHashMap())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun updatePerson(
        person: Person
    ) {
        db.collection(COLLECTION_PATH).whereEqualTo(ID_KEY, person.id).get()
            .addOnSuccessListener { querySnapshot ->
                val documentId = querySnapshot.documents.first().id
                db.collection(COLLECTION_PATH).document(documentId)
                    .update(person.toHashMap() as Map<String, String>)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            }

    }

    fun readPersonList(
        actionSuccess: (result: QuerySnapshot) -> Unit,
        actionError: (exception: Exception) -> Unit
    ) {
        db.collection(COLLECTION_PATH)
            .get()
            .addOnSuccessListener {
                actionSuccess(it)
            }
            .addOnFailureListener {
                actionError(it)
            }
    }

    fun deletePerson(
        actionSuccess: () -> Unit,
        actionError: (exception: Exception) -> Unit, person: Person
    ) {
        db.collection(COLLECTION_PATH).whereEqualTo(ID_KEY, person.id).get()
            .addOnSuccessListener { querySnapshot ->

                val documentId = querySnapshot.documents.first().id
                db.collection(COLLECTION_PATH)
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        actionSuccess()
                    }
                    .addOnFailureListener {
                        actionError(it)
                    }
            }
    }
}