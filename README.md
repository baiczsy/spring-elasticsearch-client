# spring-elasticsearch-client
基于ES的RestHighLevelClient与spring整合插件
## 使用说明
clone项目并install到本地库(暂为发布到center仓库)。

### 依赖

~~~xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.1.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>1.0.0</version>
</dependency>
~~~

### 配置

~~~java
public class RestClientConfigure {

    /**
     * 配置RestClient连接池
     * @return
     */
    @Bean
    public RestClientPoolConfig poolConfig(){
        RestClientPoolConfig poolConfig = new RestClientPoolConfig();
        poolConfig.setMinIdle(5);
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxWaitMillis(2000);
        return poolConfig;
    }

    /**
     * 装配RestClient配置类(单点)
     * @return
     */
    @Bean
    public RestClientConfiguration restClientConfiguration(){
        return new RestClientStandaloneConfiguration("localhost",9200);
    }

    /**
     * 装配RestClient配置类(集群)
     * @return
     */
    /*@Bean
    public RestClientConfiguration clusterClientConfiguration(){
        List<ElasticsearchNode> hosts = new ArrayList<>();
        hosts.add(new ElasticsearchNode("localhost", 9200));
        hosts.add(new ElasticsearchNode("localhost", 9201));
        return new RestClientClusterConfiguration(hosts);
    }*/

    @Bean
    public ElasticsearchClientFactory restHighLevelClientFactory(RestClientConfiguration configuration, RestClientPoolConfig poolConfig){
        //如果需要，可以设置默认的请求头信息
        //Properties headers = new Properties();
        //headers.setProperty("key", "value");
        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(configuration, poolConfig);
        //factory.setDefaultHeaders(headers);
        return factory;
    }

    @Bean
    public RestClientTemplate restClientTemplate(ElasticsearchClientFactory factory){
        return new RestClientTemplate(factory);
    }
}
~~~

### 注入RestClientTemplate

~~~java
@Autowired
private RestClientTemplate template;
~~~

### 创建索引

~~~java
public void createIndex(String index, Map<String, Object> mapping) {
    template.opsForIndices().create(index, mapping);
}
~~~

### 删除索引

~~~java
public void deleteIndex(String index) {
    template.opsForIndices().delete("userinfo2");
}
~~~

### 添加数据

```java
public void add(String index, String id, Map<String, Object> params) {
    template.persist(index, id, params);
}
```

### 删除数据

```java
public void delete(String index, String id){
    template.delete(index, id);
}
```

### 检索数据

```java

public List<Map<String, Object>> search(String index, String param, String[] fields) {
    return template.opsForQuery()
            .from(0)
            .size(10)
            .multiMatch(index, param, fields);
}
```

### 异步操作

```java
public void searchAsync(String index, String param, String[] fields) {
    template.opsForQuery()
            .from(0)
            .size(10)
            .multiMatch(index, param, fields, new ActionListener<SearchResponse>() {
                @Override
                public void onResponse(SearchResponse searchResponse) {
                    for (SearchHit hit : searchResponse.getHits().getHits()) {
                        log.info(hit.getSourceAsString());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                }
            });
}
```

备注：其他API参阅template类，并且所有API都支持同步和异步操作。