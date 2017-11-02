package demo.service.impl;

import demo.service.ResultRecordService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cengruilin on 2017/11/2.
 */
@Service
public class ResultRecordServiceImpl implements ResultRecordService, InitializingBean {

    private String tableName = "result_set";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<String> columns;

    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override
    public List<Map<String, Object>> findAll() {
        return jdbcTemplate.queryForList("SELECT * from " + tableName);
    }

    @Override
    public List<Map<String, Object>> findAllWithSort(String sortColumnName, String sortDirection) {
        return jdbcTemplate.queryForList("SELECT * from " + tableName + " ORDER BY " + sortColumnName + " " + sortDirection);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Map<String, Object>> queryResult = jdbcTemplate.queryForList("SELECT * from " + tableName + " LIMIT 1");
        if (queryResult.isEmpty()) {
            throw new IllegalStateException("No any data, can't get the columns");
        }
        columns = new ArrayList<>(queryResult.get(0).keySet());
    }
}
