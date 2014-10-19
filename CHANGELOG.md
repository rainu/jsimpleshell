## [JSimpleShell x.x - XXXX-XX-XX][x.x]
[x.x]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/x.x/

* [IMPORTANT/feature] If the shell is in record mode a special variable will be stored into the environment. If you wants to know whether you are currently in the record mode, you can use the _CommandRecorder.isShellInRecordMode()_ -Method.

## [JSimpleShell 3.0 - 2014-10-19][3.0]
[3.0]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/3.0/

* [feature] New command for removeing a variable from the current environment.
* [feature] There are new script commands (_echo*_) for building a string that will be displayed. For example this can be helpful for print the current state of a long durateing loop.
* [feature] A new handler type is added: _CandidateFilter_. This handler can decide if an auto complete candidate should be offered.
* [change] Commands with a prefix (e.g. "?") will not auto complete by default. Only if the user entered the prefix before, the candidates will be shown.
* [IMPORTANT/feature] Define a version-policy.
* [feature] The user can now use loops!
   * There is a counting loop ...
   * ... and a powerful foreach loop.
* [IMPORTANT/change] All commands that (can) starts a subshell should configure it in her @Command annotation! This information is needed by the _CommandRecorder_ and thus by the loops!
* [feature] Each shell has now a "command-pipeline". This pipeline contains the next command-lines that should be execute before the user can enter the next command.
* [feature] You can use the _CommandRecorder_ to let it record commands that typed by the user. 
   * This commands will not execute on user typeing!
   * A new AccessDecision is added: MUTE - means that the user can execute the command but for the moment the command will not be executed.
* [feature] There is a new handler type for observing executing command lines. 
* [fix] Solve memory leak! If a shell was leafed she will be successfuly garbage collected.
* [feature] Add a new optional command for execute and controll external processes. By default this command is disabled! You can enable it in the ShellBuilders behavior.
* [feature] You can define if a command result (return value of the method) should be displayed.
* [change/fix] The ShellBuilder has been changed:
   * Move it to a new sub-package.
   * Split the methods into logically parts. For example: If you want to add a new handler you must invoke the _behavior_ method before.
   * Adds methods for setting flags. For example: enableColor() can also be setted with setColor(true).
* [feature] The user can now enter multiple commands in one single line.
    * Use double ampersand (&) to concat two command as AND. That means that the second command will only be executed if the first command was successfuly executed.
    * Use the semicolon (;) to concat two commands as OR. That means that the second command will execute even the first command was failed.

## [JSimpleShell 2.2 - 2014-10-03][2.2]
[2.2]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/2.2/

* [feature] The user can now define variables! 
   * These variables can be _local_ or _global_. 
   * Variables can be passed as parameter for a command.
   * Each command execution will set special variables that contains the result of the command.
* [feature] There is now a class (_DefaultTypes_) that contains all default parameter-types for auto completion.
* [feature] Now you can decide at the runtime if a user have access to execute a command. Just implements the _CommandAccessManager_ interface.
    * If an user have no access to a command, this command will be not auto completed and it will be not shown in the list of the "list" command.
* [fix] If the locale was changed at the runtime the command table will be refreshed. That solved the problem that after changeing the locale the command names are still in the previous locale.
* [fix] All message resolver will be configured before initialise new commands. That cause that the message resolvers are able to change the command name, description and so on.
* [feature] add travis file to build the project via travis - for more quality :)

## [JSimpleShell 2.1 - 2014-08-23][2.1]
[2.1]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/2.1/

* [feature/change] Add localisation support:
    * The locale can now be changed at the runtime (via command).
    * The _Locale_ type can now be autocompleted by default.
    * Add translation into german.
    * The MessageResolver get two more methods for localisation support.
* [feature] The user is now able to define the command parameter order for his own by typing two minus (--) follow by the parameter name. 
  * This parameter names can also be auto completed (if the user typed the two minus before)
* [feature] Now all dependencies can be injected (but the dependend-interfaces work too). You can use the jss-own injection annotation (_@Inject_), the java-ee innjection annotation (_javax.inject.Inject_) or the spring-own annotation (_@Autowired_).
* [fix] If a command have a primitive boolean as parameter, the auto completer will complete the values too.
* [feature] Scripts/Macros can now include parameters. 
  * Each parameter has a name and is surrounded by braces ({}). 
  * On starting your script/macro you can enter a list of key-value pairs for the parameters.
  * A now command was created for display all parameters that contains in a script/macro. 
* [feature] implements a lot of integration tests - for more quality :)

## [JSimpleShell 2.0 - 2014-08-05][2.0]
[2.0]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/2.0/

* [fix] If a command has only varargs as paramater the user can enter also no arguments.
* [feature] Enum parameters can be auto completed.
* [feature] Enum parameters can be converted by default.
* [change] The _param_ annotation has been changed.
* [feature] If a command expect a boolean value the auto completion can complete boolean values (true/false)
* [feature] You can define your own paramter type, that can be used for your own _CandidatesChooser_ to auto complete the command parameters (such like the auto completion of file pathes)
  * There is a type available for auto complete only directory pathes.
* [change] The escaping of shell-own characters (e.g. the quote) has bean changed. Now you have to use the backslash (\\) to escape these characters.
* [change] All completer was moved into a seperate package.
* [feature] The prompt is now an object. You can implement the _PromptElement_ interface. Now you are able to implement a dynamicaly prompt! For example you can change the prompt color after each command :)
* [feature] The _ColoredStringBuilder_ can be used for building an colored output string.
* [feature] You can disable/enable color output programmatically. Even if you print color via the _OutputBuilder_ the text will be blank if you have disable the color output!
* [feature] Add a macro recorder command. That can be usefull if a user will record commands that he often used. 
  * A macro have a name.
  * All macros will be stored into a own macro directory (can be configured by the user itself)
* [feature] A new message resolving mechanism was implemented. 
  * Now you can rename all command names, descriptions, etc. 
  * All messages are resolvable. A message resolver get an message-key and provide the coresponding messages.
  * You can implement your own MessageResolver!
* [change] The hook-interfaces are moved to new subpackage.
* [feature] BigDecimal and BigInteger can now converted from string input by default.
* [feature] The "exit" command can be disabled.
* [feature] The handling of the user interrupt (CTRL-C) can now be configured by the ShellBuilder.
* [fix] The history will be flushed after each shell exit.
* [feature] The command-history file can now be configured by the ShellBuilder.
* [feature] The application name is now set to null by default. So the application name will not be displayed on startup.
* [feature] Add AUX-Handler via the ShellBuilder
* [feature] Each command can now cause a shell to exit - just throw an ExitException

## [JSimpleShell 1.0 - 2014-07-21][1.0] 
[1.0]: https://oss.sonatype.org/content/groups/public/de/raysha/lib/jsimpleshell/1.0/

* Initialise the project
