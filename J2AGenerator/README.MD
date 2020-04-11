J2A Generator Documentation
===========================

The J2A ("Java 2 Any") generator creates any textual output based on the properties of an existing Java class. It allows to access source class elements accessible via reflection such as fields, methods, nested classes etc. However, that is not all. Thanks to using the Java Parser library as alternative input parser, J2A can also use and process any additional class elements that are not accessible via reflection such as generic type arguments, comments, Javadocs, import statements, annotations with retention "SOURCE" and the like.

This makes J2A a handy and very versatile generation tool during development, build and in a few cases possibly even for runtime use.

Motivation for J2A
------------------

J2A bundles solutions for a lot of use cases for text and code generation from Java classes - with full IDE and (if needed) full build integration. You do not need to write a single line of template code to use common features such as getter/setter generation. But if you want, you can write target templates that allow for very detailed custom target code generation - way more powerful than what all current IDEs can do.

If you want to know why J2A is indeed a good addition to your toolbox, read this section.

Now show me what it can do or stop bothering!
---------------------------------------------

To start with something simple, J2A can do what most IDEs in Java do for you: Generating "boiler plate" code.

TODO weitere Inhalte

Wait!? Why the hell would we need YACG (Yet Another Code Generator)
-------------------------------------------------------------------

I'd better state first that J2A is of course not a silver bullet, it is just another tool in your toolbox which we hope is helpful. Since long gone hype days of MDD (model-driven design) and MDA (model-driven architecture), code generation - especially from UML and the like - is not the most beloved thing in the Java and maybe overall software developer community.

Yet, there are still common daily uses for code generation: We have generation functionality built into our IDEs to create getters, setters, hashCode and equals and so on. We have the XJC or other tools converting XML or JSON to Java and vice versa. We have the Javadoc tool to create documentation from source code. We have templating engines that create HTML or other output. The list goes on and on. Yes, there are also projects actually successfully using MDD and MDA. So code generation is still everywhere, but for most developers, it is rather a tool during development and maybe build time.

For the Java haters: For sure one reason for this is that Java still sometimes requires boiler-plate code. Lombok might be an alternative for some, but it for sure cannot eliminate all use-cases. Code generators help here. BUT there are also other use cases of code generation:
* Generate documentation, e.g. a graph (say: UML) representation of the code
* Help to bridge technological gaps between data representations, e.g. for serialization and deserialization of transport formats; XML to Java or JSON to Java and vice versa, for instance
* And last but not least: Architectural reasons

Sometimes, our software architectures - especially for large applications - require some kind of duplication. Duplication that is actually helpful. Let's look at some examples:
* If we have Data Objects coming from e.g. a database, we might to hide their exact internal representation and framework dependencies from the outside world in terms of information hiding and low coupling. Sometimes, the so-called DTO (data transfer object) pattern can help here. In these situations, DTOs might look very similar to Data Objects and thus are viable targets for code generation.
* Interfaces between different applications or client and server are integration points that need to be on the same page. Often this means that they are in-sync in terms of the data structures used - Code generation is often helpful here

Let's look at an example architecture for a large Spring Boot application. It tries to achieve separation of concerns by separating business logic from database access code by applying the following template structure for each JPA entity that is persisted in the app's DB:

TODO Bild

TODO J2A code Beispiel

Well, isn't there already such a thing?
---------------------------------------

So someone has already created something like J2A and you are just reinventing the wheel, right? Not exactly. Currently, we just have isolated solutions for very specific use cases. We have Javadoc, XJC, we have generation tools for specific remote interfaces. We have built-in but quite limited code generation facilities in IDEs such as Eclipse, IntelliJ and so on. We have templating engines for frontend development. We have the heavily aged JET (Java Emitter Tools) that are tightly coupled to Eclipse and EMF. And for sure some companies created internal tools that might form a subset of J2A's features.

But believe it or not: There is no well-known and widespread library that can generate any text based on a Java class's properties. But fortunately - now the waiting is finally over - J2A is here!



Generation Approach
-------------------



Template Syntax
---------------

### Variable References

Variable references are basically used for parameterizing varying data in a template.

A variable reference is started with a dollar and then enclosed in braces, e.g. ${j2a:user} is a valid variable reference. It consists of an optional namespace qualifier (which can be any valid Java identifier) terminated by a colon ':' as well as a required variable name (any valid Java identifier). Both are case sensitive.

The EBNF of a variable reference thus looks like this:

VariableReference ::= '${'[NamespaceQualifier:]VariableName'}'
NamespaceQualifier ::= JavaIdentifier
VariableName ::= JavaIdentifier