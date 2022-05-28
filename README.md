OpenBlocks refactoring branch
=============================

This is a fork of [MIT OpenBlocks](https://web.mit.edu/mitstep/openblocks.html)
(covered by the MIT license) for refactoring purposes.

I left this repository open for many years, as a resource for others with an interest in the topic,
even though my active use case became moot twelve years back.
Now, however, as there are many better maintained forks, I am about to archive this repository.

The principal objective of the fork was to make the code more amenable to inclusion to other projects.

If you're building a web application, you might want to consider [Google Blockly](https://developers.google.com/blockly) instead.

Getting Started:
------
	$ mvn clean package
	$ mvn exec:java -Dexec.mainClass="edu.mit.blocks.controller.WorkspaceController" -Dexec.args="support/lang_def.xml"

To do:
------

* upgrade code maintainability from the academic baseline to the commercial baseline
* replace static singletons with contexts and instances

Done:
-----

* move the various project packages from the main namespace to under the ``edu.mit.blocks`` package
* refactor out functionality such as network communication
* remove dependency to TableLayout, which doesn't appear to be actively maintained,
  and really not even used in a very complex manner

Contributors:
-------------

In order of first appearance on commit logs:

* The original MIT team: Eric Klopfer, Daniel Wendel, Ricarose Roque, Corey McCaffrey, Lunduo Ye, Aidan Ho, Brett Warne, Xudan Liu, Hout Nga
* Mikael Gueck (https://github.com/mikaelhg)
* David Li (https://github.com/taweili)
* Philippe Cade (https://github.com/philippecade)
* Tony Graham / MenteaXML (https://github.com/MenteaXML)
