package io.appetizer.intellij.remotecall.listener;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import io.appetizer.intellij.remotecall.highlight.HighLight;

public class DocumentChangeListener implements DocumentListener {
  private static Logger log = Logger.getInstance(DocumentChangeListener.class);

  public void beforeDocumentChange(DocumentEvent event) {
  }

  public void documentChanged(DocumentEvent event) {
    Editor[] editors = EditorFactory.getInstance().getEditors(event.getDocument());
    for (Editor editor : editors) {
      editor.getMarkupModel().removeAllHighlighters();
      HighLight.removeAll();
    }
  }
}
