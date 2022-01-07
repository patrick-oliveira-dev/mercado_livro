package com.mercadolivro.exception

class BusinessException(override val message: String, val errorCode: String): Exception() {

}