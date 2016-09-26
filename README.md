# andygen

This is an Android/Groovy application for randomly generating various
things for tabletop RPGs (names, spells, items, monsters, etc). The 
content for this application was taken with permission from the following
sources:

* Perilous Wilds, by Jason Lutes. http://lampblackandbrimstone.blogspot.com/
* Freebooters on the Frontier, by Jason Lutes. http://lampblackandbrimstone.blogspot.com/
* Maze Rats 0.1, by Ben Milton. http://questingblog.com/maze-rats/

## Screenshot

![alt text](https://github.com/stevesea/andygen/raw/master/docs/images/mr_monsters.png "Monsters generated for Maze Rats")

## Goals

I learn new things best when I've got a project to work on. I decided
to learn more about Groovy and Android development, and this seemed like
a tool I could get personal use out of when DMing.

## todo

- cleanup directory structure. figure out license, and if want to stay
  on github
- figure out requirements
 -- how easy should it for non-programmer to add new data? (right now it's hard)
 -- what niche is this trying to fill? I don't care about a random d100 
    table with 2-3 sentence result. I want:
      - evocative tables from interesting supplements
      - complicated tables with multiple rolls (PW steadings, dangers, etc)
- is dagger helping or hurting?
- add unit tests
- limit # of items in results list. 
- figure out publishing
- develop a Groovy DSL to define the data or at least the formatting? Or, 
  should it all be data driven?
- how to delete individual results? click/long-click/gesture?
- how to internationalize rpg content?
- cleanup API between random tables & UI
- use recyclerview instead of listview
- move data into separate package/module
- more dagger goodness
- more swissknife goodness
- image buttons? (game-icons.net)
- preferences (??? what would they be?)
- a way to copy/save a set of results? (send via e-mail?)


## Other random generators with the same/similar content

https://perilous-wilds.geekwire.net/region

http://www.random-generator.com/index.php?title=Category:Dungeon_World

https://gitlab.com/phrakture/perlious-wilds/tree/master


## Various helpful coding / IDE reference links

dagger
https://github.com/square/dagger/tree/master/examples/android-simple/src/main/java/com/example/dagger/simple

https://github.com/pieces029/is-taylor-swift-single-groovy-android/tree/master/src/main/groovy/com/andrewreitz/taylor


add 'new groovy class' template to Android Studio http://www.mscharhag.com/groovy/creating-android-apps-with-groovy

https://github.com/JStumpp/awesome-android

http://www.vogella.com/tutorials/AndroidRecyclerView/article.html

http://stackoverflow.com/questions/28684759/import-material-design-icons-into-an-android-project


http://blog.sqisland.com/2014/12/recyclerview-grid-with-header.html
