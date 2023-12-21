package com.hyperether.pointsnaps.ui.base

import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hyperether.pointsnaps.R


open class BaseFragment : Fragment() {

    fun createToast(
        message: String,
        time: Int = Toast.LENGTH_LONG,
        gravity: Int = Gravity.TOP,
        xOff: Int = 0,
        yOff: Int = 20
    ) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast, null)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = message
        with(Toast(requireContext().applicationContext)) {
            setGravity(gravity, xOff, yOff)
            duration = time
            view = layout
            show()
        }
    }
}
