package de.adorsys.mobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import de.adorsys.mobile.data.Person
import de.adorsys.mobile.data.PersonRepository
import java.util.*


class EditPersonActivity : AppCompatActivity() {

    private lateinit var firstnameEditText: TextInputEditText
    private lateinit var secondnameEditText: TextInputEditText
    private lateinit var companyEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var urlEditText: TextInputEditText
    private lateinit var photoEditText: TextInputEditText
    private lateinit var saveButton: Button

    private var executeMode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        firstnameEditText = findViewById(R.id.firstname_text_input)
        secondnameEditText = findViewById(R.id.secondname_text_input)
        companyEditText = findViewById(R.id.company_text_input)
        emailEditText = findViewById(R.id.email_text_input)
        addressEditText = findViewById(R.id.address_text_input)
        phoneEditText = findViewById(R.id.phone_text_input)
        urlEditText = findViewById(R.id.url_text_input)
        photoEditText = findViewById(R.id.photo_text_input)
        saveButton = findViewById(R.id.saveButton)

        val person = intent.getParcelableExtra<Person>(EXTRA_PERSON)
        executeMode = if (person != null) {
            setPersonData(person)
            setTitle(R.string.edit_activity_title)
            EXECUTE_MODE_UPDATE
        } else {
            setTitle(R.string.new_person_activity_title)
            EXECUTE_MODE_WRITE
        }

        saveButton.setOnClickListener {
            when (executeMode) {
                EXECUTE_MODE_UPDATE -> {
                    PersonRepository.updatePerson(getPersonData(person))
                }
                EXECUTE_MODE_WRITE -> {
                    PersonRepository.writePerson(getPersonData(person))
                }
            }
            finish()
        }

        photoEditText.setOnClickListener {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                ).setType("image/jpg")
                    .setType("image/jpeg").setType("image/png"), SELECT_IMAGE
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = data?.data ?: return
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0]) ?: 0
            val picturePath: String = cursor?.getString(columnIndex) ?: ""
            cursor?.close()

            val imageView: ImageView = findViewById(R.id.image_view)
            imageView.visibility = View.VISIBLE
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            photoEditText.setText(picturePath)
        }

    }

    private fun getPersonData(person: Person?) = Person(
        person?.id ?: UUID.randomUUID().toString(),
        if (firstnameEditText.text.toString().isNotEmpty()) firstnameEditText.text.toString() else person?.firstName
            ?: "",
        if (secondnameEditText.text.toString().isNotEmpty()) secondnameEditText.text.toString() else person?.secondName
            ?: "",
        if (phoneEditText.text.toString().isNotEmpty()) phoneEditText.text.toString() else person?.phone
            ?: "",
        if (addressEditText.text.toString().isNotEmpty()) addressEditText.text.toString() else person?.adress
            ?: "",
        if (companyEditText.text.toString().isNotEmpty()) companyEditText.text.toString() else person?.company
            ?: "",
        if (emailEditText.text.toString().isNotEmpty()) emailEditText.text.toString() else person?.email
            ?: "",
        if (urlEditText.text.toString().isNotEmpty()) urlEditText.text.toString() else person?.url
            ?: "",
        if (photoEditText.text.toString().isNotEmpty()) photoEditText.text.toString() else person?.imageUrl
            ?: ""
    )

    private fun setPersonData(person: Person) {
        firstnameEditText.setText(person.firstName)
        secondnameEditText.setText(person.secondName)
        companyEditText.setText(person.company)
        emailEditText.setText(person.email)
        addressEditText.setText(person.adress)
        phoneEditText.setText(person.phone)
        urlEditText.setText(person.url)
        photoEditText.setText(person.imageUrl)
    }

    companion object {
        private const val EXECUTE_MODE_WRITE = "write"
        private const val EXECUTE_MODE_UPDATE = "update"
        private const val EXTRA_PERSON = "extra-person"
        private const val SELECT_IMAGE = 8999
        fun createIntent(context: Context, person: Person?) =
            Intent(context, EditPersonActivity::class.java).putExtra(EXTRA_PERSON, person)
    }
}
