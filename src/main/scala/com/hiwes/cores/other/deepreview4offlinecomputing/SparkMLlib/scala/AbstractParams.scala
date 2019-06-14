package com.hiwes.cores.other.deepreview4offlinecomputing.SparkMLlib.scala

import scala.reflect.runtime.universe._

/**
  * 适用于所有的参数类
  */
abstract class AbstractParams[T: TypeTag] {
  private def tag: TypeTag[T] = typeTag[T]

  override def toString: String = {
    val tpe = tag.tpe
    //    val allAccessors = tpe.declarations.collect {  // declarations弃用，使用decls代替。
    val allAccessors = tpe.decls.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }
    val mirror = runtimeMirror(getClass.getClassLoader)
    val instanceMirror = mirror.reflect(this)
    allAccessors.map { f =>
      val paramName = f.name.toString
      val fieldMirror = instanceMirror.reflectField(f)
      val paramValue = fieldMirror.get
      s"  $paramName:\t$paramValue"
    }.mkString("{\n", ",\n", "\n}")
  }
}

