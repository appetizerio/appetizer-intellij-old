package io.appetizer.intellij.component;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;


public class SayHelloAction extends AnAction {
  private static final Logger log = Logger.getInstance(SayHelloAction.class);
  @Override
  public void actionPerformed(AnActionEvent e) {
    Application application = ApplicationManager.getApplication();
    Component myComponent = application.getComponent(Component.class);
    myComponent.sayHello();
  }
}
