/**
<slate_header>
  author: Kishore Reddy
  url: https://github.com/kishorereddy/scala-slate
  copyright: 2015 Kishore Reddy
  license: https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md
  desc: a scala micro-framework
  usage: Please refer to license on github for more info.
</slate_header>
  */

package slate.common.info

import slate.common.Strings

/**
 * represent meta-data about an application
 * @param id      : id of app
 * @param name    : name of app
 * @param desc    : desc of app
 * @param company : company the app is associated with
 * @param group   : group owning the app
 * @param region  : region associated with app
 * @param url     : url for more information
 * @param contact : contact person(s) for app
 * @param version : version of the app
 * @param tags    : tags describing the app
 */
case class About (
                     id       : String = "",
                     name     : String = "",
                     desc     : String = "",
                     company  : String = "",
                     group    : String = "",
                     region   : String = "",
                     url      : String = "",
                     contact  : String = "",
                     version  : String = "",
                     tags     : String = "",
                     examples : String = ""
                 )
{

  def log( callback:(String,String) => Unit) : Unit = {

    callback("name    ", name     )
    callback("desc    ", desc     )
    callback("group   ", group    )
    callback("region  ", region   )
    callback("url     ", url      )
    callback("contact ", contact  )
    callback("version ", version  )
    callback("tags    ", tags     )
    callback("examples", examples )
  }


  def toStringProps():String = {
    var text = ""
    val newLine = Strings.newline()
    text += "name     : " +  name     + newLine
    text += "desc     : " +  desc     + newLine
    text += "group    : " +  group    + newLine
    text += "region   : " +  region   + newLine
    text += "url      : " +  url      + newLine
    text += "contact  : " +  contact  + newLine
    text += "version  : " +  version  + newLine
    text += "tags     : " +  tags     + newLine
    text += "examples : " +  examples + newLine
    text
  }


  def dir:String = {
    Strings.valueOrDefault(company, name).replaceAllLiterally(" ", "-")
  }
}



object About
{
  val none = new About(
    id       = "",
    name     = "",
    desc     = "",
    company  = "",
    group    = "",
    region   = "",
    url      = "",
    contact  = "",
    examples = ""
  )
}
