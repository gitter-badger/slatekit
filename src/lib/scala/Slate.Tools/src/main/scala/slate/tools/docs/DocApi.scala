/**
<slate_header>
  url: www.slatekit.com
  git: www.github.com/code-helix/slatekit
  org: www.codehelix.co
  author: Kishore Reddy
  copyright: 2016 CodeHelix Solutions Inc.
  license: refer to website and/or github
  about: A Scala utility library, tool-kit and server backend.
  mantra: Simplicity above all else
</slate_header>
  */

package slate.tools.docs

import slate.common.Result
import slate.common.results.{ResultSupportIn}
import slate.core.apis.{Api, ApiAction}
import slate.core.common.svcs.ApiWithSupport

@Api(area = "sys", name = "docs", desc= "help doc generator",
  roles= "@admin", auth = "app", verb = "post", protocol = "*")
class DocApi extends ApiWithSupport
  with ResultSupportIn {


  @ApiAction(name = "", desc= "encryptes the text", roles= "", verb = "@parent", protocol = "@parent")
  def generate(root:String, output:String, template:String):Result[String] =
  {
    val doc = new DocService(root, output, template)
    val result = doc.process()
    result
  }
}
