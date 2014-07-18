JSimpleShell
============

If you dont want to spent much time to design and implement your own user interface you've come to the right place! This project is a lightweight and simple-to-use library, with wich one you can create your own shell (as a command line interface) in java! It based on [cliche](https://code.google.com/p/cliche/) but use [jline2](https://github.com/jline/jline2/) to improve the shell functionality.

Demo version
------------

You can build the demo project (runnable jar file) for your own to checkout the functionality of ___JSimpleShell___. The only thing what you have to do is checkout and build the project (see below). After that you can run the jar-file which is created under the exaples/target folder.

    java -jar example/target/jsimpleshell-example-1.0-SNAPSHOT-jar-with-dependencies.jar

License
-------

JSimpleShell is distributed under the [BSD License (3-Clause)](http://opensource.org/licenses/BSD-3-Clause).

Maven Usage
--------

__Attention__: At the moment there are no artifact deployed in a official maven-repo! You must build it for yourself.

If you want to add ___JSimpleShell___ to your maven project, you can add the following dependency in your __pom.xml__:

    <dependency>
      <groupId>de.rainu.lib</groupId>
      <artifactId>jsimpleshell</artifactId>
      <version>1.0-SNAPSHOT</version>
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
