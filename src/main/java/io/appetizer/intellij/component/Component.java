package io.appetizer.intellij.component;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;

public class Component implements ApplicationComponent {

  public Component() {
  }

  public void initComponent() {
    // TODO: insert component initialization logic here
  }

  public void disposeComponent() {
    // TODO: insert component disposal logic here
  }

  @org.jetbrains.annotations.NotNull
  public String getComponentName() {
    return "Component";
  }

  public void sayHello() {
    String welcomeInfo = "<html> Appetizer is a DevOps tool intended for mobile app development. " +
                         "<br/>Download Appetizer from <a href=\"http://www.appetizer.io/\"> here</a> .</html>";
    Messages.showMessageDialog(welcomeInfo, "Apptizer", Messages.getInformationIcon());
  }
}