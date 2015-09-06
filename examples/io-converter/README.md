Input/Output Converter
============

User can only type string values. If you want to convert these string input to a custom type, you have to define a own _InputConverter_.
If your command return a non string value, the shell will print it out with the help of the toString()-Method of the returned object. But if you
want to print out an other value, you have to implement a own _OutputConverter_.