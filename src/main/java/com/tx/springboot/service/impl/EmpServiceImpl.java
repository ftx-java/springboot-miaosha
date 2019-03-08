package com.tx.springboot.service.impl;

import com.tx.springboot.dao.EmpMapper;
import com.tx.springboot.pojo.Emp;
import com.tx.springboot.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Demo class
 *
 * @author tx
 * @date 2018/10/29
 */
@Service
@Transactional
public class EmpServiceImpl implements EmpService {
    @Autowired
    EmpMapper empMapper;

    @Override
    public Emp select(int id) {
        return empMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insert(Emp emp) {
        empMapper.insert(emp);
    }

    @Override
    public void delete(int id) {
         empMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Emp emp) {
        empMapper.updateByPrimaryKey(emp);
    }

//    @Override
//    public List<Emp> selectAll() {
//
//        return empMapper;
//    }
}
