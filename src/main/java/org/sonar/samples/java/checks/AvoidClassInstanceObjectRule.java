package org.sonar.samples.java.checks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Properties;

import org.sonar.check.Priority;
import org.sonar.check.Rule;

import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Modifier;
import org.sonar.plugins.java.api.tree.ModifiersTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.sample.model.ModifiersUtils;

@Rule(   key = "AvoidClassInstanceObject",
name = "Variables should not be instantiated in class level",
description = "Global variables are prohibitted in the BSL applications.",
priority = Priority.BLOCKER,
tags = {"bug"})
public class AvoidClassInstanceObjectRule extends BaseTreeVisitor implements JavaFileScanner {

	private Deque<Boolean> isClassStack = new ArrayDeque<>();
	private Deque<Boolean> isVarStack = new ArrayDeque<>();
	//"C:/Program Files/Java/jdk-11.0.7/bin/java"
	private static final String propFileLocation="C:/Applications/sonar_custom_java.properties";
	
	
	 
	  private JavaFileScannerContext context;
	 

	/*
	 * public static void main(String args[]) {
	 * System.out.println("Is white listed variables :"+new
	 * AvoidClassInstanceObjectRule().isWhiteListVariable("myName")); }
	 */
	  
	 
	  
	  public String getValFromProp(String prop)
	  {
		  String propVal="";
		  try {
		  FileReader reader;
		  reader = new FileReader(propFileLocation);
		  Properties p=new Properties();  
		    p.load(reader); 
		    propVal= p.getProperty(prop);
		    
		} catch (FileNotFoundException e) {
			System.out.println("Exception ");
			e.printStackTrace();
		}  
		  catch (IOException e) {
				e.printStackTrace();
			}
		  catch (Exception e) {
				e.printStackTrace();
			} 
		  
		    return propVal;
	  }
	  
	  public boolean isWhiteListVariable(String myDesiredVariable)
	  {
		  boolean isWhiteListed= false;
		  List<String> varList= new ArrayList<String>();
		  
		  String[] myVars=getValFromProp("whitelist.variables").split(",");
		  
		 // String[] myVars=getPropertyValue("whitelist.variables").split(",");
		  
		  varList = Arrays.asList(myVars);
		  
		  if(varList.contains(myDesiredVariable))
		  {
			  isWhiteListed=true;
		  }
		  
		  
		  return isWhiteListed;
	  }
	  
	  
	
	  
	  @Override
	  public void scanFile(JavaFileScannerContext context) {
	    this.context = context;
	    scan(context.getTree());
	  }
	 
	  @Override
	  public void visitClass(ClassTree tree) {
		isVarStack.push( tree.is(Tree.Kind.INSTANCE_OF) || tree.is(Tree.Kind.VARIABLE));
	    isClassStack.push(tree.is(Tree.Kind.CLASS) || tree.is(Tree.Kind.ENUM));
	    super.visitClass(tree);
	    isClassStack.pop();
	  }
	 
	  @Override
	  public void visitVariable(VariableTree tree) {
	    ModifiersTree modifiers = tree.modifiers();
	    List<AnnotationTree> annotations = modifiers.annotations();
	 
		
		  if (isClass() && tree.parent().is(Tree.Kind.CLASS) && !isWhiteListVariable(tree.simpleName().name()) && !(isFinal(modifiers) || !annotations.isEmpty()))
		  { 
			  context.reportIssue(this, tree.simpleName(), "Restricted to define " + tree.simpleName() +  "  as Global variable."+
		               " Parent Kind :"+tree.parent().kind() +", Class Variable :"+tree.parent().is(Tree.Kind.CLASS)
			  + ", White Listed :"+ isWhiteListVariable(tree.simpleName().name())); 
		  }
	    super.visitVariable(tree);
	  }
	 
	  private boolean isClass() {
	    return !isClassStack.isEmpty() && isClassStack.peek();
	  }
	
	  private static boolean isFinal(ModifiersTree modifiers) {
	    return ModifiersUtils.hasModifier(modifiers, Modifier.FINAL);
	  }
	 
	  private static boolean isPublic(ModifiersTree modifiers) {
	    return ModifiersUtils.hasModifier(modifiers, Modifier.PUBLIC);
	  }
	
}
