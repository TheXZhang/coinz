package com.example.s1604556.coinz.downloader

//taken from lecture, modification is made to var result, from it can be null to it being empty string ""
interface DownloadCompleteListener {
    fun downloadComplete(result: String)
}

object DownloadCompleteRunner : DownloadCompleteListener {
    var result : String = ""
    override fun downloadComplete(result:String){
        DownloadCompleteRunner.result = result
    }
}