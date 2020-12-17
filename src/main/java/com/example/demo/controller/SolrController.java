package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.client.solrj.response.TermsResponse;
import org.apache.solr.client.solrj.response.TermsResponse.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@RestController
public class SolrController {
	@Value("${spring.data.solr.host}")
	private String url;

	@RequestMapping("test")
	public  void dataImportSolrData() throws SolrServerException, IOException {
		 //[1]获取连接
		String solrUrl = url+"/test_core";
		HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
    	        .withConnectionTimeout(10000)
    	        .withSocketTimeout(60000)
    	        .build();
        //[2]创建SolrQuery
        SolrQuery query = new SolrQuery();
        //[3]设置参数
        query.setRequestHandler("/terms");//设置requestHandler
        query.setTerms(true);//开启terms
        query.setTermsLimit(10);//设置每页返回的条目数量
//        query.setTermsLower("");// 可选的. 这个term开始。如果不指定,使用空字符串,这意味着从头开始的。
        query.setTermsPrefix("123");//可选的. 限制匹配，设置terms前缀是以什么开始的。
        query.addTermsField("ta");//必须的. 统计的字段
        query.setTermsMinCount(1);//可选的. 设置最小统计个数
        //[4]创建QueryRequest 获取 TermsResponse 
        QueryRequest request = new QueryRequest(query);
        QueryResponse process = request.process(client);
        TermsResponse termsResponse = process.getTermsResponse();
        //[5]遍历结果
        List<Term> terms = termsResponse.getTerms("ta");
        for (Term term : terms) {
            System.out.println(term.getTerm() + ":\t"+ term.getFrequency());
        }
	}
	
	@RequestMapping("testTerms")
	public  String TestTerms(@RequestParam("keyword")String keyword) throws SolrServerException, IOException {
		//[1]获取连接
		String solrUrl = url+"/test_core";
		HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
				.withConnectionTimeout(10000)
				.withSocketTimeout(60000)
				.build();
		 //[2]创建SolrQuery
        SolrQuery query = new SolrQuery();
        //[3]设置查询参数  
        query.set("q", "*:*");  
        query.set("qt","/terms");//设置requestHandler
        
        // parameters settings for terms requesthandler  
        // 参考 http://wiki.apache.org/solr/termscomponent  
        query.set("terms","true");//开启terms
        query.set("terms.fl", "ta");//必须的. 统计的字段  
        
        //指定下限  
        // query.set("terms.lower", ""); // term lower bounder开始的字符  ，// 可选的. 这个term开始。如果不指定,使用空字符串,这意味着从头开始的。
        // query.set("terms.lower.incl", "true");  
        // query.set("terms.mincount", "1");//可选的. 设置最小统计个数  
        // query.set("terms.maxcount", "100"); //可选的. 设置最大统计个数   
        
        //http://localhost:8983/solr/terms?terms.fl=text&terms.prefix=家//  
        //using for auto-completing   //自动完成  
        //query.set("terms.prefix", "家");//可选的. 限制匹配，设置terms前缀是以什么开始的。 
        String keyworkdString=keyword;
        query.set("terms.regex", keyworkdString+"+.*");  
        query.set("terms.regex.flag", "case_insensitive");  
         
        //query.set("terms.limit", "20"); //设置每页返回的条目数量 
        //query.set("terms.upper", ""); //结束的字符  
        //query.set("terms.upper.incl", "false");  
        //query.set("terms.raw", "true");  
        
        query.set("terms.sort", "index");//terms.sort={count|index} -如果count，各种各样的条款术语的频率（最高计数第一）。 如果index，索引顺序返回条款。默认是count     
        query.setTermsLimit(10);//设置每页返回的条目数量
        // 查询并获取结果  
        QueryResponse response = client.query(query);  
        // 获取相关的查询结果  
        List<TermsResponse.Term> termList=null;
        if (response != null) {  
            TermsResponse termsResponse = response.getTermsResponse();  
            if (termsResponse != null) {  
                Map<String, List<TermsResponse.Term>> termsMap = termsResponse.getTermMap();  
                for (Map.Entry<String, List<TermsResponse.Term>> termsEntry : termsMap.entrySet()) {  
                    //System.out.println("Field Name: " + termsEntry.getKey());  
                    termList = termsEntry.getValue();  
                    for (TermsResponse.Term term : termList) {  
                        System.out.println(term.getTerm() + " : "+ term.getFrequency());  
                    }  
                }  
            }  
        }  
        String jsonstr = JSON.toJSONString(termList);
        return jsonstr;  
	}
	
	
	@RequestMapping("testSuggest")
	public  String testSuggest(@RequestParam("keyword")String keyword) throws SolrServerException, IOException {
		//[1]获取连接
		String solrUrl = url+"/test_core";
		HttpSolrClient client = new HttpSolrClient.Builder(solrUrl)
				.withConnectionTimeout(10000)
				.withSocketTimeout(60000)
				.build();
		//[2]创建SolrQuery
		SolrQuery query = new SolrQuery();
		   query.setRequestHandler("/suggester");//设置requestHandler
		   query.set("qt", "/suggest");
		   query.setQuery("ta:12*");

	    query.setStart(0);
	    query.setRows(10);

		// 查询并获取结果  
		QueryResponse response = client.query(query);  
		// 获取相关的查询结果  
		 SpellCheckResponse suggest = response.getSpellCheckResponse();  
	        List<Suggestion> suggestionList = suggest.getSuggestions();  
	        for (Suggestion suggestion : suggestionList) {  
	            System.out.println("Suggestions NumFound: " + suggestion.getNumFound());  
	            System.out.println("Token: " + suggestion.getToken());  
	            System.out.print("Suggested: ");  
	            List<String> suggestedWordList = suggestion.getAlternatives();  
	            for (String word : suggestedWordList) {  
	                System.out.println(word + ", ");  
	            }  
	            System.out.println();  
	        }    
		String jsonstr = JSON.toJSONString(suggestionList);
		return jsonstr;  
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 自定义同义词
	 * @param map
	 * @return
	 */
	@RequestMapping("reload")
	public String reload(@RequestBody List<String> map) {
		updateSynonyms(JSON.toJSONString(map));
		String urlString="http://localhost:8983/solr/admin/cores?action=RELOAD&core=test_core";
	    ResponseEntity<String> responseEntity=	restTemplate.getForEntity(urlString, String.class);
	    if (responseEntity.getStatusCodeValue()==HttpStatus.SC_OK) {
	    	return "成功";
		}else {
			return "失败";
		}
	}
	
	public void updateSynonyms(String jsonParam) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse;
		try {
			String updateAssetUrl="http://localhost:8983/solr/test_core/schema/analysis/synonyms/english";
	        HttpPost httpPost = new HttpPost(updateAssetUrl);
	        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
//	        String jsonParam="{\"123×240\":[\"3+241\",\"3+243\"]}";
	        StringEntity entity = new StringEntity(jsonParam, ContentType.create("text/json", "UTF-8"));
	        httpPost.setEntity(entity);
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
	        httpPost.setConfig(requestConfig);
	        
	        closeableHttpResponse = closeableHttpClient.execute(httpPost);
	
	        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
	        if (statusCode == HttpStatus.SC_OK) {
	            //TODO:状态码非200代表没有正常返回,此处处理你的业务
	        	System.out.println("成功");
	        }else {
	        	System.out.println("失败");
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	
	}
	
	
	
	
}
