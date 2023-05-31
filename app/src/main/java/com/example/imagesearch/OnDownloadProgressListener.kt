package com.example.imagesearch

interface OnDownloadProgressListener {
    fun percent(percent: Int)
    fun downloadStart()
    fun downloadedSuccess()
    fun downloadFail(error: String?)
    fun downloadCancel()
}