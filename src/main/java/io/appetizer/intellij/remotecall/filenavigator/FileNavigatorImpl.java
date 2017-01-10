package io.appetizer.intellij.remotecall.filenavigator;

import com.google.common.base.Joiner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.JBColor;
import java.awt.*;
import java.io.File;
import java.util.*;

public class FileNavigatorImpl implements FileNavigator {

  private static final Logger log = Logger.getInstance(FileNavigatorImpl.class);
  private static final Joiner pathJoiner = Joiner.on("/");
  private static final String pathConstraint = "src";
  private Project myProject = null;

  @Override
  public void findAndNavigate(final String applicationid, final String fileName, final ArrayList<Integer> lines, final ProcessType.TYPE type) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      public void run() {
        myProject = TargetProject.getTargetProject(applicationid);
        if (myProject == null) {
          // TODO: return error to appetizer
          return;
        }
          for (VirtualFile directFile : FilenameIndex.getVirtualFilesByName(myProject, new File(fileName).getName(), GlobalSearchScope.allScope(myProject))) {
            if (directFile.getPath().contains(pathConstraint)) {
              if (directFile.getPath().endsWith(fileName)) {
                log.info("Found file " + directFile.getName());
                switch (type) {
                  case HIGHLIGHT:
                    addLinesHighlighter(myProject, lines);
                    break;
                  case NAVIGATE:
                    navigate(myProject, directFile, lines);
                    addLinesHighlighter(myProject, lines);
                    break;
                  case REMOVEHIGHLIGHT:
                    removeHightlight(myProject, directFile, lines);
                    break;
                  case NAVIGATEANDHIGHLIGHT:
                    navigate(myProject, directFile, lines);
                    addLinesHighlighter(myProject, lines);
                    break;
                  default:
                    log.info("Can't find");
                    break;
                }
                return;
              }
            }
          }
      }
    });
  }

  private static Deque<String> splitPath(String filePath) {
    File file = new File(filePath);
    Deque<String> pathParts = new ArrayDeque<String>();
    pathParts.push(file.getName());
    while ((file = file.getParentFile()) != null && !file.getName().isEmpty()) {
      pathParts.push(file.getName());
    }
    return pathParts;
  }

  private static boolean checkPackageName(Project project, VirtualFile file, String packageName) {
    return true;
  }

  private static void navigate(Project project, VirtualFile file, ArrayList<Integer> lines) {
    final OpenFileDescriptor openFileDescriptor;
    if (!lines.isEmpty()) {
      int line = lines.get(0);
      openFileDescriptor = new OpenFileDescriptor(project, file, line - 1, 0);
    } else {
      openFileDescriptor = new OpenFileDescriptor(project, file);
    }
    if (openFileDescriptor.canNavigate()) {
      log.info("Trying to navigate to " + file.getPath());
      openFileDescriptor.navigate(true);
      Window parentWindow = WindowManager.getInstance().suggestParentWindow(project);
      if (parentWindow != null) {
        parentWindow.toFront();
      }
    }
    else {
      log.info("Cannot navigate");
    }
  }

  private static void addLinesHighlighter(Project project, ArrayList<Integer> lines) {
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    //editor.getMarkupModel().removeAllHighlighters();
    final TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(JBColor.LIGHT_GRAY);
    //attr.setForegroundColor(JBColor.LIGHT_GRAY);
    // TODO: Check if line is illegal
    for (int line : lines){
      log.info("line:" + line);
      if (line >= 1) {
        editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
      }
    }
  }

  private static void removeHightlight(Project project, VirtualFile file, ArrayList<Integer> linesArrayList) {
    FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(file);
    Editor editor= fileEditor instanceof TextEditor ? ((TextEditor)fileEditor).getEditor() : null;
    editor.getMarkupModel().getDocument();
    final TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(JBColor.WHITE);
    attr.setForegroundColor(JBColor.BLACK);
    for (int line : linesArrayList){
      editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
    }
  }
}
