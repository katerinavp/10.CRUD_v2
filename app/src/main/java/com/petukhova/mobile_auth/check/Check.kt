package com.petukhova.mobile_auth.check

class Check() {

    fun checktextInputAuth(login: String, password: String): Boolean { // проверка на пустые поля ввода
        return !(login.isEmpty() || password.isEmpty())
    }

    fun checktextInputReg(login: String, password: String, passwordRepeat: String): Boolean { // проверка на пустые поля ввода всех полей в активити
        return !(login.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty())
    }

    fun checkPassword(password1:String, password2:String) :Boolean{ //проверка паролей на совпадение
        return(password1.equals(password2))
    }
    fun checkPostIsNotEmpty(txtInputBodyPost: String):Boolean {// проверка на создание нового поста
        return txtInputBodyPost.isEmpty()
    }

}