jdf-stack
=========

Stack index

What is it?
-----------

The stacks.yaml file represents all relationship between Runtimes, Maven BOMs and Archetypes.

Run the tests
-------------

If you modify the stacks.yaml file run the tests to get sure that it is working

        mvn test

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


ArchetypeVersion labels
----------------------

- type : javaee-web, javaee-ear, html5-mobile, richfaces-kitchensink, errai-kitchensink, spring-kitchensink
- isBlank : true if it's a blank archetype
- environment : web-ee6 or full-ee6, depending on the required Java EE profile. We'll add web-ee7, full-ee7 for future wildfly archetypes
- target : product or community
- additionalRepositories : a Map containing repository-id as key and the repository url as value
- essentialDependencies : collection of GAV coordinates of essential depenendencies tooling will try to resolve before creating the project. If these deps are missing the repos defined in additionalRepositories will be proposed to be added to settings.xml