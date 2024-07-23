package com.example.mymusicapp.callback

import com.example.mymusicapp.data.dto.PlayList

interface LoadDataListener {
    fun onSuccess(data: ArrayList<PlayList>)
}