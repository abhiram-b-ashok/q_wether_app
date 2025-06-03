package com.example.qweather.repository

class TideRepository {
    private fun getTides() {
        lifecycleScope.launch {
            val progressDialog= ProgressDialog()
            progressDialog.show(childFragmentManager,"Progress")
            city = Database.cityDao.findCityByIdOrNull(cityID!!.toLong())

            val response = TideAPI.getHourlyTideData()

            progressDialog.dismiss()

            when (response.exception) {
                null -> {
                    val parser = XmlPullParserFactory.newInstance().newPullParser()
                    parser.setInput(StringReader(response.rawResponse))
                    tides = TideXmlDocument.readXml(parser)
                    if (tides!=null) {
                        binding.errorMessage.visibility = View.GONE
                        tidalData = TidalViewData()
                        tidalData.status = Status.SUCCESS
                        tidalData.document = tides
                        // tidalData.area = getFirstArea(tides)
                        tidalData.area = tides?.areaTags?.firstOrNull { it.id == areaId }
                        setData()
                        binding.groupContent.visibility = View.VISIBLE

                    } else {
                        binding.groupContent.visibility = View.GONE
                        tidalData = TidalViewData()
                        tidalData.status = Status.ERROR
                        binding.errorMessage.visibility = View.VISIBLE
                    }
                }
                is IOException -> {
                    binding.errorMessage.visibility = View.VISIBLE
                    showAlertDialog(
                        title = R.string.error_title.toLocalizedString(),
                        message = R.string.err_network_description.toLocalizedString(),
                        positiveButton = R.string.ok.toLocalizedString(),
                        onPositiveButtonClicked = { findNavController().navigateUp() },
                        onDismissed = { findNavController().navigateUp() }
                    )
                    Log.d(TAG, "calculateAdditionalTidalData: ${response.exception}")
                }
                is JSONException -> {
                    binding.errorMessage.visibility = View.VISIBLE
                    showAlertDialog(
                        title = R.string.error_title.toLocalizedString(),
                        message = R.string.err_network_description.toLocalizedString(),
                        positiveButton = R.string.ok.toLocalizedString(),
                        onPositiveButtonClicked = { findNavController().navigateUp() },
                        onDismissed = { findNavController().navigateUp() }
                    )
                    Log.d(TAG, "calculateAdditionalTidalData: ${response.exception}")
                }
                else -> {
                    binding.errorMessage.visibility = View.VISIBLE
                    showAlertDialog(
                        title = R.string.error_title.toLocalizedString(),
                        message = R.string.err_network_description.toLocalizedString(),
                        positiveButton = R.string.ok.toLocalizedString(),
                        onPositiveButtonClicked = { findNavController().navigateUp() },
                        onDismissed = { findNavController().navigateUp() }
                    )
                }
            }

        }
    }
}