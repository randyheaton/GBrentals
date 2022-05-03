package com.injectorsuite.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//region SearchPageViewModel()
class SearchPageViewModel(): ViewModel() {
    var titles by mutableStateOf(listOf<CardInfo>(), neverEqualPolicy())
    var pagificationOffset by mutableStateOf(0)
    val limit=10
    var provideGuidance by mutableStateOf(false)
    var searchText by mutableStateOf("")
    var inquiredCard:CardInfo? by mutableStateOf(null)

    var contentType by mutableStateOf(ContentType.Popular)

    init{
        retreiveNewBatch()
    }

    fun retreiveNewBatch(){
        println("###retreive new batch ###")
        provideGuidance=false
        viewModelScope.launch{
            //This is too tightly coupled with QueryGrammar() and I wouldn't actually do it in production, but I wanted to try something new for a single-screen app
            val newBatch=with(QueryGrammar()){useInterface().forContentType(contentType).withParameters(pagificationOffset,limit,searchText)}
            if (newBatch.isNullOrEmpty()){
                provideGuidance=true
                return@launch
            }
            titles=titles+newBatch!!
            incrementPagification()
        }
    }

    fun incrementPagification(){
        when (contentType){
            ContentType.Popular->{pagificationOffset=pagificationOffset+limit}
            ContentType.Search->{pagificationOffset=pagificationOffset+1}
        }
    }

    fun invalidateAndChangeType(){
        titles=listOf()
        contentType=ContentType.values().first{it!=contentType}
        pagificationOffset=0
        retreiveNewBatch()
    }
}
//endregion