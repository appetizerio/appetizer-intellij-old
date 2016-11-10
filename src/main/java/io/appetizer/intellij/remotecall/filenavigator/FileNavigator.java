package io.appetizer.intellij.remotecall.filenavigator;

public interface FileNavigator {
  void findAndNavigate(String fileName, int line, int column, int offsetline);
}
