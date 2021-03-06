/**
  <header>
    <author>Kishore Reddy</author>
    <url>https://github.com/kishorereddy/scala-slate</url>
    <copyright>2015 Kishore Reddy</copyright>
    <license>https://github.com/kishorereddy/scala-slate/blob/master/LICENSE.md</license>
    <desc>a scala micro-framework</desc>
    <usage>Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    </usage>
  </header>
  */
package slate.core.cmds

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

import slate.common.DateTime


/**
  * Light-weight implementation of a command pattern with some additional features.
  * This allows a function call/method to be wrapped inside a command that can return:
  *
  * 1. name of the command
  * 2. success/failure of the command
  * 3. message of success/failure
  * 4. result of the command
  * 5. time started
  * 6. time ended
  * 7. duration of the execution
  *
  * @param name
  */
class Cmd(val name: String) {

  var _state: CmdState = new CmdState(name, DateTime.now(), false, 0, 0, null)
  var _result: CmdResult = null


  def state : CmdState = _state.copy

  def result : CmdResult = _result.copy()

  /**
   * execute this command with optional arguments
   * @param args
   * @return
   */
  def execute(args:Array[Any] = null): CmdResult =
  {
    // track state
    val start = DateTime.now
    var end = start
    var success = false
    var msg = ""
    var executionResult:AnyRef = null
    var executeTimeMilli = 0d

    try
    {
      _state.lastRuntime = start
      executionResult = executeInternal(args)
      success = true
    }
    catch {
      case e: Exception => {
        _state.errorCount += 1
        msg = e.getMessage
      }
    }
    finally
    {
      _state.runCount += 1
      _state.hasRun = true
    }

    // calculate total time
    end = DateTime.now

    _result = new CmdResult(name, success, msg, Some(executionResult), 0, start, end, _state.runCount)
    _result
  }


  /**
   * executes the command, this should be overridden in sub-classes
   * @param args
   * @return
   */
  protected def executeInternal(args: Any) : AnyRef =
  {
    null
  }
}
