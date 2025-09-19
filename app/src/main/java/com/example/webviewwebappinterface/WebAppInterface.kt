package com.example.webviewwebappinterface

import android.webkit.JavascriptInterface

class WebAppInterface(
    private val viewModel: WebViewModel
) {
    @JavascriptInterface
    fun showToast(msg: String){
        if(msg.isNotBlank()){
            viewModel.onWebToast(msg)
        }else{
            viewModel.onError("토스트 메시지가 비어있습니다.")
        }
    }

    @JavascriptInterface
    fun showPopup(msg: String){
        if(msg.isNotBlank()){
            viewModel.onWebPopup(msg)
        }else{
            viewModel.onError("팝업 메시지가 비어있습니다.")
        }
    }
}