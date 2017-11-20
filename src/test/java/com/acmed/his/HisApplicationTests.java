package com.acmed.his;

import com.acmed.his.dao.DictMapper;
import com.acmed.his.model.Dict;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HisApplicationTests {

	@Autowired
	private DictMapper dictMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRun() {
		PageHelper.startPage(1, 2);
		dictMapper.selectAll().forEach(obj -> System.out.println(">>>>>>>>>>>>>>rs>>" + obj.toString()));
	}

}
