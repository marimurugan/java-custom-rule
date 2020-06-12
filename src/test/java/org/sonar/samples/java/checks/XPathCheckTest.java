package org.sonar.samples.java.checks;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonarsource.analyzer.commons.xml.checks.SonarXmlCheckVerifier;


public class XPathCheckTest {

  @Rule
  public LogTester logTester = new LogTester();

  @Test
  public void test_nodes() throws Exception {
    SonarXmlCheckVerifier.verifyIssues("simple.xml", getCheck("//b"));
  }

  @Test
  public void test_file() throws Exception {
    SonarXmlCheckVerifier.verifyIssueOnFile("simple.xml", getCheck("boolean(a)"), "XPath issue message");
    SonarXmlCheckVerifier.verifyNoIssue("simple.xml", getCheck("not(boolean(a))"));
  }

  @Test(expected = IllegalStateException.class)
  public void test_invalid_xpath() throws Exception {
    SonarXmlCheckVerifier.verifyNoIssue("simple.xml", getCheck("boolean(a"));
  }

   private static XPathCheckRule getCheck(String expression) {
	   XPathCheckRule check = new XPathCheckRule();
	   
    return check;
  }

}
