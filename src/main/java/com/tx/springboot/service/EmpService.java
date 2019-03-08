package com.tx.springboot.service;

import com.tx.springboot.pojo.Emp;

import java.util.List;

public interface EmpService {
    public Emp select(int id);

    public void insert(Emp emp);

    public void delete(int id);

    public void update(Emp emp);

   // public List<Emp> selectAll();
}
