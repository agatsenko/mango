/**
  * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
  * Created: 2018-09-12
  */
package io.mango

package object sql {
  private[sql] val defaultSqlValueConverter: SqlValueConverter = new SqlValueConverter {}

  object Implicits {
    object All extends ConnectionExtImplicits
                       with ResultSetReaders
                       with OptionResultSetReader
                       with ResultSetExtImplicits
                       with SqlQueryBuilderImplicits {
      implicit val defaultSqlValueConverter: SqlValueConverter = sql.defaultSqlValueConverter
    }

    object ConnectionExt extends ConnectionExtImplicits

    object ResultSetExt extends ResultSetExtImplicits with ResultSetReaders with OptionResultSetReader

    object SqlQueryBuilder extends SqlQueryBuilderImplicits {
      implicit val defaultSqlValueConverter: SqlValueConverter = sql.defaultSqlValueConverter
    }
  }
}
