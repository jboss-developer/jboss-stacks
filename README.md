jdf-stack
=========

Stack index

What is it?
-----------

The stacks.yaml file represents all relationship between Runtimes, Maven BOMs and Archetypes.

How to parse the file ?
-----------------------

If you need to convert the stack.yaml file to Java Objects add the following dependency to your project

        <dependency>
            <groupId>org.jboss.jdf</groupId>
            <artifactId>stacks-client</artifactId>
            <version>LATEST-VERSION</version>
        </dependency>
        
Then you can use the stacks-client API:        

        StacksClient stacksClient = new StacksClient();
        Stacks stacks = stacksClient.getStacks();
        
When you get the Stacks the stacks-client will retrieve the Stacks.yaml file from its repository and cache it locally.
The cache will be updated once a day (24 hours since last update)
               
File format evolution
----------------------

Each branch represents a file format. If you need to update the stacks.yaml format, create a new branch to to avoid breaking compatibility with previous format versions
