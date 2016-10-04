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

package slate.common

/**
  * Semantic console writer to print text in different colors and in Uppercase/lowercase for
  * things like title, subtitle, url etc.
  */
class ConsoleWriter
{
  /**
    * prints a empty line
    */
  def line()
  {
    println()
  }


  /**
    * prints a tab count times
    * @param count
    */
  def tab(count:Int = 1)
  {
    for (ndx <- 0 to count)
    {
      print("\t")
    }
  }


  /**
    * prints text in title format ( UPPERCASE and BLUE )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def title(text:String, endLine:Boolean = true):Unit =
  {
    var finalText = text
    if(!Strings.isNullOrEmpty(finalText))
      finalText = finalText.toUpperCase()
    write(Console.BLUE, finalText, endLine)
  }


  /**
    * prints text in subtitle format ( CYAN )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def subTitle(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.CYAN, text, endLine)
  }


  /**
    * prints text in url format ( BLUE )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def url(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.BLUE, text, endLine)
  }


  /**
    * prints text in important format ( RED )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def important(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.RED, text, endLine)
  }


  /**
    * prints text in highlight format ( YELLOW )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def highlight(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.YELLOW, text, endLine)
  }


  /**
    * prints text in error format ( RED )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def error(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.RED, text, endLine)
  }


  /**
    * prints text in title format ( UPPERCASE and BLUE )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def success(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.GREEN, text, endLine)
  }


  /**
    * prints text in normal format ( WHITE )
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def text(text:String, endLine:Boolean = true):Unit =
  {
    write(Console.WHITE, text, endLine)
  }


  /**
    * prints text in the color supplied.
    * @param text    : the text to print
    * @param endLine : whether or not to include a newline at the end
    */
  def write(color:String, text:String, endLine:Boolean): Unit =
  {
    print(color + " " + text )
    if(endLine)
      println()
  }
}