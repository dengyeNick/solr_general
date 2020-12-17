package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    	System.out.println("sdfs");
    	String time="2020-12";
    	int start=time.indexOf("-")+1;
    	System.out.println(start);
    	int end=time.lastIndexOf("-");
    	System.out.println(start);
    	Calendar c = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	
    	try {
    		c.setTime(sdf.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String month = String.valueOf(c.get(Calendar.MONTH) + 1);
    	if(Integer.valueOf(month)<10) {
    		month="0"+month;
    	}
    	System.out.println( month);
    }
    
    
    @Test
    void testString() {
    	String time="2020-02";
    	String copy=time;
    	for (int i=0;i<10;i++) {
    		time =copy;
    		time+="-01";
    		System.out.println(time);
    	}
    }
    
    
    @Test
    void mapS() {
    	Map<String, Object> artTmpIds=new HashMap<String, Object>();
    	com(artTmpIds);
    }
    
    public void com(Map<String, Object> artTmpIds) {
    	try {
    		if (artTmpIds.size()>0) {
    			
    		}else {
    			System.out.println("数据为空");
    		}
		} catch (Exception e) {
		System.out.println("出现错误");
		}
    	
    }
    

}
