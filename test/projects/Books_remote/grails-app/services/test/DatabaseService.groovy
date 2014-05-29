package test

import groovy.sql.Sql

import javax.sql.DataSource


class DatabaseService {
    DataSource dataSource

    void reset () {
        def script = new File ('sql/reset.sql').text

        Sql sql = new Sql (dataSource)

        sql.withTransaction { def c ->
            sql.execute (script)
            sql.commit()
        }
        sql.close ()
    }
}
