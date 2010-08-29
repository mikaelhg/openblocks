OpenBlocks refactoring branch
=============================

This is a fork of [MIT OpenBlocks](http://education.mit.edu/drupal/openblocks) (covered by the MIT license) for refactoring purposes.

The principal objective of the fork is to make the code more amenable to inclusion to other projects.

To do:
------

* remove dependency to TableLayout, which doesn't appear to be actively maintained, and really not even used in a very complex manner
* replace static singletons with contexts and instances
* move the various project packages from the main namespace to under the ``edu.mit.openblocks`` package
