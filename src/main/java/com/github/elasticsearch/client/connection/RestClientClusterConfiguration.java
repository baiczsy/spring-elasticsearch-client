package com.github.elasticsearch.client.connection;

import org.apache.http.HttpHost;
import com.github.elasticsearch.client.core.ElasticsearchNode;

import java.util.List;

/**
 * @author wangl
 * @date 2019-04-30
 */
public class RestClientClusterConfiguration extends RestClientConfiguration{

    private List<ElasticsearchNode> clusterNodes;

    public RestClientClusterConfiguration(){

    }

    public RestClientClusterConfiguration(List<ElasticsearchNode> clusterNodes){
        this.clusterNodes = clusterNodes;
    }

    public void setHosts(List<ElasticsearchNode> clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    @Override
    public HttpHost[] getHttpHosts(){
        HttpHost[] hostArray = new HttpHost[clusterNodes.size()];
        for (int i=0; i<clusterNodes.size(); i++) {
            hostArray[i] = new HttpHost(clusterNodes.get(i).getHost(), clusterNodes.get(i).getPort(), "http");
        }
        return hostArray;
    }

}
