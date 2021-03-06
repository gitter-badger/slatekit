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

package slate.common.databases


import slate.common.Strings
import slate.common.Model
import slate.common.query.{QueryEncoder, Query}


/**
  * Builds up database tables, indexes and other database components
  */
class DbBuilder() {


  def addTable(model:Model): String =
  {
    var sql = ""

    // 1. build the "CREATE <tablename>
    sql += getCreateTable(model.name)

    // 2. build the primary key column
    sql += getPrimaryKey("id") + ","

    // 3. Now build all the columns
    var colsAdded = 0
    for( ndx <- 0 until model.fields.size)
    {
      val mapping = model.fields(ndx)
      if (!Strings.isMatch(mapping.name, "id"))
      {
        val sqlType = SqlType.getTypeFromScala(mapping.dataType)
        if(colsAdded > 0)
        {
          sql += ","
        }
        sql += addColNew(mapping.name, sqlType, mapping.isRequired, mapping.maxLength)
        colsAdded += 1
      }
    }

    // 4. finish the construction and get the sql.
    sql += " );"
    sql
  }


  def dropTable(name:String): String =
  {
    val tableName = QueryEncoder.ensureField(name)
    val sql = s"DROP TABLE IF EXISTS $tableName;"
    sql
  }


  def addCol(table:String, col:String, sqlType:Int): DbBuilder =
  {
    this
  }


  def addColNew(name:String, dataType:Int, required:Boolean = false, maxLen:Int = 0): String =
  {
    val nullText = if(required ) "NOT NULL" else ""
    val isText = dataType == SqlType.TypeString
    var maxLenText = maxLen.toString
    val colType = getColType(dataType, maxLen)
    val colName = getColName(name)

    val sql = " " + Strings.newline() + colName + " " + colType + " " + nullText
    sql
  }


  def getColName(name:String): String =
  {
    if (Strings.compare(name, "key", true) == 0)
      return "`" + name + "`"

    if (Strings.compare(name, "group", true) == 0)
      return "`" + name + "`"

    name + " "
  }


  def getColType(colType:Int, maxLen:Int): String =
  {
    if(colType == SqlType.TypeString && maxLen == -1)
    {
      return "longtext"
    }
    if(colType == SqlType.TypeString)
    {
      return "VARCHAR(" + maxLen + ")"
    }
    SqlType.getName(colType)
  }


  protected def getPrimaryKey(name:String): String =
  {
    val finalName = QueryEncoder.ensureField(name)
    s"`$finalName` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY"
  }


  protected def getCreateTable(name:String): String =
  {
    "create table `" + name + "` ( " + Strings.newline()
  }
}
