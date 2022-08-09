package com.jetpack_compose_trainning_04

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.jetpack_compose_trainning_04.retrofit.RetrofitInstance
import com.jetpack_compose_trainning_04.ui.theme.Jetpack_Compose_trainning_04Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
      var myList:List<ModelItem> = emptyList()
    var context:Context?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context=this

//           Thread.sleep(3000)
        Toast.makeText(this,"we will begin ",Toast.LENGTH_SHORT).show()

         val prog =ProgressBar(context)
        lifecycleScope.launch(Dispatchers.IO) {

            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e:IOException){
              Log.e("alo :","alo IOExeption")
                return@launch
            }catch (e:HttpException){
                Log.e("alo :","alo HTTPExeption")
                return@launch
            }
            //here means we got a response
            if(response.isSuccessful && response.body() !=null ){
                //here means we really got smth
                    GlobalScope.launch(Dispatchers.Main){
                        Toast.makeText(context,"we got the data ",Toast.LENGTH_SHORT).show()
                    }
                myList=response.body()!!
            }else{
                GlobalScope.launch(Dispatchers.Main){
                    Toast.makeText(context,"we got no the data ",Toast.LENGTH_SHORT).show()
                }

                Log.e("alo :","alo Response not successful")

            }
        }


        setContent {
         androidx.compose.material.Surface(
             modifier = Modifier.fillMaxSize()
         ) {
             val lst = remember {
                 mutableStateOf(myList)
            }
             LazyColumn(
                 modifier = Modifier.padding(10.dp)
             ){

                 items(lst.value){
                         ModelItem-> myItem(model = ModelItem)
                 }
             }
         }
        }
    }
}

@Composable
fun myItem(model:ModelItem) {
      val checkState = remember {
        mutableStateOf(model.completed)
    }
   Row(
       horizontalArrangement = Arrangement.SpaceBetween,
       modifier = Modifier.padding(10.dp)
           .fillMaxWidth()
           .border(2.dp, Color.Blue)
   ) {
       Text(text = model.title)
       Checkbox(checked = checkState.value,
           onCheckedChange = {
               checkState.value = it
           })
   }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Jetpack_Compose_trainning_04Theme {
     androidx.compose.material.Surface() {
     }
   myItem(model = ModelItem(true,1,"hello",9))
    }
}