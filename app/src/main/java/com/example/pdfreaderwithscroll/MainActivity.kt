package com.example.pdfreaderwithscroll

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.assetButton).setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra("ViewType","assets")
            startActivity(intent)
        }

        findViewById<Button>(R.id.storageButton).setOnClickListener {
            val intent2 = Intent(Intent.ACTION_GET_CONTENT)
            intent2.type = "application/pdf"
            intent2.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent2,"Select PDF"),code)
        }

        findViewById<Button>(R.id.onlineButton).setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra("ViewType","internet")
            val text = findViewById<EditText>(R.id.linkforpdf).text
            intent.putExtra("link",text)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == code && resultCode == RESULT_OK && data != null) {
            val selectedPdf = data.data
            val intent3 = Intent(this, PdfViewerActivity::class.java)
            intent3.putExtra("ViewType","storage")
            intent3.putExtra("FileUri",selectedPdf.toString())
            startActivity(intent3)
        }
    }

    companion object {
        const val code = 100
    }
}