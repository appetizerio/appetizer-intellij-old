package io.appetizer.intellij.component;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;

public class EditorIllustration extends AnAction {
  private static final Logger log = Logger.getInstance(EditorIllustration.class);
  static {
    final EditorActionManager actionManager = EditorActionManager.getInstance();
    final TypedAction typedAction = actionManager.getTypedAction();
    typedAction.setupHandler(new MyTypedHandler());
  }

  @Override
  public void actionPerformed(AnActionEvent anActionEvent) {
  }
}