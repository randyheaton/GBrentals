package com.injectorsuite.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch





/*
A lot of these components like the search box and the bottom modal could be broken into composables. The upshot would be that they would have more descriptive names and could be maintained
independently for small-scale modifications. The downside is I would have to pass state to each one either explicitly or by giving them an
instance of the ViewModel, and that would slow down iteration speed.

I'm not opinionated and would defer to the house guidelines. But for something that hasn't received feedback, I would want to keep state exposed as possible for on-the-fly
changes.

 */


//region SearchPage()
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchPage() {
    val searchPageViewModel = viewModel<SearchPageViewModel>()
    val keyboardController = LocalSoftwareKeyboardController.current
    var bottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope= rememberCoroutineScope()
    Column(Modifier.padding(horizontal = 10.dp)) {
        OutlinedTextField(
            value = searchPageViewModel.searchText,
            onValueChange = {
                searchPageViewModel.searchText = it
                if (it=="" && searchPageViewModel.contentType==ContentType.Search){
                    searchPageViewModel.invalidateAndChangeType()
                }
            },
            label = { Text("Search a title") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, "") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                if (searchPageViewModel.searchText==""){
                    return@KeyboardActions
                }
                searchPageViewModel.invalidateAndChangeType()
            })
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center){
            Text(searchPageViewModel.contentType.name)
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            itemsIndexed(searchPageViewModel.titles) {index,cardInfo  ->
                Column(Modifier.clickable(onClick={
                    searchPageViewModel.inquiredCard=cardInfo
                    coroutineScope.launch{
                        bottomState.animateTo(ModalBottomSheetValue.Expanded)
                    }
                })) {
                    GameCard(cardInfo, index)
                }
            }

        }
        if (searchPageViewModel.provideGuidance){
            Text("That's all we could find.")
        }
    }

    ModalBottomSheetLayout(sheetState=bottomState,sheetShape= RoundedCornerShape(topStart=16.dp,topEnd=16.dp),sheetContent={
        var showGuidance by remember{ mutableStateOf(false) }
        Column(Modifier.fillMaxHeight(.8f).padding(10.dp)){
            Text(searchPageViewModel.inquiredCard?.name?:"")
            Spacer(Modifier.height(20.dp))
            Box(Modifier.requiredSize(300.dp)){
                Row{
                    Spacer(Modifier.weight(1f))
                    GlideImage(searchPageViewModel.inquiredCard?.image?.screen_large_url,contentScale= ContentScale.Fit)
                    Spacer(Modifier.weight(1f))
                }


            }
            Spacer(Modifier.weight(1f))
            Text((searchPageViewModel.inquiredCard?.number_of_user_reviews?:0).toString()+" people have reviewed this game. Make that number higher by renting it and sharing your thoughts.")
            Spacer(Modifier.weight(1f))
            Row{
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment= Alignment.End) {
                    Button(onClick = {showGuidance=true},enabled=!showGuidance) {
                        Text("Try it")
                    }
                    if (showGuidance){
                        Text("Checkout Successful")
                    }
                }
            }


        }
    }){}
}
//endregion

//region GameCard(cardInfo:CardInfo,index:Int)
@Composable
fun GameCard(cardInfo:CardInfo,index:Int){
    val searchPageViewModel= viewModel<SearchPageViewModel>()
    LaunchedEffect(0){
        if (index==searchPageViewModel.titles.lastIndex){
            searchPageViewModel.retreiveNewBatch()
        }
    }

    Column{
        Divider()
        Text(cardInfo.name)
        Spacer(Modifier.weight(1f))
        Row{
            Spacer(Modifier.weight(1f))
            Box(Modifier
                .requiredSize(200.dp)
                .weight(2f)) {
                GlideImage(cardInfo.image.screen_large_url, contentScale = ContentScale.Fit)
            }
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.weight(1f))
        Row{
            Spacer(Modifier.weight(1f))
            Text("Reviews: "+cardInfo.number_of_user_reviews)
        }
        Divider()
    }
}
//endregion