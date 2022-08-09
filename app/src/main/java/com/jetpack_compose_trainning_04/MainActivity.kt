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
import androidx.compose.runtime.currentCompositionLocalContext
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
import kotlinx.coroutines.*
import okhttp3.internal.wait
import retrofit2.HttpException
import java.io.IOException
var i :Int=0
var context:Context?=null
class MainActivity : ComponentActivity() {
      var myList:List<ModelItem> = emptyList()

    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context=this
        i=0

//           Thread.sleep(3000)
        Toast.makeText(this,"we will begin ",Toast.LENGTH_SHORT).show()



        val job = lifecycleScope.launch(Dispatchers.IO) {

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
              //  Toast.makeText(context,"we got the data ",Toast.LENGTH_SHORT).show()

                delay(1000)
                myList=response.body()!!
                delay(1000)
                //Toast.makeText(context,"we set you the data ",Toast.LENGTH_SHORT).show()



            }else{
                GlobalScope.launch(Dispatchers.Main){
                    Toast.makeText(context,"we got no the data ",Toast.LENGTH_SHORT).show()
                }

                Log.e("alo :","alo Response not successful")

            }
        }



       runBlocking {
           job.join()
            delay(1000)
           Toast.makeText(context,"we will set the content",Toast.LENGTH_SHORT).show()
           setContent {
               ShowMyColumn(myList = myList)

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
       modifier = Modifier
           .padding(10.dp)
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
@Composable
fun ShowMyColumn(myList:List<ModelItem>){

//        val lst = remember {
//            mutableStateOf(myList)
//        }
        Toast.makeText(context,"content set successfully",Toast.LENGTH_SHORT).show()

        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ){

            items(myList){
                    ModelItem-> myItem(model = ModelItem)
            }
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