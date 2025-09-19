package com.example.webviewwebappinterface

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WebViewScreen() {
    val viewModel: WebViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var webView by remember { mutableStateOf<WebView?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMesage by remember { mutableStateOf("") }

    uiState.toastMessage?.let { msg ->
        Toast.makeText(webView?.context, msg, Toast.LENGTH_SHORT).show()
        viewModel.clearToast()
    }

    uiState.popupMessage?.let { msg ->
        dialogMesage = msg
        showDialog = true
        viewModel.clearPopup()
    }

    uiState.errorMessage?.let { msg ->
        Toast.makeText(webView?.context, msg, Toast.LENGTH_SHORT).show()
        viewModel.clearError()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("확인")
                }
            },
            title = { Text("웹 요청 팝업") },
            text = { Text(dialogMesage) }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    //  웹뷰안에서 자바스크립트 코드를 호출하기 위해 사용
                    settings.javaScriptEnabled = true
                    //  alert() 띄우기위해 사용
                    webChromeClient = object : WebChromeClient(){}
                    addJavascriptInterface(WebAppInterface(viewModel), "AndroidApp")
                    loadUrl("file:///android_asset/sample.html")
                }
            },
            update = { webView = it }
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "앱 -> 웹 요청",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Row(Modifier.fillMaxWidth()) {
            Button(onClick = {
                webView?.evaluateJavascript(
                    "showAlertFromApp('앱에서 요청한 알림')", null
                )
            }) { Text("웹 Alert") }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                webView?.evaluateJavascript(
                    "insertTextFromApp('앱에서 보낸 메시지')", null
                )
            }) { Text("웹 텍스트 추가") }
        }
    }
}