package com.injectorsuite.myapplication

class KeyProviderSingleInstance(){
    companion object{
        val keys=mutableMapOf<String,String>()
        fun registerNewKey(name:String,key:String){
            keys.put(name,key)
        }
        fun fetchKey(name:String):String{
            if (keys[name]==null){
                throw(Error("API Key reference name not admissible. Admissible names are: "+keys.keys.toString()))
            }
            return keys[name]!!
        }
    }
}