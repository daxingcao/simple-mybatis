package com.caodaxing.mybatis.test;

import com.caodaxing.mybatis.entry.User;

import java.util.List;

public interface TestInterface {

    List<User> queryUser();

    void updateUser();

}
