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

* Add a group of highlight (< packagePath/fileName > + < lines >) to < groupId >, at the same time highlight lines of this group of lines, and jump to the first line of < lines >.
```linux
 python main.py [-p <port>] --id <applicaionid> -g <groupId> -f <packagePath/fileName> -l <line>
```
* Add a group of highlight (< packagePath/fileName > + < lines >) to < groupId >, at the same time highlight lines of this group of lines. (Comparing to previous one, no Jumping )

```linux
 python main.py [-p <port>] --id <applicaionid> --hl -g <groupId> -f <packagePath/fileName> -l <line>
```
* Highlight < line > of < packagePath/fileName >, and jump to < line >.
```linux
 python main.py [-p <port>] --id <applicaionid> -j -f <packagePath/fileName> -l <line>
```
* Cancel to the highlighting lines of < removeGroupId >

```linux
 python main.py [-p <port>] --id <applicaionid> --rg <removeGroupId>
```
* Find the < taggedWords >, and activate 'small bulb' which provides suggestions to jump to the < relatedline > of < relatedFileName >

```linux
 python main.py [-p <port>] --id <applicaionid> --tw <taggedWords> --rf <packagePath/fileName> --rl <relatedline>
```
* Query highlighting info of < querygroupId >, return JSON.
```linux
 python main.py [-p <port>] --id <applicaionid> --qg <querygroupId>
```

* Query simple Project info, return JSON.
```linux
 python main.py [-p <port>] --id <applicaionid> -i
```



 
 

