# appetizer-intellij

## Installation

### Install from JAR archive

To install the plugin from the JAR archive, follow these steps:

* Download the latest version of the plugin from  [appetizer-intellij.jar](https://github.com/appetizerio/appetizer-intellij/blob/master/appetizer-intellij.jar)
* Once downloaded, open the IntelliJ IDEA **File -> Settings -> Plugins -> Install plugin from disk -> Choose**
* Browse to the location of the downloaded JAR archive.
* Press OK to confirm the installation. The IDE will ask you to restart in order to complete the plugin installation. Restart your IDE.


### Install from plugin repositories

Coming soon

## Usage

Python cmd APIs are provided.

* Add a group of highlight (< clazz > + < lines >) to < group >, at the same time highlight lines of this group of lines, and jump to the first line of < lines >.
```linux
 python cli.py highlight group clazz lines [lines ...] 
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/highlight.gif)


* Jump to (< clazz > + < line >)
```linux
 python cli.py jump clazz line
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/jump.gif)


* Cancel to the highlighting lines of < group >
```linux
 python cli.py unhighlight < group >
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/unhighlight.gif)


* Find the < word >, and activate 'small bulb' which provides suggestions to jump to the < lines > of < clazz >

```linux
 python cli.py tagwords word clazz lines [lines ...]
```

* Query highlighting info of < group >, return JSON.
```linux
 python cli.py query < group >
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/query.gif)

* Query simple Project info, return JSON. **(Return last operation's project Info)**
```linux
 python cli.py info
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/info.gif)

* Query plugin version
```linux
 python cli.py version
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/version.gif)



 
 

