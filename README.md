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
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
Then you can use the stacks-client API:        

        StacksClient stacksClient = new StacksClient();
        Stacks stacks = stacksClient.getStacks();
        
When you get the Stacks the stacks-client will retrieve the Stacks.yaml file from its repository and cache it locally.
The cache will be updated once a day (24 hours since last update)


JBoss Stacks Client configuration
---------------------------------

The stacks-client can be configured through the `StacksClientConfiguration` interface.

You can implement this class and pass it on the constructor of the StacksClient

Example:

        MyConfiguration myConfig = new MyConfiguration();
        StacksClient stacksClient = new StacksClient(myConfig);

If no configuration is supplied, StacksClient will use 'DefaultStacksClientConfiguration' class with de following values:

- url: The URL of stacks yaml file repository

- proxyHost: Proxy host to use. Default: null
- proxyPort: Proxy port to use. Default: null
- proxyUser: Proxy user to use. If proxy uses Authenticantion. Default: null
- proxyPassword: Proxy password to use. If proxy uses Authenticantion. Default: null

- online: Flag that indicates if the system should be used online/offline. Default: true
- cacheRefreshPeriodSeconds: Time in seconds to keep the cache information: Default to 86400 (24 hours)


At any moment will can retrieve the information by calling:

        stacksClient.getActualConfiguration();
        
Getting the Stacks messages
---------------------------

If you need to get the messages from stacks-client you can make your own implementation of 'StacksClientMessages' interface and use it on the StacksClient constructor.

Example:

        MyConfiguration myConfig = new MyConfiguration();
        MyMessageWay myMsg = new MyMessageWay();
        StacksClient stacksClient = new StacksClient(myConfig, myMsg);
        
               
File format evolution
----------------------

Each branch represents a file format. If you need to update the stacks.yaml format, create a new branch to to avoid breaking compatibility with previous format versions
