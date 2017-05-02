# appetizer-intellij
Appetizer-intellij is an IntelliJ/Android Studio plugin that exposes some functionalities of these IDEs to modern continuous integration systems. The goal is to allow CI systems to leverage the code navigation, code highlighting or even refactoring capability of powerful IDEs.

## Installation

### Install from JAR archive

To install the plugin from the JAR archive, follow these steps:

* Download the latest version of the plugin from  [GitHub release](https://github.com/appetizerio/appetizer-intellij/releases)
* Once downloaded, open the IntelliJ IDEA **File -> Settings -> Plugins -> Install plugin from disk -> Choose**
* Browse to the location of the downloaded JAR archive.
* Press OK to confirm the installation. The IDE will ask you to restart in order to complete the plugin installation. Restart your IDE.

### Install from plugin repositories

Coming soon

### Install from Appetizer Desktop

Coming soon

## Usage
Once installed, `appetizer-intellij` listens at `localhost:8097` for commands (port can be configured in the setting panel of the plugin). It receives REST commands and we provide a simple Python CLI for demonstration.

* Highlight certain line(s) of a Java class. `group` is an arbitrary numeric ID. `class_name` is a fully qualified Java class, e.g. `com.example.MyClass`. `lines` is a comma-separated list of line numbers, e.g., `1,2,100`. After executing, these highlights would be added to the specified group and can be unhighlighted by giving the group.
```bash
 python cli.py highlight group class_name lines
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/highlight.gif)

* Un-highlight a certain highlight group
```bash
 python cli.py unhighlight group
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/unhighlight.gif)

* Query which line(s) belong to a certain highlight group
```bash
 python cli.py query group
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/query.gif)

* Jump to a certain line of a Java class
```bash
 python cli.py jump class_name line
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/jump.gif)

* Query the basic information of the project of last operation
```bash
 python cli.py info
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/info.gif)

* Query plugin version
```bash
 python cli.py version
```
![image](https://github.com/appetizerio/appetizer-intellij/blob/master/pic/version.gif)
