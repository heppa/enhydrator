package com.airhacks.enhydrator.in;

/*
 * #%L
 * enhydrator
 * %%
 * Copyright (C) 2014 Adam Bien
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.function.Function;

/**
 *
 * @author airhacks.com
 */
public class ResultSetToEntries implements Function<ResultSet, Row> {

    @Override
    public Row apply(ResultSet resultSet) {
        Row row = new Row();
        try {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int columnIndex = 0;
            for (int i = 1; i <= columnCount; i++) {
                columnIndex++;
                //from java.sql.Types
                int columnType = metaData.getColumnType(i);
                String columnName = metaData.getColumnName(i);

                Object value = null;
                switch (columnType) {
                    case Types.VARCHAR:
                    case Types.CHAR:
                        value = resultSet.getString(i);
                        break;
                    case Types.INTEGER:
                        value = resultSet.getInt(i);
                        break;
                    case Types.DOUBLE:
                        value = resultSet.getDouble(i);
                        break;
                    case Types.BOOLEAN:
                        value = resultSet.getBoolean(i);
                        break;
                    case Types.FLOAT:
                        value = resultSet.getFloat(i);
                        break;
                    default:
                        value = resultSet.getObject(i);
                }

                if(value == null){
                    row.addNullColumn(columnIndex, columnName);
                }
                else{
                    row.addColumn(columnIndex, columnName, value);
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Problems accessing ResultSet", ex);
        }
        return row;
    }

}
