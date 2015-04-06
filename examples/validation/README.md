Validation
============

Any command have a weak validation - the user must offer the right input type. For example an integer. 
If the user type a string (e.g. "Hello World"), he gets an input error (if no any other command matches the input pattern).

But for example: if you want that the user can only type an integer between 1 and 100, you have to use the "advanced" validation.
You can use:
* the _javax-annotations_ (aka. JSR-303, aka. BeanValidation)
* or your _own validation_ mechanism (just implements an interface)

Note: If you want to use the JSR-303 Annotations, you must have a __validation provider__ into your classpath (for example the Hibernate).