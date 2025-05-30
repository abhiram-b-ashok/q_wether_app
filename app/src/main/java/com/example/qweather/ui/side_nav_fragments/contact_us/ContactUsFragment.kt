package com.example.qweather.ui.side_nav_fragments.contact_us

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qweather.databinding.FragmentContactUsBinding
import androidx.core.widget.addTextChangedListener
import androidx.core.net.toUri


class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

           contactSubmitButton.setOnClickListener {
                if (validate()) {
                   contactSubmitButton.isEnabled = false
                    contactSubmitButton.text = "Submitted"
                    contactSubmitButton.isClickable = false
                    mailEt.text?.clear()
                    subjectEt.text?.clear()
                    messageEt.text?.clear()

                }
            }

            mailEt.addTextChangedListener {
               mailError.visibility = View.GONE
            }
            subjectEt.addTextChangedListener {
                subjectError.visibility = View.GONE
            }
            messageEt.addTextChangedListener {
                messageError.visibility = View.GONE
            }

            faxNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+974 44557103")
                startActivity(intent)
            }
            telephoneNumber.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+974 44557333")
                startActivity(intent)
            }
            webLink.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                    "https://www.caa.gov.qa".toUri())
                startActivity(browserIntent)
            }

            icTwitter.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                    "https://x.com/CAAQATAR".toUri())
                startActivity(browserIntent)
            }
            icFacebook.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                    "https://www.facebook.com/CAAQATAR".toUri())
                startActivity(browserIntent)
            }
            icLinkedin.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW,
                    "https://www.linkedin.com/company/qcaa".toUri())
                startActivity(browserIntent)
            }
        }
    }

    private fun validate(): Boolean{
        var isValid = true
        binding.apply {

            if (mailEt.text.isNullOrEmpty()) {
                mailError.visibility = View.VISIBLE
                isValid = false

            }
            if (subjectEt.text.isNullOrEmpty()) {
                subjectError.visibility = View.VISIBLE
                isValid = false
            }
            if (messageEt.text.isNullOrEmpty()) {
                messageError.visibility = View.VISIBLE
                isValid = false
            }
        }
        return isValid
    }

}