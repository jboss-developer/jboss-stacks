jdf-stack Contributing Guide
============================

Basic Steps
-----------

To contribute with Stacks, clone your own fork instead of cloning the main Stacks repository, commit your work on topic branches and make pull requests. In detail:

1. [Fork](https://github.com/jboss-jdf/jdf-stack/fork_select) the project.

2. Clone your fork (`git@github.com:<your-username>/jdf-stack.git`).

3. Add an `upstream` remote (`git remote add upstream git@github.com:jboss-jdf/jdf-stack.git`).

4. Get the latest changes from upstream (e.g. `git pull upstream master`).

5. Create a new topic branch to contain your feature, change, or fix (`git checkout -b <topic-branch-name>`).

6. Make sure that your changes follow the General Guidelines below.

7. Run the tests

        mvn test

8. Commit your changes to your topic branch.

9. Push your topic branch up to your fork (`git push origin  <topic-branch-name>`).

10. [Open a Pull Request](http://help.github.com/send-pull-requests/) with a clear title and description.

If you don't have the Git client (`git`), get it from: <http://git-scm.com/>


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
