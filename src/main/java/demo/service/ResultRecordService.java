package demo.service;

import java.util.List;
import java.util.Map;

/**
 * Created by cengruilin on 2017/11/2.
 */
public interface ResultRecordService {
    List<String> getColumns();

    List<Map<String, Object>> findAll();

    List<Map<String, Object>> findAllWithSort(String sortColumnName, String sortDirection);

}
