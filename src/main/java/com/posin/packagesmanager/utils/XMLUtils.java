package com.posin.packagesmanager.utils;

import android.content.ComponentName;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * FileName: XMLUtils
 * Author: Greetty
 * Time: 2018/10/12 13:08
 * Desc: TODO
 */
public class XMLUtils {


    public static List<ComponentName> readXML(InputStream inStream) {

        List<ComponentName> result = new ArrayList<ComponentName>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(inStream);

            Element root = dom.getDocumentElement();

            NodeList items = root.getElementsByTagName("pkg");//查找所有person节点

            for (int i = 0; i < items.getLength(); i++) {
                //得到第一个person节点
                Element pkgNode = (Element) items.item(i);

                final String pkgName = pkgNode.getAttribute("name");

                List<String> disabled = xmlGetDisabledComponents(pkgNode);
                for (String s : disabled) {
                    result.add(new ComponentName(pkgName, s));
                }
            }

            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static List<String> xmlGetDisabledComponents(Element pkgNode) {
        List<String> result = new ArrayList<String>();
        NodeList childNodes = pkgNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i); //判断是否为元素类型
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element childNode = (Element) node;

            if (!"disabled-components".equals(childNode.getNodeName()))
                continue;

            NodeList itemNodes = childNode.getChildNodes();
            for (int j = 0; j < itemNodes.getLength(); j++) {
                Node n = itemNodes.item(j);
                //Element e = (Element)itemNodes.item(j);
                //if(!"item".equals(node.getNodeName()))
                if (!"item".equals(n.getNodeName()))
                    continue;
                String name = ((Element) n).getAttribute("name");
                result.add(name);
            }
        }
        return result;
    }

}
