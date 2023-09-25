package com.mostafahelal.atmodrive2.auth.data.utils

sealed class Resource<out T> (
    val data : T? = null,
    val message : String? = null
){
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data,message)

    fun isSuccessful():Boolean{
        return this is Success
    }
    fun isFailed():Boolean{
        return this is Error
    }
}
fun  <T>Resource<T>.getData():T?{

    if (this is Resource.Success)
        return this.data

    return null
}
fun  <T>Resource<T>.getError():String?{

    if (this is Resource.Error)
        return this.message

    return null
}
