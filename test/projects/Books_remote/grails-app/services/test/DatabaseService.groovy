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


/*
package de.lineas.proms.test

import groovy.sql.Sql

import javax.sql.DataSource


class ClearTestDataService {
//    def grailsApplication

    DataSource dataSource

//    def createTestData () {
//        Sql sql = new Sql (dataSource)
//
//        schema
//        String sqlString = new File ('scripts/thereda_create_schema.sql').text
//        sql.execute(sqlString)
//
//        data
//        sqlString = new File ('scripts/thereda_insert_data.sql').text
//        sql.execute(sqlString)
//        sql.commit()
//        sql.close()
//    }

    def clearData () {
//        works in dev and prod?
//        def test = grailsApplication.mainContext.getResource ('grails-app/sql/clear.sql')
//        def exists = test.exists()

        def script = new File ('sql/clear.sql').text

        Sql sql = new Sql (dataSource)

        sql.withTransaction { def c ->
            sql.execute (script)
            sql.commit()
        }
        sql.close ()
    }

}








 */
