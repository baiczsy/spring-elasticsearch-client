package com.github.elasticsearch.client.core;

/**
 * @author wangl
 * @date 2019/9/2
 */
public enum HighlightType {
    /**
     * 荧光笔
     */
    UNIFIED("unified");

    private String type;

    HighlightType(String type){
        this.type = type;
    }

    public String getValue(){
        return type;
    }
}
