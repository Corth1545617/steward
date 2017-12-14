package com.rent.steward.user;

import java.util.List;

/**
 * Created by Corth1545617 on 2017/6/5.
 */

public interface IPersonInfoDAO {

    Person insert(Person person);

    Person findByAccount(String account);

    List<Person> getAll();

    boolean update(Person person);

    boolean delete(long id);

}
