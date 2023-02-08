package recio.aitor.heroesapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import recio.aitor.heroesapp.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var heroImage : ImageView
    private var heroBitmap : Bitmap? = null
    private var picturePath = ""


    //This method takes a preview of the image, replacing with one to save image on storage
    /*private val getContent = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        bitmap ->
        heroBitmap = bitmap
        heroImage.setImageBitmap(heroBitmap!!)
    }*/

    private val getContent = registerForActivityResult(ActivityResultContracts.TakePicture()){
        succes ->
        if(succes && picturePath.isNotEmpty()){
            heroBitmap = BitmapFactory.decodeFile(picturePath)
            heroImage.setImageBitmap(heroBitmap!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val heroName = binding.heroNameEdit.text.toString()
            val alterEgoName = binding.alterEgoEdit.text.toString()
            val bio = binding.bioEditText.text.toString()
            val power = binding.ratingBar.rating
            val hero = Superhero(heroName, alterEgoName, bio, power)
            openDetailsActivity(hero)
        }

        heroImage = binding.heroImage;

        heroImage.setOnClickListener{
            openCamera()
        }
    }

    private fun openCamera() {

        //val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //startActivityForResult(cameraIntent, 1000) Deprecated
        val file = createImageFile()

        val uri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            FileProvider.getUriForFile(this,"$packageName.provider",file)
        }else{
            Uri.fromFile(file)
        }
        getContent.launch(uri)
    }

    private fun createImageFile(): File {

        val fileName = "superhero_image"
        val fileDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(fileName,"jpg",fileDirectory)
        picturePath = file.absolutePath
        return file
    }

    private fun openDetailsActivity(superhero: Superhero ) {
        val intent = Intent(this, DetailActivity::class.java )
        intent.putExtra(DetailActivity.SUPERHERO_KEY, superhero)
        intent.putExtra(DetailActivity.BITMAP_KEY, picturePath)
        startActivity(intent)
    }

    /* Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && resultCode == 1000){
            val extras = data?.extras
            heroBitmap = extras?.get("data") as Bitmap
            heroImage.setImageBitmap(heroBitmap!!)
        }
    }*/
}