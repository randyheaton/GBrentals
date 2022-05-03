package com.injectorsuite.myapplication

import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.injectorsuite.myapplication.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.intellij.lang.annotations.JdkConstants
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import javax.net.ssl.SSLHandshakeException

/*
                        ┌────────────┐
                        │            │
                        │   Views    │
                        │            │
                        └┬─────────▲─┘
               notify    │         │ update state
                events   │         │   for
                 to      │         │
                       ┌─▼─────────┴───┐◄──────────────┐
                       │               │               │
                       │  ViewModel    ├────────┐      │
                       │               │        │      │
                       └───────────────┘        │      │ render JSON
                                       forward  │      │    for
                                       query    │      │
                                       through  │      │
                                                │      │
┌─────────────────────────────┐           ┌─────▼──────┴─────────┐
│                             │  provide  │   NetworkInterface   │
│                             │   key to  │   (data classes,     │
│  KeyProviderSingleInstance  ├───────────┼─►  ApiInterface,     │
│                             │           │    QueryGrammar)     │
│                             │           │                      │
│                             │           │                      │
└────┬─────────────────────▲──┘           └────┬─────────▲───────┘
     │                     │                   │         │
     │request key          │          request  │         │ provide JSON
     │   from              │           JSON    │         │    to
   ┌─▼──────────┐          │           from   ┌▼─────────┴───┐
   │            │          │                  │              │
   │            │          │                  │              │
   │  Resources ├──────────┘                  │   Server     │
   │            │   provide key               │              │
   │            │      to                     │              │
   └────────────┘                             └──────────────┘
 */


class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SearchPage()
                }
            }
        }
    }
}












