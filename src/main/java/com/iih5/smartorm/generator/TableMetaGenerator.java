package com.iih5.smartorm.generator;/*
 * Copyright 2016 xueyi (1581249005@qq.com)
 *
 * The SmartORM Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import com.iih5.smartorm.model.Db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableMetaGenerator {
    /**
     * 从db获取库表和字段信息
     * @param dataSource
     * @param dbName
     * @return
     * @throws Exception
     */
    public List<TableMetaTest> findTableMetaList(String dataSource, String dbName) throws Exception{
        List<TableMetaTest> tableList = new ArrayList<TableMetaTest>();
        Set<String> sets= new HashSet<String>();
        String sql="select TABLE_NAME,DATA_TYPE,COLUMN_NAME,COLUMN_COMMENT from information_schema.columns where table_schema='?' ";
        List<GeneratorModel> list = Db.use(dataSource).findList(sql,new Object[]{dbName}, GeneratorModel.class);
        for (GeneratorModel gModel:list) {
            sets.add(gModel.getStr("TABLE_NAME"));
        }
        for (String name:sets) {
            tableList.add(toTableMeta(name,list));
        }
        return tableList;
    }

    /**
     * 组合TableMeta
     * @param tableName
     * @param list
     * @return
     */
    private   TableMetaTest toTableMeta(String tableName, List<GeneratorModel> list){
        TableMetaTest tableMeta=new TableMetaTest();
        tableMeta.name=tableName;
        for (GeneratorModel model:list) {
            if (tableName.equals(model.getStr("TABLE_NAME"))){
                ColumnMetaTest columnMeta= new ColumnMetaTest();
                columnMeta.dataType=model.getStr("DATA_TYPE");
                columnMeta.name=  model.getStr("COLUMN_NAME");
                columnMeta.comment=model.getStr("COLUMN_COMMENT");
                tableMeta.columnMetas.add(columnMeta);
            }
        }
        return tableMeta;
    }
}