package com.example.gymmanagement.Utils.Database;

import static com.example.gymmanagement.Utils.Database.DatabaseQueriesNames.*;
import java.util.Map;

public class DatabaseQueries {
    private static Map<DatabaseQueriesNames, String> queries = Map.ofEntries(
            Map.entry(ADD_PAYMENT, "insert into \"Payments\" (idaccount, subscription, amount, time) VALUES (%d,'%s',%d,'%s')"),
            Map.entry(GET_PAYMENTS_FOR_CURRENT_USER, "select time, subscription, amount from \"Payments\" p join \"Accounts\" A on p.idaccount = A.idaccount where A.idaccount = %d"),
            Map.entry(DELETE_USER_BY_ID, "delete from \"Accounts\" where idaccount=%d;\n" +
                    "delete from \"Members\" where idmember=%d;\n "),
            Map.entry(GET_USER_BY_ID, "select * from \"Accounts\" a join \"Members\" m on m.idmember = a.idaccount where idmember = %d LIMIT 1"),
            Map.entry(GET_ALL_USERS, "select * from \"Accounts\" a join \"Members\" m on m.idmember = a.idaccount"),
            Map.entry(GET_DETAILS_FROM_USERNAME, "select firstname, lastname, weight, height, phone from \"Members\" m join \"Accounts\" a on a.idaccount = m.idmember where a.username='%s'" ),
            Map.entry(UPDATE_DETAILS_FROM_USERNAME,"update \"Members\" m set firstname='%s',lastname='%s',weight='%d',height='%d',phone='%s' from \"Accounts\" a where a.idaccount=m.idmember and username='%s' "),
            Map.entry(GET_USER_ID_FROM_USERNAME, "select idaccount from \"Accounts\" where username='%s'"),
            Map.entry(GET_USER_FROM_USERNAME, "select * from \"Accounts\" a join \"Members\" m on m.idmember = a.idaccount where username = '%s'")
    );

    public static String getQuery(DatabaseQueriesNames databaseQueriesName) {
        return queries.get(databaseQueriesName);
    }
}
