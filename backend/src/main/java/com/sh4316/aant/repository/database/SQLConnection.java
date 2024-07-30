package com.sh4316.aant.repository.database;

import java.sql.Connection;
import java.sql.Statement;

public record SQLConnection(Connection conn, Statement stat) {

}
