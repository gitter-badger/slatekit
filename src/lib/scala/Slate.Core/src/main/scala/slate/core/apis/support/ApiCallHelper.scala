/**
<slate_header>
  author: Kishore Reddy
  url: https://github.com/kishorereddy/scala-slate
  copyright: 2016 Kishore Reddy
  license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  desc: a scala micro-framework
  usage: Please refer to license on github for more info.
</slate_header>
  */

package slate.core.apis.support

import slate.common.results.{ResultSupportIn}
import slate.common.utils.Temp
import slate.core.apis.Request

import scala.reflect.runtime.universe.typeOf
import slate.common._

import scala.reflect.runtime.universe.Type
import scala.collection.mutable.ListBuffer

/**
  * Created by kreddy on 3/15/2016.
  */
object ApiCallHelper extends ResultSupportIn {

  protected var _tpeString:Type = Reflector.getFieldType(typeOf[Temp], "typeString")


  def validateArgs(callReflect:ApiCallReflect, args:Inputs): Result[Boolean] =
  {
    var error = ": inputs missing or invalid "
    var totalErrors = 0

    // Check each parameter to api call
    for(input <- callReflect.paramList )
    {
      // parameter not supplied ?
      val paramName = input.name
      if(!args.containsKey(paramName))
      {
        val separator = if(totalErrors == 0 ) "( " else ","
        error += separator + paramName
        totalErrors = totalErrors + 1
      }
    }
    // Any errors ?
    if(totalErrors > 0)
    {
      error = error + " )"
      return badRequest( Some(false), Some("bad request: action " + callReflect.name + error))
    }

    // Ok!
    ok()
  }


  def fillArgs(callReflect:ApiCallReflect, cmd:Request, args:Inputs): Array[Any] =
  {
    // Check 1: No args ?
    if(!callReflect.hasArgs)
      return Array[Any]()

    // Check 2: 1 param with default and no args
    if(callReflect.isSingleDefaultedArg() && args.size() == 0)
      return Array[Any](null)

    // Check each parameter to api call
    val inputs = new ListBuffer[Any]()
    for(ndx <- 0 until callReflect.paramList.size )
    {
      // Get each parameter to the method
      val parameter = callReflect.paramList(ndx)
      val paramName = parameter.name
      val paramType = parameter.typeName
      var paramValue:Any = null

      // Convert to appropriate type
      if(paramType == "String")
      {
        paramValue = args.getString(paramName)
        if("null".equalsIgnoreCase(paramValue.asInstanceOf[String]))
        {
          paramValue = null
        }
      }
      else if(paramType == "Int")
      {
        paramValue = args.getString(paramName).toInt
      }
      else if(paramType == "Boolean")
      {
        paramValue = args.getString(paramName).toBoolean
      }
      else if(paramType == "Long")
      {
        paramValue = args.getString(paramName).toLong
      }
      else if(paramType == "Double")
      {
        paramValue = args.getString(paramName).toDouble
      }
      else if(paramType == "DateTime")
      {
        paramValue = DateTime.parseNumericVal(args.getString(paramName))
      }
      else if(paramType == "ApiCmd")
      {
        paramValue = cmd
      }
      else if (!parameter.isBasicType()){
        val subObj = args.getObject(paramName).asInstanceOf[Inputs]
        paramValue = buildArgInstance(parameter, subObj)
      }

      inputs.append(paramValue)
    }
    inputs.toArray[Any]
  }


  private def buildArgInstance(parameter:ReflectedArg, inputs:Inputs):Any = {

    // Create object
    val instance = Reflector.createInstance(parameter.asType())

    // Get fields
    val fields = Reflector.getFieldsDeclared(instance.asInstanceOf[AnyRef])

    for(field <- fields) {
      val name = field.symbol.name.toString().trim()
      if ( inputs.containsKey(name)){

        val dataType = field.symbol.typeSignature.resultType
        if( dataType == _tpeString )
        {
          val sVal = inputs.getString(name)
          Reflector.setFieldValue(instance, name, sVal)
        }
        else if(dataType == typeOf[Boolean])
        {
          val bVal = inputs.getBool(name)
          Reflector.setFieldValue(instance, name, bVal)
        }
        else if(dataType == typeOf[Int])
        {
          val iVal = inputs.getInt(name)
          Reflector.setFieldValue(instance, name, iVal)
        }
        else if(dataType == typeOf[Long])
        {
          val lVal = inputs.getLong(name)
          Reflector.setFieldValue(instance, name, lVal)
        }
        else if(dataType == typeOf[Double])
        {
          val dVal = inputs.getDouble(name)
          Reflector.setFieldValue(instance, name, dVal)
        }
        else if(dataType == typeOf[DateTime])
        {
          val dVal = inputs.getDate(name)
          Reflector.setFieldValue(instance, name, dVal)
        }
      }
    }
    instance
  }
}
