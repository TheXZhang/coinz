package com.example.s1604556.coinz

interface DownloadCompleteListener {
    fun downloadComplete(result: String)
}

object DownloadCompleteRunner : DownloadCompleteListener{
    var result : String = ""
    override fun downloadComplete(result:String){
        this.result = result
    }
}