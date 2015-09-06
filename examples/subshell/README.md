SubShell
============

SubShells can be used for group commands. Another usecase can be a builder as an SubShell. For example: A shell-user wants
to create a user-object. Now you can implement a command such like "createUser". This command can start a SubShell where
the shell-user can build the user. After exiting the SubShell the "createUser" command can persist the built user-object.

SubShells can be also created by the _ShellBuilder_ (such like a "normal" shell). You need the parent shell to create a
subshell. All AUX-Handler from the parent shell will be shared with the subshell. But not the "normal" handler!

Commands which can start a SubShell _should be_ mark as "startsSubshell". This must be done in the @Command-Annotation.
If you don't mark this command, JSS can not record commands correctly. If JSS is in record mode, you may have to skip
some actions. For this reason you can ask if you be in record mode: _CommandRecorder.isShellInRecordMode(myShell)_
