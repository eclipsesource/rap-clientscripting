Differences of experimental branch to eclipse incubator master:

* None of the changes June 2006 (including Paint support)
* widget proxy has it's protocol id in user-data (not API)
* Allow a Map<String, Object> as a optional parameter for ClientListener (previously EventBinding)
  * Map may (currently) contain instances of Widget, Integer, String or JavaFunction (part of CS)
  * Values of map are put into into local scope of client function (like "var = xxx;")
  * Actual local variables declared in javascript code are not overwritten
  * Was previousy attached to "this" object of client function. (That's why it's still called "context")
  * JavaFunction currently not supporting parameters or return value
* Single sourcing helper "Listener.java" introduced
  * Uses reflection to turn a POJO into Map<String, Object>, only private fields and methods allowed
  * Listener must implement "getClientImpl" and should implement "handleEvent" (though it is ignored in RAP)
  * SWT version in bundle "org.eclipse.rap.clientscripting.swt"
  * NOT implemented: Server-Client sync for fields