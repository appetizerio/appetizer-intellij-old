package io.appetizer.intellij.remotecall.filenavigator;

import java.util.ArrayList;

public interface FileNavigator {
  void findAndNavigate(String fileName, ArrayList<Integer> lines, ProcessType.TYPE type);
}
