package io.appetizer.intellij.remotecall.filenavigator;

import com.google.common.base.Joiner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
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
  private static final String androidManifestName = "AndroidManifest.xml";

  @Override
  public void findAndNavigate(final String fileName, final ArrayList<Integer> lines, final boolean isAdd) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      public void run() {
        Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = new HashMap<Project, Collection<VirtualFile>>();
        Project[] projects = ProjectManager.getInstance().getOpenProjects();

        for (Project project : projects) {
          foundFilesInAllProjects
            .put(project, FilenameIndex.getVirtualFilesByName(project, new File(fileName).getName(), GlobalSearchScope.allScope(project)));
        }
        Deque<String> pathElements = splitPath(fileName);
        String variableFileName = pathJoiner.join(pathElements);

        while (pathElements.size() > 0) {
          for (Project project : foundFilesInAllProjects.keySet()) {
            for (VirtualFile directFile : foundFilesInAllProjects.get(project)) {
              if (directFile.getPath().contains(pathConstraint)) {
                // AndroidManifest.xml
                if (directFile.getPath().endsWith(androidManifestName)) {
                  log.info("Check project package name" + directFile.getName());
                  // TODO: Check if apk belongs to opened project
                }
                if (directFile.getPath().endsWith(variableFileName)) {
                  log.info("Found file " + directFile.getName());
                  if (isAdd) {
                    navigate(project, directFile, lines);
                  } else {
                    removeHightlight(project, lines);
                  }
                  return;
                }
              }
            }
          }
          pathElements.pop();
          variableFileName = pathJoiner.join(pathElements);
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
    final OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, file);
    if (openFileDescriptor.canNavigate()) {
      log.info("Trying to navigate to " + file.getPath());
      openFileDescriptor.navigate(true);
      addLinesHighlighter(project, lines);
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
    editor.getMarkupModel().removeAllHighlighters();
    final TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(JBColor.LIGHT_GRAY);
    //attr.setForegroundColor(JBColor.LIGHT_GRAY);
    for (int line : lines){
      editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
    }
  }

  private static void removeHightlight(Project project, ArrayList<Integer> linesArrayList) {
    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    editor.getMarkupModel().getDocument();
    final TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(JBColor.WHITE);
    attr.setForegroundColor(JBColor.BLACK);
    for (int line : linesArrayList){
      editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
    }
  }
}
