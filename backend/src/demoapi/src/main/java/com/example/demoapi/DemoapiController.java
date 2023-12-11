package com.example.demoapi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoapiController {
    @Autowired
    private Connection snowflakeConnection;
    private SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    private JSONArray resultsetToJson(ResultSet resultSet) throws SQLException{
        ResultSetMetaData md = resultSet.getMetaData();
        int numCols = md.getColumnCount();
        List<String> colNames = IntStream.range(0, numCols)
                                    .mapToObj(i -> {
                                        try {
                                            return md.getColumnName(i + 1);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            return "?";
                                        }
                                    })
                                    .collect(Collectors.toList());

        JSONArray result = new JSONArray();
        while (resultSet.next()) {
            JSONObject row = new JSONObject();
            colNames.forEach(cn -> {
                try {
                    row.put(cn, resultSet.getObject(cn));
                } catch (JSONException | SQLException e) {
                    e.printStackTrace();
                }
            });
            result.put(row);
        }
        return result;
    }

    private String str_top_clerks =   "SELECT "
                                    + "  o_clerk AS O_CLERK, "
                                    + "  SUM(o_totalprice) AS CLERK_TOTAL "
                                    + "FROM snowflake_sample_data.tpch_sf10.orders "
                                    + "WHERE o_orderdate >= '%s' "
                                    + "  AND o_orderdate <= '%s' "
                                    + "GROUP BY o_clerk "
                                    + "ORDER BY clerk_total DESC "
                                    + "LIMIT %d"
                                    ;

    @RequestMapping(value = "/top_clerks", method= {RequestMethod.POST, RequestMethod.GET})
    public String top_clerks(@RequestParam(defaultValue = "1995-01-01") String start_range
                                , @RequestParam(defaultValue = "1995-03-31") String end_range 
                                , @RequestParam(defaultValue = "10") String topn
                                ) throws ParseException, SQLException {
        Date start_range_date = yyyy_MM_dd.parse(start_range);
        Date end_range_date = yyyy_MM_dd.parse(end_range);
        int topn_int = Integer.parseInt(topn);

        String sql = String.format(str_top_clerks,
                                    yyyy_MM_dd.format(start_range_date), 
                                    yyyy_MM_dd.format(end_range_date),
                                    topn_int);
        ResultSet rs = snowflakeConnection.createStatement().executeQuery(sql);

        JSONArray res = resultsetToJson(rs);
        return res.toString();
    }
}
