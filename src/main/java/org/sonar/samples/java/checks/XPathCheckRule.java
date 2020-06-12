/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.samples.java.checks;

import javax.xml.xpath.XPathExpression;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.sample.model.SimpleXPathBasedCheck;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Rule(   key = "XPathCheck",
name = "Checking the Scope of spring beans",
description = "BSL is not allowing to create spring beans without scope Property",
priority = Priority.BLOCKER,
tags = {"bug"})
public class XPathCheckRule extends SimpleXPathBasedCheck {

  private XPathExpression singleConnectionFactoryBeansExpression = getXPathExpression("beans/bean[@class='org.springframework.jms.connection.SingleConnectionFactory']");
  private XPathExpression reconnectOnExceptionPropertyValueExpression = getXPathExpression("property[@name='reconnectOnException' and value='true']");
  private XPathExpression scopeOfTheBeansExpression = getXPathExpression("beans/bean");

  @Override
  public void scanFile(XmlFile file) {
	  
	  System.out.println(" Scanning the XML file :");
	  
    evaluateAsList(singleConnectionFactoryBeansExpression, file.getNamespaceUnawareDocument()).forEach(bean -> {
      if (!hasPropertyAsAttribute(bean) && !hasPropertyAsChild(bean)) {
        reportIssue(bean, "Add a \"reconnectOnException\" property, set to \"true\"");
      }
    });
    
    
    evaluateAsList(scopeOfTheBeansExpression, file.getNamespaceUnawareDocument()).forEach(bean -> {
       
          reportIssue(bean, "This is bean node tested  ");
      
      });
}

  private static boolean hasPropertyScopeAttribute(Node bean) {
	    Node attribute = XmlFile.nodeAttribute(bean, "scope");
	    return attribute != null && "true".equals(attribute.getNodeValue());
	  }
  
  private static boolean hasPropertyAsAttribute(Node bean) {
    Node attribute = XmlFile.nodeAttribute(bean, "p:reconnectOnException");
    return attribute != null && "true".equals(attribute.getNodeValue());
  }

  private boolean hasPropertyAsChild(Node bean) {
    NodeList nodeList = evaluate(reconnectOnExceptionPropertyValueExpression, bean);
    return nodeList != null && nodeList.getLength() != 0;
  }

}