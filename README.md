# spring-elasticsearch-client
基于ES的elasticsearch-rest-high-level-client 7.0.0与spring整合客户端。

## 使用说明
clone项目并install到本地库(暂未发布到center仓库)。

补充：如需使用中文检索，请在Elasticsearch中安装ik中文分词插件，注意对应版本。
ik安装：

```
$ ./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.0.0/elasticsearch-analysis-ik-7.0.0.zip
```

### 依赖

~~~xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.1.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.github.baiczsy</groupId>
    <artifactId>spring-elasticsearch-client</artifactId>
    <version>1.0.0</version>
</dependency>
~~~

### 配置

~~~java
@Configuration
public class RestClientConfigure {

    /**
     * 配置RestClient连接池，基于commons-pool2
     */
    @Bean
    public RestClientPoolConfig poolConfig(){
        RestClientPoolConfig poolConfig = new RestClientPoolConfig();
        poolConfig.setMinIdle(5);
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxWaitMillis(2000);
        //other...
        return poolConfig;
    }
    
    /**
     * 装配RestClient配置类(单机)
     */
    @Bean
    public RestClientConfiguration restClientConfiguration(){
        RestClientStandaloneConfiguration configuration = new RestClientStandaloneConfiguration("localhost",9200);
        configuration.setConnectTimeout(1000);
        configuration.setConnectionRequestTimeout(500);
        configuration.setSocketTimeout(20000);
        configuration.setMaxConnTotal(100);
        configuration.setMaxConnPerRoute(100);
        return configuration;
    }

    /**
     * 装配RestClient配置类(集群)
     */
    /*@Bean
    public RestClientConfiguration clusterClientConfiguration(){
        List<ElasticsearchNode> hosts = new ArrayList<>();
        hosts.add(new ElasticsearchNode("localhost", 9200));
        hosts.add(new ElasticsearchNode("localhost", 9201));
        RestClientClusterConfiguration configuration = new RestClientClusterConfiguration(hosts);
        configuration.setConnectTimeout(1000);
        configuration.setConnectionRequestTimeout(500);
        configuration.setSocketTimeout(20000);
        configuration.setMaxConnTotal(100);
        configuration.setMaxConnPerRoute(100);
        return configuration;
    }*/

    /**
     * 装配ElasticsearchClientFactory
     */
    @Bean
    public ElasticsearchClientFactory elasticsearchClientFactory(RestClientConfiguration configuration, RestClientPoolConfig poolConfig){
        ElasticsearchClientFactory factory = new ElasticsearchClientFactory(configuration, poolConfig);
        //如果需要，可以设置默认的请求头信息
        //Map<String, String> headers = new HashMap<>();
        //headers.put("key", "value");
        //factory.setDefaultHeaders(headers);
        return factory;
    }
    
    /**
     * 装配RestClientTemplate
     */
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

### 创建索引及doc映射

```java
//构建mapping
Map<String, Object> name = new HashMap<>();
name.put("type", "text");
name.put("analyzer", "ik_max_word");

Map<String, Object> age = new HashMap<>();
age.put("type", "integer");

Map<String, Object> address = new HashMap<>();
address.put("type", "text");
address.put("analyzer", "ik_max_word");

Map<String, Object> properties = new HashMap<>();
properties.put("name", name);
properties.put("age", age);
properties.put("address", address);

Map<String, Object> mapping = new HashMap<>();
mapping.put("properties", properties);

//users为index名称
CreateIndexRequest request = new CreateIndexRequest("users").mapping(mapping);
template.opsForIndices().create(request);
```

### 删除索引

~~~java
DeleteIndexRequest request = new DeleteIndexRequest("users");
template.opsForIndices().delete(request);
~~~

### 添加数据

```java
//用户数据
Map<String, Object> map = new HashMap<>();
map.put("name", "zing");
map.put("age", 26);
map.put("address","global village");

IndexRequest request = new IndexRequest("users").id("1").source(map);
template.persist(request);
```

### 根据id查找数据

~~~java
GetRequest request = new GetRequest("users").id("1");
Map<String, Object> map = template.get(request);
~~~

### 更新数据

```java
//用户数据
Map<String, Object> map = new HashMap<>();
map.put("name", "bobo");
map.put("age", 28);

UpdateRequest request = new UpdateRequest("users", "1").doc(map);
template.update(request);
```

### 删除数据

```java
DeleteRequest request = new DeleteRequest("users").id("1");
template.delete(request);
```

### 检索数据

```java
//fields为需要检索的字段名(如：name，address)，param为查询条件
public List<Map<String, Object>> search(String index, String param, String[] fields) {
    return template.opsForQuery().multiMatch(index, param, fields);
}
```

### 分页检索

~~~java
template.opsForQuery()
            .from(0)
            .size(10)
            .multiMatch(index, param, fields);
~~~

### 异步操作

```java
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
```

### 使用execute方法操作RestHighLevelClient

```java
//例如添加一条记录
template.execute((highLevelClient) -> {
    IndexRequest request = new IndexRequest(index).id(id).source(map);
    request.timeout(TimeValue.timeValueNanos(timeout));
    highLevelClient.index(request, RequestOptions.DEFAULT);
});
```
说明：其他API参阅RestClienTemplate类，且所有API都支持同步和异步操作。

## 整合Spring boot
https://github.com/baiczsy/elasticsearch-client-spring-boot-starter

