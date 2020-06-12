package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class AvoidClassInstanceObjectTest {

	@Test
	  public void detected() {
	    // Verifies that the check will raise the adequate issues with the expected message.
	    // In the test file, lines which should raise an issue have been commented out
	    // by using the following syntax: "// Noncompliant {{EXPECTED_MESSAGE}}"
	    JavaCheckVerifier.newVerifier()
	      .onFile("src/test/files/AvoidClassInstanceObjectCheck.java")
	      .withCheck(new AvoidClassInstanceObjectRule())
	      .verifyIssues();
	  }
	
}
