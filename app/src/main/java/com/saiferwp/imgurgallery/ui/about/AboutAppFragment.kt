package com.saiferwp.imgurgallery.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.saiferwp.imgurgallery.BuildConfig
import com.saiferwp.imgurgallery.R
import java.text.SimpleDateFormat
import java.util.*


class AboutAppFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TranslucentDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dilalog_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonDone = view.findViewById<Button>(R.id.close)
        buttonDone.setOnClickListener {
            dismissAllowingStateLoss()
        }

        val version = view.findViewById<TextView>(R.id.app_version)
        version.text =
            getString(R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        val buildTime = view.findViewById<TextView>(R.id.build_time)
        val buildDate = Date(BuildConfig.TIMESTAMP)
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH:mm", Locale.getDefault())
        buildTime.text = getString(R.string.about_build_time, formatter.format(buildDate))

        val author = view.findViewById<TextView>(R.id.author)
        author.setText(R.string.about_author)
    }
}