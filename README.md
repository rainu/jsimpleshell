jsimpleshell
============

## 1) What is JSimpleShell?

JSimpleShell is a java library that allows you to implement easily a shell environment. This shell can be used such like the bash shell in linux systems: You can execute commands; jump through the history; navigate with the arrow-keys; and many more... And the best: it is system independent! That means you can use this functions also on windows systems as well as linux systems.

This is the perfect solution if you dont't have much time or no desire to develop a beautiful grafical user interface. You can very simple define a command - just one annotation is enough!

### 1.1) License

Of curse JSimpleShell is open-source! This library is distributed under the [BSD License (3-Clause)](http://opensource.org/licenses/BSD-3-Clause).

## 2) Integration

### 2.1) Maven

JSimpleShell is deployed on [maven-central](http://search.maven.org/#search|ga|1|jsimpleshell). That means that you can integrate it easily into your maven project. Just one dependency is enough:

```xml
<dependency>
	<groupId>de.raysha.lib</groupId>
	<artifactId>jsimpleshell</artifactId>
	<version>2.2</version>
</dependency>
```

### 2.2) Shell-Builder

Let us starts with a plain shell. To create a shell instance you have to use the _ShellBuilder_. With this builder you can build a _main shell_ or a _sub shell_. The following code starts an plain shell. This plain shell contains all default commands such like the command for listing all available commands.

```java
import java.io.IOException;

import de.raysha.lib.jsimpleshell.Shell;
import de.raysha.lib.jsimpleshell.ShellBuilder;

public class Example {

	public static void main(String[] args) throws IOException {
		Shell shell = ShellBuilder.shell("MyShell").build();
		shell.commandLoop();
	}
}
```

With the _ShellBuilder_ you can configure your shell. You can add handlers (see Handler section); define the style of prompt and many more. Look at the _ShellBuilder_ functions for more useful functions.

The _commandLoop_ method starts the input loop. This loop will be leave if the user leave the current shell. By default it happens if the user enter _exit_. 

### 2.3) Handler

There are two general types of handler:
* main handler
* aux handler

The _main handler_ are __only__ in the current shell available! If this shell starts a sub shell, this main handler is not available in there! However an _aux handler_ will be __shared__ with all following sub shells. If a shell starts a sub shell, this aux handler will be available!

For example:
If you want to implement a command that is everywhere available you should use a _aux handler_ which contains this command. 

#### 2.3.1) Command(-Handler)

#### 2.3.2) Access-Manager

#### 2.3.3) Command execution hooks

#### 2.3.4) Message-Resolver

#### 2.3.5) Candidates-Chooser

#### 2.3.6) Converter

### 2.4) Dependency injection

### 2.5) Input and Output

## 3) Usage

### 3.1) Help

### 3.2) Parameter

### 3.3) Auto completion

### 3.4) Sub-Shells

### 3.5) Variables

### 3.6) Macros

### 3.7) Language
