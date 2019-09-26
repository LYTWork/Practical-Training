package com.java1234;

import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListDrugs;
import com.java1234.service.CustomerReturnListDrugsService;
import com.java1234.service.CustomerReturnListService;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LAT
 * @date 2019/5/13 - 23:00
 **/
public class CustomerReturnListServiceImplTest {

    @Autowired
    CustomerReturnListService customerReturnListService;


    @Test
    public void list() {

    }

//    @Test
//    public void delete() {
//        Integer id = 8;
//        customerReturnListService.delete(id);
//    }
//
//    @Test
//    public void findById() {
//        Integer id = 7;
//        CustomerReturnList result = customerReturnListService.findById(id);
//    }

    @Test
    public void update() {
    }
}
