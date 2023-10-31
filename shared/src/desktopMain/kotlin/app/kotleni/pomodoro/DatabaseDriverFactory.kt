package app.kotleni.pomodoro

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:local.db")
            .also {
                if(!File("local.db").exists())
                    Database.Schema.create(it)
            }
    }
}