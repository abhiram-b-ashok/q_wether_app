package com.example.qweather.ui.side_nav_fragments.contact_us

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.qweather.data.network.NetworkResult
import com.example.qweather.databinding.FragmentContactUsBinding
import com.example.qweather.repository.ContactUsRepository
import com.example.qweather.view_models.contact_us.ContactUsViewModel
import com.example.qweather.view_models.contact_us.ContactUsViewModelFactory


class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding
    private lateinit var viewModel: ContactUsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val repository = ContactUsRepository()
            val factory = ContactUsViewModelFactory(repository)
            viewModel = ViewModelProvider(requireActivity(), factory)[ContactUsViewModel::class.java]

            binding.contactSubmitButton.setOnClickListener {
                if (validate()) {
                    val name = "User" // optional field if not in form
                    val email = binding.mailEt.text.toString()
                    val subject = binding.subjectEt.text.toString()
                    val message = binding.messageEt.text.toString()

                    viewModel.sendContactMessage(name, email, subject, message)
                }
            }

            viewModel.response.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        binding.contactSubmitButton.isEnabled = false
                        binding.contactSubmitButton.text = "Sending..."
                    }
                    is NetworkResult.Success -> {
                        Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                        binding.contactSubmitButton.text = "Submitted"
                        binding.contactSubmitButton.isEnabled = false
                        binding.contactSubmitButton.isClickable = false
                        binding.mailEt.text?.clear()
                        binding.subjectEt.text?.clear()
                        binding.messageEt.text?.clear()
                    }
                    is NetworkResult.Error -> {
                        binding.contactSubmitButton.text = "Submit"
                        binding.contactSubmitButton.isEnabled = true
                        Toast.makeText(requireContext(), result.message ?: "Something went wrong", Toast.LENGTH_LONG).show()
                    }
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

            val email = mailEt.text.toString()
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mailError.visibility = View.VISIBLE
                mailError.text = "Please enter a valid email address"
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