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

package slate.entities.repos

import slate.entities.core.IEntity

import scala.reflect.runtime.universe.Type


/**
  * Repository class specifically for MySql
  *
  * @param entityType
  * @tparam T
  */
class EntityRepoMySql [T >: Null <: IEntity ](entityType:Type)
  extends EntityRepoSql[T](entityType)
{

  override def top(count:Int, desc:Boolean ): List[T]  =
  {
    val orderBy = if(desc) " order by id desc" else " order by id asc"
    val sql = "select * from " + _tableName + orderBy + " limit " + count
    val items = _db.mapMany(sql, _mapper).getOrElse(List[T]())
    items
  }


  override protected def scriptLastId(): String =
  {
    "SELECT LAST_INSERT_ID();"
  }


  override protected def tableName:String =
  {
    "`" + _tableName.toString + "`"
  }
}
