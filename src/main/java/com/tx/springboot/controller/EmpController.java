package com.tx.springboot.controller;

import com.tx.springboot.pojo.Emp;
import com.tx.springboot.service.EmpService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Demo class
 * http://localhost:8082/emp/select/1
 * http://localhost:8082/emp/delete/3
 * http://localhost:8082/emp/update?id=2&lastname=bb&email=ww&gender=3
 * http://localhost:8082/emp/insert?id=4&lastname=hahaha&email=qw&gender=7
 *
 * @author tx
 * @date 2018/10/29
 */
@RestController
@RequestMapping("/emp")

public class EmpController {
    @Autowired
    EmpService empService;

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户的详细信息")
    /**
     * @ApiImplicitParam(name = "id",value = "用户ID",required = true,dataType = "int")
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://127.0.0.1:8020")
    public Emp select(@RequestParam("id") int id) {
        return empService.select(id);

    }

    @ApiOperation(value = "新增用户信息", notes = "根据url的id指定新增对象，并根据传过来的emp表信息来增加用户详细信息")
    /**
     *  @ApiImplicitParams({
     *             @ApiImplicitParam(name = "id", value = "用户" +
     *                     "ID", required = true, dataType = "int"),
     *             @ApiImplicitParam(name = "emp", value = "用户详细实体emp", required = true, dataType = "emp")
     *     })
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://127.0.0.1:8020")
    public void insert(Emp emp) {
        empService.insert(emp);
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    /**
     * @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int")
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://127.0.0.1:8020")
    public void delete(@RequestParam("id") int id) {
        empService.delete(id);
    }

    @ApiOperation(value = "更新用户信息", notes = "根据url的id指定更新对象，并根据传过来的emp表信息来更新用户详细信息")
    /**
     *  @ApiImplicitParams({
     *             @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int"),
     *             @ApiImplicitParam(name = "emp", value = "用户详细实体emp", required = true, dataType = "emp")
     *     })
     */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://127.0.0.1:8020")
    public void update(Emp emp) {
        empService.update(emp);
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder, WebRequest request) {
//
//        //转换日期
//        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
//    }

}
