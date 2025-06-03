package com.example.qweather.ui.dashboard.inner_fragments.tides

import org.xmlpull.v1.XmlPullParser
import java.util.*

class TidalValueTag {
    companion object {
        fun readXml(parser: XmlPullParser): TidalValueTag {
            val tag = TidalValueTag()
            tag.dateStr = parser.getAttributeValue(null, "Date")
            loop@ while (true) {
                when (parser.next()) {
                    XmlPullParser.TEXT -> {
                        tag.valueStr = parser.text
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "value") {
                            break@loop
                        }
                    }
                }
            }
            return tag
        }
    }

    var dateStr: String? = null
    var valueStr: String? = null

    val date: Long
        get() {
            val dateStrVal = dateStr!!.toInt()
            return (dateStrVal / 100.0).toLong()
        }

    val hour: Int
        get() = (dateStr!!.toInt() - (date * 100)).toInt()

    val value: Double
        get() = valueStr!!.toDouble()
}

class TidalAreaTag {
    companion object {
        fun readXml(parser: XmlPullParser): TidalAreaTag {
            val tag = TidalAreaTag()
            tag.id = parser.getAttributeValue(null, "id")
            tag.name = parser.getAttributeValue(null, "name")
            tag.nameAr = parser.getAttributeValue(null, "name-ar")
            tag.latitude = parser.getAttributeValue(null, "latitude")
            tag.longitude = parser.getAttributeValue(null, "longitude")
            val values = arrayListOf<TidalValueTag>()
            loop@ while (true) {
                when (parser.next()) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "value") {
                            values.add(TidalValueTag.readXml(parser))
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "area")
                            break@loop
                    }
                }
            }
            tag.valueTags = values
            return tag
        }
    }

    var id: String? = null
    var name: String? = null
    var nameAr: String? = null
    var latitude: String? = null
    var longitude: String? = null

    var valueTags: List<TidalValueTag>? = null
}

class TideXmlDocument {
    companion object {
        fun readXml(parser: XmlPullParser): TideXmlDocument {
            val document = TideXmlDocument()
            val areaTags = arrayListOf<TidalAreaTag>()
            loop@ while (true) {
                when (parser.next()) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "data" -> {
                                document.source = parser.getAttributeValue(null, "source")
                                document.productionCenter =
                                    parser.getAttributeValue(null, "productioncenter")
                            }
                            "area" -> {
                                areaTags.add(TidalAreaTag.readXml(parser))
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "data") {
                            break@loop
                        }
                    }
                }
            }
            document.areaTags = areaTags
            return document
        }
    }

    var source: String? = null
    var productionCenter: String? = null

    var areaTags: List<TidalAreaTag>? = null
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

class TidalViewData {
    var status: Status? = null
    var document: TideXmlDocument? = null
    var dates: List<Long>? = null
    var area: TidalAreaTag? = null
    var date: Long? = null
    var values: List<TidalValueTag>? = null

    var currentHeightMeters: Double? = null
    var lastUpdatedTime: Date? = null

    var maxTideHeightMeters: Double? = null
    var maxTideHeightHour: Int? = null
    var minTideHeightMeters: Double? = null
    var minTideHeightHour: Int? = null
}

data class TidalArea (
    var id: Int? = null,
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name_ar: String? = null
)