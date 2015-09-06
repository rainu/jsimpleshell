Command-Pipeline
============

You have two ways to execute commands programmaticaly: _processLine()_ and the _pipeline()_. The difference about
these two ways are the following:

* _processLine()_ will execute the command in the shell instance where you call the processLine.
This command will be invoke directly.
* _pipeline()_ will share its commands with all subshells which can be coming.
This commands will not be invoke directly.

For example:

shell/ $> start-subshell
shell/sub $> do-something
shell/sub $> exit
shell $> exit

If you use _processLine()_ like this:
```java
shell.processLine("start-subshell");
shell.processLine("do-something");
shell.processLine("exit");
shell.processLine("exit");
```

You will get this:
shell/ $> start-subshell
shell/sub $> .... wait for user input ...
shell $> do-something
shell $> exit
shell $> exit

And if you use _pipeline()_ like this:
```java
shell.getPipeline().append("start-subshell");
shell.getPipeline().append("do-something");
shell.getPipeline().append("exit");
shell.getPipeline().append("exit");
shell.commandLoop();
```

You will get this:
shell/ $> start-subshell
shell/sub $> do-something
shell/sub $> exit
shell $> exit
