package ch.hellorin.challengames.service.provider

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

object QueryHelper {
    fun buildFieldsQuery(klass : KClass<out Any>) : String {
        return "fields ${klass.declaredMemberProperties.map { it.name }.joinToString { it }};"
    }
}