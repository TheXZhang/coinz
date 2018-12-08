package com.example.s1604556.coinz.downloader

interface DownloadCompleteListener {
    fun downloadComplete(result: String)
}

object DownloadCompleteRunner : DownloadCompleteListener {
    var result : String = ""
    override fun downloadComplete(result:String){
        DownloadCompleteRunner.result = result
    }
}