package com.example.qweather.data.models.contact_us

data class ContactUsModel(
    val mail: String,
    val subject: String,
    val message: String
)

data class ContactUsResponse(
    val response: ContactUsResponseData
)

data class ContactUsResponseData(
    val status: Boolean,
    val result: ContactUsModel
)

