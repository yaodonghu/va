package demo.web.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import demo.service.ResultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by cengruilin on 2017/11/2.
 */
@RestController
@RequestMapping("")
public class DatatableRestContoller {

    @Autowired
    private ResultRecordService resultRecordService;

    @RequestMapping(value = "/datatables_data_head", method = RequestMethod.GET)
    public Object datatables_data_head() {
        List<String> columns = new ArrayList<>();
        columns.add("User");
        columns.add("Pred");
        columns.add("P(yes)");
        columns.add("Margin");
        columns.add("RowID");
        columns.addAll(resultRecordService.getColumns());
        return columns;
    }

    @RequestMapping(value = "/datatables_data", method = RequestMethod.GET)
    public Object datatables_data(int draw, int start, int length,
                                  HttpServletRequest request) {

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (name.indexOf("columns") > -1) {

            }
        }
        final RowIdGenerator rowIdGenerator = new RowIdGenerator();

        List<Map<String, Object>> queryResult = null;
        String orderColumnIndexStr = request.getParameter("order[0][column]");
        if (!Strings.isNullOrEmpty(orderColumnIndexStr)) {
            String sortDirection = request.getParameter("order[0][dir]");
            String columnName = request.getParameter(String.format("columns[%s][name]", orderColumnIndexStr));
            if (!Strings.isNullOrEmpty(columnName)) {
                queryResult = resultRecordService.findAllWithSort(columnName, sortDirection);
            }
        }

        if (null == queryResult) {
            queryResult = resultRecordService.findAll();
        }


        queryResult.stream().forEach(item -> {
            item.put("P(yes)", "nan");
            item.put("Margin", "nan");
            item.put("Pred", "-");
            item.put("User",
                    String.format("<a href='javascript:label_example(%s, \"yes\")'>yes</a>&nbsp;<a href='javascript:label_example(%s, \"no\")'>no</a>", 0, 0));
            item.put("RowID", rowIdGenerator.generateRowId());
        });

        int recordsTotal = queryResult.size();
        int recordsFiltered = recordsTotal;
        Object data = queryResult;

        Map<String, Object> result = Maps.newHashMap();
        result.put("data", data);
        result.put("draw", draw);
        result.put("recordsTotal", recordsTotal);
        result.put("recordsFiltered", recordsFiltered);


        return result;
    }

    @RequestMapping("/datatables_ft")
    public Object datatables_ft() {
        return "{\"ftCoefs\":[[\"percentile__ks_stat\",0.0],[\"n__n\",0.0],[\"min__min\",0.0],[\"p1__p1\",0.0],[\"p5__p5\",0.0],[\"p10__p10\",0.0],[\"p25__p25\",0.0],[\"p50__p50\",0.0],[\"p75__p75\",0.0],[\"p90__p90\",0.0],[\"p95__p95\",0.0],[\"p99__p99\",0.0],[\"max__max\",0.0],[\"TestName__height\",0.0],[\"TestName__ht\",0.0],[\"TestName__stature\",0.0],[\"Component__digital\",0.0],[\"Component__height\",0.0],[\"Component__reported\",0.0],[\"Component__rod\",0.0],[\"Component__self\",0.0],[\"Component__stadiometer\",0.0],[\"Topography__adult\",0.0],[\"Topography__body\",0.0],[\"Topography__full\",0.0],[\"Topography__height\",0.0],[\"Units__feet\",0.0],[\"Units__ft\",0.0],[\"LOINC__1754-1\",0.0],[\"LOINC__6793-4\",0.0],[\"LOINC__6793-4\\r\\n\",0.0]]}\n";
    }

    @RequestMapping("/datatables_stats")
    public Object datatables_stats() {
        return "{\"ftCoefs\":{\"Component__digital\":0.0,\"Component__height\":0.0,\"Component__reported\":0.0,\"Component__rod\":0.0,\"Component__self\":0.0,\"Component__stadiometer\":0.0,\"LOINC__1754-1\":0.0,\"LOINC__6793-4\":0.0,\"LOINC__6793-4\\r\\n\":0.0,\"TestName__height\":0.0,\"TestName__ht\":0.0,\"TestName__stature\":0.0,\"Topography__adult\":0.0,\"Topography__body\":0.0,\"Topography__full\":0.0,\"Topography__height\":0.0,\"Units__feet\":0.0,\"Units__ft\":0.0,\"max__max\":0.0,\"min__min\":0.0,\"n__n\":0.0,\"p10__p10\":0.0,\"p1__p1\":0.0,\"p25__p25\":0.0,\"p50__p50\":0.0,\"p5__p5\":0.0,\"p75__p75\":0.0,\"p90__p90\":0.0,\"p95__p95\":0.0,\"p99__p99\":0.0,\"percentile__ks_stat\":0.0},\"trainAccuracy\":0.0,\"trainPrecision\":0.0,\"trainRecall\":0.0}\n";
    }

    static class RowIdGenerator {
        private int rowId = 0;

        public int generateRowId() {
            return rowId++;
        }
    }
}
