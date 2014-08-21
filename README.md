JSimpleShell
============

If you dont want to spent much time to design and implement your own user interface you've come to the right place! This project is a lightweight and simple-to-use library, with wich one you can create your own shell (as a command line interface) in java! It based on [cliche](https://code.google.com/p/cliche/) but use [jline2](https://github.com/jline/jline2/) to improve the shell functionality.

Feature-List
------------
* Implement a command via annotation
* Show help-text for each command
* Colored output
* Persistable command history
* Searching through the command history
* Arrow-Key navigation
* Sub-Shells
* Scriptable (execute command line by line)
* Clear screen function
* Platform independent
* Maven integration (artifacts are stored in maven central)
* Masking input (helpful for password inputs)
* Param type dependent autocompletion (Commands and File-Pathes are available by default)
* All messages can be resolved (useful for l18n)
* Recording macros

For possible future changes see [here](https://github.com/rainu/jsimpleshell/labels/enhancement)

How it works
------------

Do you want to see some code? No problem! Look  [here](https://github.com/rainu/jsimpleshell/blob/master/example/src/main/java/de/raysha/lib/jsimpleshell/example)

Demo version
------------

You can build the demo project (runnable jar file) for your own to checkout the functionality of ___JSimpleShell___. The only thing what you have to do is checkout and build the project (see below). After that you can run the jar-file which is created under the exaples/target folder.

    java -jar example/target/JSimpleShell-Demo.jar 

License
-------

JSimpleShell is distributed under the [BSD License (3-Clause)](http://opensource.org/licenses/BSD-3-Clause).

Maven Usage
--------

If you want to add ___JSimpleShell___ to your maven project, you can add the following dependency in your __pom.xml__:

    <dependency>
      <groupId>de.raysha.lib</groupId>
      <artifactId>jsimpleshell</artifactId>
      <version>2.0</version>
    </dependency>
    
Building
--------

### Requirements

* Maven 3+
* Java 6+

Check out and build:

    git clone git://github.com/rainu/jsimpleshell
    cd jsimpleshell
    mvn clean install

Eclipse integration
--------------------

If you want to import the project into your eclipse, you must execute the following maven command:

    mvn eclipse:eclipse
    
Now you can import "An existing Project into your workspace".
