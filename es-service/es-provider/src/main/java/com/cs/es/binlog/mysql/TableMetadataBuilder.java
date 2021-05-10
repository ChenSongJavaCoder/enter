package com.cs.es.binlog.mysql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keosn
 * @date 2019/3/25 21:12
 */
@Service
@Slf4j
public class TableMetadataBuilder {

    @Autowired
    DataSource dataSource;

    public TableMetadata build(String database, String table, Long tableId) {
        TableMetadata tableMetadata = new TableMetadata();
        tableMetadata.setDatabase(database);
        tableMetadata.setTable(table);
        tableMetadata.setTableId(tableId);

        tableMetadata.setColumnMetadata(getColumnNames(database, table));
        getColumnNames(database, table);

        return tableMetadata;
    }

    public Map<Long, ColumnMetadata> getColumnNames(String database, String table) {
        Map<Long, ColumnMetadata> columnInfos = new HashMap<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT COLUMN_NAME, ORDINAL_POSITION FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ?");
            preparedStatement.setString(1, database);
            preparedStatement.setString(2, table);
            resultSet = preparedStatement.executeQuery();
            ColumnMetadata columnMetadata;
            while (resultSet.next()) {
                columnMetadata = new ColumnMetadata();
                columnMetadata.setName(resultSet.getString("COLUMN_NAME"));
                columnMetadata.setOrder(resultSet.getLong("ORDINAL_POSITION"));

                columnInfos.put(columnMetadata.getOrder(), columnMetadata);
            }
        } catch (Exception e) {
            log.error("Refresh table map information with error !", e);
        } finally {
            try {
                if (null != resultSet) resultSet.close();
            } catch (SQLException e) {
                log.error("Close database source 'ResultSet' error:", e);
            }

            try {
                if (null != preparedStatement) preparedStatement.close();
            } catch (SQLException e) {
                log.error("Close database source 'PreparedStatement' error:", e);
            }

            try {
                if (null != connection) connection.close();
            } catch (SQLException e) {
                log.error("Close database source 'Connection' error:", e);
            }
        }
        return columnInfos;
    }

}
