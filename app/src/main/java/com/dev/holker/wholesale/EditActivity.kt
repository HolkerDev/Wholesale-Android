package com.dev.holker.wholesale

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dev.holker.wholesale.model.InfoDialog
import com.dev.holker.wholesale.model.LocationDialog
import com.dev.holker.wholesale.presenters.SignUpDescriptionPresenter
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.ByteArrayOutputStream

class EditActivity : AppCompatActivity(), LocationDialog.NoticeDialogListener {
    override fun apply(country: String?, city: String?, street: String?) {
        val obj = ParseObject("Location")

        //Put street to this table
        if (street != null) {
            obj.put("street", street)
        }

        //Query for finding a city
        val queryCity = ParseQuery<ParseObject>("City")
        queryCity.whereEqualTo("objectId", SignUpDescriptionPresenter.getCityId(city))
        val objCity = queryCity.first

        //Then put it in Location table like a pointer
        obj.put("city", objCity)

        //Query for finding an country.
        val queryCountry = ParseQuery<ParseObject>("Country")
        queryCountry.whereEqualTo("objectId", SignUpDescriptionPresenter.getCountryId(country))
        val objCountry = queryCountry.first
        //Then put it in Location table like a pointer
        obj.put("country", objCountry)

        obj.saveInBackground {
            val locationObj = obj
            //put location to user
            val userCurrent = ParseUser.getCurrentUser()
            userCurrent.put("location", locationObj)
            userCurrent.saveInBackground {

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        edit_info.setOnClickListener {
            val dialog = InfoDialog()
            val fm = supportFragmentManager
            dialog.show(fm, "Information")
        }

        edit_background.setOnClickListener {

        }

        edit_photo.setOnClickListener {
            getPhoto()
        }

        edit_interests.setOnClickListener {
            val dialog: AlertDialog
            val interests = ArrayList<String>()
            val checkedItems = booleanArrayOf(false, false, false, false, false, false)
            val items = arrayOf(
                "Dining Sets", "Outdoor Lounge Chairs", "Banquet Chairs", "Tables",
                "High chairs", "Bar Stools"
            )

            val builder = AlertDialog.Builder(this@EditActivity)
            builder.setTitle("Select interests")
            builder.setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
                // Get the clicked item
                val choice = items[which]
                if (checkedItems[which] == true) {
                    interests.add(choice)
                } else {
                    interests.remove(items[which])
                }
                // Display the clicked item text
            }

            builder.setPositiveButton("OK") { _, _ ->
                val queryInter = ParseQuery<ParseObject>("Preference")
                queryInter.whereEqualTo("user", ParseUser.getCurrentUser())
                queryInter.findInBackground { objects, e ->
                    if (e != null) {
                        Log.i("Edit", e.message)
                    } else {
                        if (objects.size > 0) {
                            for (obj in objects) {
                                obj.deleteInBackground()
                            }
                        }
                    }

                    for (string in interests) {
                        val objPref = ParseObject("Preference")
                        objPref.put("user", ParseUser.getCurrentUser())

                        val queryType = ParseQuery<ParseObject>("ProductType")
                        queryType.whereEqualTo("name", string)
                        val type = queryType.first
                        //put this object to preference
                        objPref.put("productType", type)
                        //save preference
                        objPref.saveInBackground()
                    }
                }
            }
            dialog = builder.create()
            dialog.show()
        }

        edit_go_back.setOnClickListener {
            finish()
        }

        edit_location.setOnClickListener {
            getLocation()
        }
    }

    fun getLocation() {
        val dialog = LocationDialog()
        val fm = supportFragmentManager
        dialog.show(fm, "location")
    }

    //get selected photo if its was selected
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val image = data.data
            val currentUser = ParseUser.getCurrentUser()
            val avatar = MediaStore.Images.Media.getBitmap(contentResolver, image)
            val stream = ByteArrayOutputStream()
            avatar.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bytes = stream.toByteArray()
            val file = ParseFile("avatar.png", bytes)
            currentUser.put("avatar", file)
            currentUser.saveInBackground {
            }
        }
    }

    private fun getPhoto() {
        val photoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoIntent, 1);
    }
}
