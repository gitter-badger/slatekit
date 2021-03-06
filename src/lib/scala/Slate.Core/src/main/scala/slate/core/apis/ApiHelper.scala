/**
  * <slate_header>
  * author: Kishore Reddy
  * url: https://github.com/kishorereddy/scala-slate
  * copyright: 2015 Kishore Reddy
  * license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  * desc: a scala micro-framework
  * usage: Please refer to license on github for more info.
  * </slate_header>
  */

package slate.core.apis

import slate.common.results.ResultSupportIn

import scala.collection.mutable.Map
import slate.common.{InputArgs, Strings, Result}
import slate.core.apis.support.ApiCallReflect

object ApiHelper extends ResultSupportIn {

  def isValidMatch(actual:String, expected:String):Boolean = {
    if ( Strings.isNullOrEmpty(actual) || actual == "*" )
      true
    else
      Strings.isMatch(actual, expected)
  }


  def buildArgs(inputs:Option[List[(String,String)]]):InputArgs = {

    // fill args
    val rawArgs = Map[String,Any]()
    if(inputs.isDefined) {
      for (input <- inputs.get) {
        rawArgs(input._1) = input._2
      }
    }
    val args = new InputArgs(rawArgs)
    args
  }


  def buildCmd(path:String,
               inputs:Option[List[(String,String)]],
               headers:Option[List[(String,String)]]): Request = {

    val tokens = Strings.split(path, '.').toList
    val args = Some(buildArgs(inputs))
    val opts = Some(buildArgs(headers))
    val apiCmd = new Request(path, tokens, tokens(0), tokens(1), tokens(2), "get", args, opts)
    apiCmd
  }


  /**
   *  Checks the action and api to ensure the current request (cmd) is authorizated to
    *  make the call
   */
  def isAuthorizedForCall(cmd:Request, call:ApiCallReflect, auth:Option[ApiAuth]):Result[Boolean] =
  {
    val noAuth = !auth.isDefined

    // CASE 1: No auth for action
    if(noAuth && (call.action.roles == ApiConstants.RoleGuest || Strings.isNullOrEmpty(call.action.roles) )){
      return ok()
    }
    // CASE 2: No auth for parent
    if(noAuth && call.action.roles == ApiConstants.RoleParent
        && call.api.roles == ApiConstants.RoleGuest){
      return ok()
    }
    // CASE 3: No auth and action requires roles!
    if(noAuth){
      return unAuthorized(msg = Some("Unable to authorize, authorization provider not set"))
    }
    // auth-mode, action roles, api roles
    auth.get.isAuthorized(cmd, call.api.auth, call.action.roles, call.api.roles)
  }


  def getReferencedValue(primaryValue:String, parentValue:String) : String = {

    // Role!
    if(!Strings.isNullOrEmpty(primaryValue) ){
      if(Strings.isMatch(primaryValue, ApiConstants.RoleParent)){
        return parentValue
      }
      return primaryValue
    }
    // Parent!
    if(!Strings.isNullOrEmpty(parentValue)){
      return parentValue
    }
    ""
  }


  /**
   * copies the annotation taking into account the overrides
    *
    * @param ano
   * @param roles
   * @param auth
   * @param protocol
   * @return
   */
  def copyApiAnnotation(ano:Api,
                        roles:Option[String] = None,
                        auth:Option[String] = None,
                        protocol:Option[String] = None ): Api = {
    if(!roles.isDefined && !auth.isDefined && !protocol.isDefined ){
      return ano
    }
    val finalRoles    = roles.getOrElse( ano.roles )
    val finalAuth     = auth.getOrElse( ano.auth )
    val finalProtocol = protocol.getOrElse( ano.protocol )
    ano.copy(ano.area, ano.name, ano.desc, finalRoles, finalAuth, ano.verb, finalProtocol )
  }
}
