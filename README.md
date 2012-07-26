jdf-stack
=========

Stack index

What is it?
-----------

The stacks.yaml file represents all relationship between Runtimes, Maven BOMs and Archetypes.

How to parse the file ?
-----------------------

If you need to convert the stack.yaml file to Java Objects just copy the Parser.java to your project (remember to update the package declaration) and run

        Parser p = new Parser();
        Stacks stacks =  p.parse( inputStream );


File format evolution
----------------------

Each branch represents a file format. If you need to update the stacks.yaml format, create a new branch to to avoid breaking compatibility with previous format versions
