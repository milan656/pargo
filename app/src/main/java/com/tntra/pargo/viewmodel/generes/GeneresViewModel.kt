package com.tntra.pargo.viewmodel.generes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tntra.pargo.model.generes.GeneresListModel
import com.tntra.pargo.repository.GeneresRepository

class GeneresViewModel : ViewModel() {

    private var generesRepository: GeneresRepository? = null
    var generesListModel: MutableLiveData<GeneresListModel>? = null

    fun callApiGeneresList(authorizationToke: String,context: Context) {
        generesRepository = GeneresRepository().getInstance()
        generesListModel = generesRepository?.callApiGeneres(authorizationToke,context)
    }

    fun getGeneresList(): LiveData<GeneresListModel>? {
        return generesListModel
    }
}