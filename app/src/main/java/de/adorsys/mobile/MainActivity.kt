package de.adorsys.mobile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import de.adorsys.mobile.adapter.ListAdapterBusinessCard
import de.adorsys.mobile.barcode.BarcodeGenerator
import de.adorsys.mobile.data.Person
import de.adorsys.mobile.extions.mapQueryToPersonList
import de.adorsys.mobile.mecard.MecardGenerator
import de.adorsys.mobile.storage.FirebaseProvider
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.app_name)

        recyclerView = findViewById(R.id.business_card_recycler_view)

        recyclerView.adapter = ListAdapterBusinessCard(
            { openActionSendToIntent(it) },
            { openActionDialIntent(it) },
            { openActionViewIntent(it) },
            { openEditPersonActivity(it) },
            { deletePerson(it) },
            { showBarCode(it) })

        recyclerView.layoutManager = LinearLayoutManager(this)
        getData()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.new_action) {
            openEditPersonActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showBarCode(person: Person) {
        val content = MecardGenerator.mapPersonToMeCard(person)
        val bitmap = BarcodeGenerator.generateBarcode(content, BarcodeFormat.QR_CODE, 400, 400)

        val subView = layoutInflater.inflate(R.layout.view_barcode, null)
        val imageView = subView.findViewById<ImageView>(R.id.barcode_image_view)
        imageView.setImageBitmap(bitmap)

        val builder = buildAlertDialog(bitmap)
        builder.setView(subView)
        builder.show()
    }

    private fun buildAlertDialog(bitmap: Bitmap? = null): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.alert_dialog_bar_code_title))
        builder.setMessage(getString(R.string.alert_dialog_bar_code_message))

        builder.setPositiveButton(getString(R.string.alert_dialog_bar_code_button_text)) { _, _ ->
            bitmap?.let {
                openActionSendIntent(it)
            }

            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }
        builder.setCancelable(true)
        return builder
    }

    private fun getData() {
        FirebaseProvider.readPersonList(
            {
                (recyclerView.adapter as? ListAdapterBusinessCard)?.submitList(
                    it.mapQueryToPersonList()
                )
                recyclerView.adapter?.notifyDataSetChanged()
            },
            { Log.e(FirebaseProvider.TAG, "sth went wrong", it) })
    }

    private fun openEditPersonActivity(person: Person? = null) {
        startActivity(EditPersonActivity.createIntent(this, person))
    }

    private fun openActionDialIntent(person: Person) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + person.phone)
        startActivity(intent)
    }

    private fun openActionViewIntent(person: Person) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(person.url)
        startActivity(intent)
    }

    private fun openActionSendToIntent(person: Person) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + person?.email))
        startActivity(intent)
    }

    private fun openActionSendIntent(bitmap: Bitmap) {
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")
        intent.putExtra(
            Intent.EXTRA_STREAM,
            getImageUri(this, bitmap)
        )

        startActivity(Intent.createChooser(intent, ""))
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "",
            null
        )
        return Uri.parse(path)
    }

    private fun deletePerson(person: Person) {
        FirebaseProvider.deletePerson({
            val toast =
                Toast.makeText(this, "Successful delete Person", Toast.LENGTH_LONG)
            toast.show()
            getData()

        }, {
            Log.e(FirebaseProvider.TAG, "Sth went wrong while deleting", it)
            getData()
        }, person)
    }


}
