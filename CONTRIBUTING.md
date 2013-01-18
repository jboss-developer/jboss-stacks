jdf-stack Contributing Guide
============================

Basic Steps
-----------

To contribute to the jdf-stack, fork the jdf-stack repository to your own Git, clone your fork, commit your work on topic branches, and make pull requests. 

If you don't have the Git client (`git`), get it from: <http://git-scm.com/>

Here are the steps in detail:

1. [Fork](https://github.com/jboss-jdf/jdf-stack/fork_select) the project. This creates a the project in your own Git.

2. Clone your fork. This creates a directory in your local file system.

        git clone git@github.com:<your-username>/jdf-stack.git

3. Add the remote `upstream` repository.

        git remote add upstream git@github.com:jboss-jdf/jdf-stack.git

4. Get the latest files from the `upstream` repository.

        git fetch upstream

5. Create a new topic branch to contain your features, changes, or fixes.

        git checkout -b <topic-branch-name> upstream/master

6. Contribute new code or make changes to existing files. Make sure that you follow the General Guidelines below.

7. Commit your changes to your local topic branch. You must use `git add filename` for every file you create or change.

        git add <changed-filename>
        git commit -m `Description of change...`

8. Push your local topic branch to your github forked repository. This will create a branch on your Git fork repository with the same name as your local topic branch name.

        git push origin HEAD            

9. Browse to the <topic-branch-name> branch on your forked Git repository and [open a Pull Request](http://help.github.com/send-pull-requests/). Give it a clear title and description.


General Guidelines
------------------

* The current file format is [1.0.0](https://raw.github.com/jboss-jdf/jdf-stack/1.0.0.Final/fileformat.png)

* The id of each item must match its anchor `(&)`

* The recommendedVersion attribute of Archetypes and BOMs must have it's correspondent ArchetypeVersion and BOMVersion

* The artifact (Archetype, BOM or Runtime) must be released before sending the pull request

* The pull request must be sent to the [1.0.0 branch](https://github.com/jboss-jdf/jdf-stack/tree/1.0.0.Final)

File format evolution
----------------------

This file has a strict file format defined by the [Stacks format diagram](https://raw.github.com/jboss-jdf/jdf-stack/1.0.0.Final/fileformat.png)

Each branch represents a file format. If you need to alter the file format, we strongly encourage you to discuss your planned change on the [dev list](http://www.jboss.org/jdf/forums/jdf-dev/) before starting.

If you need to update the stacks.yaml format, create a new branch to to avoid breaking compatibility with previous format versions.
