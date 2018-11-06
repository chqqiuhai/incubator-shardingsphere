/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.shardingjdbc.jdbc.core.connection;

import io.shardingsphere.core.constant.transaction.TransactionType;
import io.shardingsphere.shardingjdbc.jdbc.adapter.AbstractConnectionAdapter;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import io.shardingsphere.shardingjdbc.jdbc.core.statement.MasterSlavePreparedStatement;
import io.shardingsphere.shardingjdbc.jdbc.core.statement.MasterSlaveStatement;
import lombok.Getter;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Connection that support master-slave.
 * 
 * @author zhangliang
 * @author zhaojun
 */
@Getter
public final class MasterSlaveConnection extends AbstractConnectionAdapter {
    
    private final MasterSlaveDataSource masterSlaveDataSource;
    
    public MasterSlaveConnection(final MasterSlaveDataSource masterSlaveDataSource) {
        super(TransactionType.LOCAL);
        this.masterSlaveDataSource = masterSlaveDataSource;
    }
    
    public MasterSlaveConnection(final MasterSlaveDataSource masterSlaveDataSource, final TransactionType transactionType) {
        super(transactionType);
        this.masterSlaveDataSource = masterSlaveDataSource;
    }
    
    @Override
    protected Map<String, DataSource> getDataSourceMap() {
        return masterSlaveDataSource.getDataSourceMap();
    }
    
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection(masterSlaveDataSource.getMasterSlaveRule().getMasterDataSourceName()).getMetaData();
    }
    
    @Override
    public Statement createStatement() {
        return new MasterSlaveStatement(this);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency) {
        return new MasterSlaveStatement(this, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
        return new MasterSlaveStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, resultSetType, resultSetConcurrency);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, columnIndexes);
    }
    
    @Override
    public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
        return new MasterSlavePreparedStatement(this, sql, columnNames);
    }
}
