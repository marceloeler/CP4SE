# CP4SE
Constraing Profiling for Symbolic Execution

This is a tool that has been developed to analyzed the characteristics of Java methods that may impact test data 
generation from a symbolic execution perspective. These are the metrics collect by the tool (among others):
- cyclomatic complexity
- number of loops and inner loops
- CFG structure
- types of comparisons (predicates)
- exception dependent paths
- non-linear expressions
- complexity of the constraints considering their data types and type of elements (variable, method call, etc)

This tool was only a prototype devised for simple purposes but it has became bigger along the time, therefore it needs 
serious refactoring in order to become easier to understand and evolve. At some point I intend to build a new version
of this tool - at least the CFG builder and the metric extraction. I should also include the test cases.

This tool was used to extract the metrics used in these papers:

ELER, M. M. ; DURELLI, VINICIUS H.S. ; ENDO, ANDRE T. . An Empirical Study to Quantify the Characteristics of Java 
Programs that May Influence Symbolic Execution from a Unit Testing Perspective. 
In: The Journal of Systems and Software, 2016 (in press).

ELER, M. M. ; DURELLI, VINICIUS H.S. ; ENDO, ANDRE T. . Analyzing Exceptions in the Context of Test Data Generation 
Based on Symbolic Execution. In: The 27th International Conference on Software Engineering and Knowledge Engineering, 
2015, Pittsburgh - EUA.

ELER, M. M. ; ENDO, ANDRE T. ; DURELLI, VINICIUS H.S. Quantifying the Characteristics of Java Programs That May Influence 
Symbolic Execution from a Test Data Generation Perspective. In: 2014 IEEE 38th Annual Computer Software and Applications 
Conference (COMPSAC), 2014, Västeras - Sweeden. 
