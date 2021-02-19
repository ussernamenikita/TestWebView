package gmk57.testwebview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    val rationale: TextView by lazy {
        findViewById<TextView>(R.id.shouldShowRationale)
    }

    val requestPermission: AppCompatActivity.(String) -> Unit = {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            this.requestPermissions(arrayOf(it), 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.storage).setOnClickListener {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        findViewById<Button>(R.id.location).setOnClickListener {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        findViewById<Button>(R.id.photo).setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoURI: Uri = getPhotoUri()
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            try {
                startActivityForResult(takePictureIntent, 0, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPhotoUri(): Uri {
        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "test.jpg")
        )
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        rationale.text =
            "Should show rationale: ${this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)}"
    }
}
