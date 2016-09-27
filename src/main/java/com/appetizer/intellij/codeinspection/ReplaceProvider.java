package com.appetizer.intellij.codeinspection;

import com.intellij.codeInspection.InspectionToolProvider;

public class ReplaceProvider implements InspectionToolProvider {
  @Override
  public Class[] getInspectionClasses() {
    return new Class[]{ReplaceInspection.class};
  }
}
