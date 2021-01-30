package com.example.pdfreaderwithscroll

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.LinearInterpolator
import android.widget.EditText
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import java.io.File


class PdfViewerActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        val pdfView = findViewById<PDFView>(R.id.pdfView)

        if (intent != null) {
            val type = intent.getStringExtra("ViewType")
            if (type != null || !TextUtils.isEmpty(type)) {
                when {
                    type.equals("assets") -> {
                        pdfView.fromAsset("index.pdf").load()
                    }
                    type.equals("storage") -> {
                        val pdf = Uri.parse(intent.getStringExtra("FileUri"))
                        pdfView.fromUri(pdf).load()
                    }
                    type.equals("internet") -> {
                        val bundle = intent.extras
                        val link = bundle?.get("link")
                        FileLoader.with(this)
                            .load(link.toString())
                            .fromDirectory("PDFFiles", FileLoader.DIR_INTERNAL)
                            .asFile(object : FileRequestListener<File>() {
                                override fun onLoad(
                                    request: FileLoadRequest,
                                    response: FileResponse<File>
                                ) {
                                    val loadedFile = response.body
                                    pdfView.fromFile(loadedFile)
                                        .load()
                                    // do something with the file
                                }

                                override fun onError(request: FileLoadRequest, t: Throwable) {}
                            })


                    }
                }
            }
        }

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                pdfView.setOnClickListener {
                    val int = findViewById<EditText>(R.id.songduration).text.toString().toLong() * 1500
                    val objectAnimator = ObjectAnimator.ofInt(
                        scrollView,
                        "scrollY",
                        scrollView.getChildAt(0).height - scrollView.height
                    )
                    objectAnimator.duration = int
                    objectAnimator.interpolator = LinearInterpolator()
                    objectAnimator.start()
                }
            }
        })


    }
}