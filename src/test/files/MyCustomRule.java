class A {

  int foo() {}
  int foo(int a) {} // Noncompliant {{Dont use same datatype as return type}}
  int foo(int a, int b) {}

  Object foo(Object a){} // Noncompliant {{message}}
  String foo(String a){} // Noncompliant {{message}}
  String foo(Object a){}
}