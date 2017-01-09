package io.appetizer.intellij.component;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import io.appetizer.intellij.remotecall.highlight.HighLight;
import org.jetbrains.annotations.NotNull;

public class MyTypedHandler implements TypedActionHandler {
  private static final Logger log = Logger.getInstance(MyTypedHandler.class);
  @Override
  public void execute(@NotNull final Editor editor,final char c, @NotNull DataContext dataContext) {
    final Document document = editor.getDocument();
    final Project project = editor.getProject();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        insertCharacter(editor, document, c);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        VirtualFile virtualFiles[] = FileEditorManager.getInstance(project).getSelectedFiles();
        editor.getMarkupModel().removeAllHighlighters();
        HighLight.removeByFile(virtualFiles[0].getPath());
      }
    };
    WriteCommandAction.runWriteCommandAction(project, runnable);
  }
  private void insertCharacter(Editor editor, Document document, char c) {
    CaretModel caretModel = editor.getCaretModel();
    document.insertString(caretModel.getOffset(), String.valueOf(c));
    caretModel.moveToOffset(caretModel.getOffset() + 1);
  }
}
