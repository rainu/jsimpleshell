Hooks
============

The JSS-Framework provides some hooks. In this example project you can see what kind of hooks are available.

| Method        | Interface           | Description  |
--- | --- | ---|
| cliBeforeCommandLine | CommandLoopObserver | This method will be called before the command line will be proceed. |
| cliAfterCommandLine | CommandLoopObserver | This method will be called after the command line was proceed. |
| cliBeforeCommand | CommandHookDependent | This method will be called before a command will be executed. |
| cliAfterCommand | CommandHookDependent | This method will be called after a command was executed. |
| cliDeniedCommand | CommandHookDependent | This method will be called if a execution of command was denied. |
| cliEnterLoop | ShellManageable | This method is called when it is about to enter the command loop. |
| cliLeaveLoop | ShellManageable | This method is called when Shell is leaving the command loop. |
