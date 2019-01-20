package me.toptas.rssconverter

import java.io.Serializable

data class RssEnclosure (var type: String? = null,
            var link: String? = null,
            var length: String? = null) : Serializable