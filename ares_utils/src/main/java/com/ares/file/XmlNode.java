package com.ares.file;

import org.dom4j.Element;

import java.util.List;

public class XmlNode {
    //当前节点
    private Element currentNode;
    //当前节点的子节点集合
    private List<XmlNode> childNode;

    public Element getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Element currentNode) {
        this.currentNode = currentNode;
    }

    public List<XmlNode> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<XmlNode> childNode) {
        this.childNode = childNode;
    }

}
