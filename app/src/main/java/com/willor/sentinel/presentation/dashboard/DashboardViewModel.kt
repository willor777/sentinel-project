package com.willor.sentinel.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.utils.printToDEBUG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val repo: IRepo
): ViewModel(){


    fun testAdvancedChart(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getETFQuote("SPY").collect{
                when(it){
                    is Resource.Loading ->{
                        printToDEBUG("Collected Loading from getAdvChart")
                    }
                    is Resource.Success -> {
                        printToDEBUG("Success collecting getAdvChart")
                    }
                    else ->{
                        printToDEBUG("")
                    }
                }
            }
        }
    }

}