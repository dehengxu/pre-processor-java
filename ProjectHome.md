## Awards ##
![http://s1.softpedia-static.com/base_img/softpedia_free_award_f.gif](http://s1.softpedia-static.com/base_img/softpedia_free_award_f.gif)

Preprocessor-Java 0.1 - 100% Free
more is here http://mac.softpedia.com/get/Developer-Tools/Preprocessor-Java.shtml

## Welcome ##
Welcome to the **Java Preprocessor** project!
This project is a **pre-processor** for **multi program languages** based on **java**.

## Features ##
Firstly, It can run with：
  * Command line
  * GUI
  * ant task

Moreover, basically, It can do a **preprocess** work for any text files,  such as code and plaint text, whatever. of course, it is more valuable for processing the code files.

And, it supports boolean, integer, float and string data types. and support **syntax check** as well.

## The specification of the files it supports: ##
The file should have a character or a string for a single line comment.
such as "//" for java, "#" for linux shell script.
## What it can do ##
Simple example of a **preprocess** statement
```
	 //	#define BOOL_VALUE  True   
         //	#define INT_VALUE  123   
         //	#ifdef BOOL_VALUE	
                something here when BOOL_VALUE is TRUE
                //	#ifdef INT_VALUE == 123 
			INT_ VALUE is 
           		// #<< INT_VALUE 
		//	#else	
			INT_VALUE is NOT 123
		//	#endif	
	//	#else	
		BOOL_VALUE is NOT TRUE
	//	#endif	
```

After **preprocessing**, we can get the code below:
```
                something here with BOOL_VALUE is TRUE
			INT_ VALUE is 
           		123
```
## How to run ##
  1. Command Line:
```
java -jar javapc.jar -s src -d dest [-e false|true] [-i filename] [-m comment]
```
    * -s   Source file or directory.
    * -d   Destination file or directory.
    * -e   If true, export a code version with the parameters you set. Or just comment the useless code. The default is false. **Setting export as false is easy to debug your code, because the line number of code file will not be changed after preprocessing.**
    * -i   Define a initial file, this file will be loaded firstly. The default name of init file is "global.def". You can define some global variables in this file.
    * -m Define yourself mark for comment. The default is "//".
  1. GUI
```
java -jar javapc.jar -g
```
> > ![https://lh6.googleusercontent.com/_9xn0C3oWk3c/TWoKZq1RJOI/AAAAAAAAASE/drTpcxTQGLs/JavaPC_GUI.png](https://lh6.googleusercontent.com/_9xn0C3oWk3c/TWoKZq1RJOI/AAAAAAAAASE/drTpcxTQGLs/JavaPC_GUI.png)
  1. ant task
A template file:
```

<?xml version="1.0" encoding="UTF-8"?>
<project name="Preprocess ant task" basedir="." default="precompile">

	<property name="src.dir" value="src" />
	<property name="result.dir" value="result" />

	<target name="clean" description="Delete all generated files">
		<delete dir="${result.dir}" failonerror="false" />
	</target>

	<target name="init">
		<!-- create directories -->
		<mkdir dir="${result.dir}" />
	</target>

	<target name="precompile" description="Pre compile the Task" depends="clean,init">
		<taskdef name="javapc" classname="au.songdi.javapc.ant.JavaPCTask" classpath="javapc.jar" />
		<javapc srcdir="${src.dir}" destdir="${result.dir}" export="false" initfile="global.def" commentmark="//" />
                <!-- same with <javapc srcdir="${src.dir}" destdir="${result.dir}" />-->
	</target>
</project>

```

## More detail in QuickStart Manual ##
http://pre-processor-java.googlecode.com/files/JavaPC_QuickStart_0.1.pdf

## Brother project pypc ##
**pypc is a pre-processor written by python**
https://github.com/songdi/pypc--pre-processor-python--