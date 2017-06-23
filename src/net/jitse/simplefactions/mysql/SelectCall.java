package net.jitse.simplefactions.mysql;

import java.sql.ResultSet;

/**
 * Created by Jitse on 23-6-2017.
 */
public interface SelectCall {

    void call(ResultSet resultSet);
}
