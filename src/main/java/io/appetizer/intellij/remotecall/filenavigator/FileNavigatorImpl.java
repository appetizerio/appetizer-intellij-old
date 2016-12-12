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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.appetizer.intellij.remotecall.filenavigator.ProcessType.TYPE.HIGHLIGHT;

public class FileNavigatorImpl implements FileNavigator {

  private static final Logger log = Logger.getInstance(FileNavigatorImpl.class);
  private static final Joiner pathJoiner = Joiner.on("/");
  private static final String pathConstraint = "src";
  private static final String androidManifestName = "AndroidManifest.xml";
  private static Project myProject = null;
  private static int count = 0;

  @Override
  public void findAndNavigate(final String fileName, final ArrayList<Integer> lines, final ProcessType.TYPE type) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      public void run() {
        Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = new HashMap<Project, Collection<VirtualFile>>();
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
          log.info("project:" + project.getName());
          foundFilesInAllProjects
            .put(project, FilenameIndex.getVirtualFilesByName(project, new File(androidManifestName).getName(), GlobalSearchScope.allScope(project)));
        }
        for (Project project : foundFilesInAllProjects.keySet()) {
          for (VirtualFile directFile : foundFilesInAllProjects.get(project)) {
            if (directFile.getPath().contains(pathConstraint) && directFile.getPath().endsWith(androidManifestName) && isValidProject(directFile.getPath(), "org.luoluyao.myapplication")) {
              log.info("Check project package name:" + directFile.getName());
              myProject = project;
            }
          }
        }
        if (myProject == null) {
          // TODO: return error to appetizer
          return;
        }
        Deque<String> pathElements = splitPath(fileName);
        String variableFileName = pathJoiner.join(pathElements);
        log.info("variableFileName:" + variableFileName);
        while (pathElements.size() > 0) {
          for (VirtualFile directFile : FilenameIndex.getVirtualFilesByName(myProject, new File(fileName).getName(), GlobalSearchScope.allScope(myProject))) {
            if (directFile.getPath().contains(pathConstraint)) {
              if (directFile.getPath().endsWith(variableFileName)) {
                log.info("Found file " + directFile.getName());
                switch (type) {
                  case HIGHLIGHT:
                    addLinesHighlighter(myProject, lines);
                    break;
                  case NAVIGATE:
                    navigate(myProject, directFile, lines);
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
      //addLinesHighlighter(project, lines);
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
    // TODO: Check if line is illegal
    for (int line : lines){
      log.info("line:" + line);
      editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
    }
  }

  private static void removeHightlight(Project project, VirtualFile file, ArrayList<Integer> linesArrayList) {
    FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(file);
    Editor editor= fileEditor instanceof TextEditor ? ((TextEditor)fileEditor).getEditor() : null;
    editor.getMarkupModel().getDocument();
    if (linesArrayList.contains(-1)) {
      editor.getMarkupModel().removeAllHighlighters();
      return;
    }
    final TextAttributes attr = new TextAttributes();
    attr.setBackgroundColor(JBColor.WHITE);
    attr.setForegroundColor(JBColor.BLACK);
    for (int line : linesArrayList){
      editor.getMarkupModel().addLineHighlighter(line - 1, HighlighterLayer.LAST, attr);
    }
  }

  private static boolean isValidProject(String path, String applicationId) {
    Element element = null;
    File f = new File(path);
    log.info("file path: " + path);
    DocumentBuilder db = null;
    DocumentBuilderFactory dbf = null;
    try {
      dbf = DocumentBuilderFactory.newInstance();
      db = dbf.newDocumentBuilder();
      Document dt = db.parse(f);
      element = dt.getDocumentElement();
      String packageName = element.getAttributes().getNamedItem("package").getNodeValue();
      log.info("packageName:" + packageName);
      if (packageName.equals(applicationId)) {
        return true;
      }
      else {
        return false;
      }
    } catch (Exception e) {
      log.error("Error", e);
    }
    return false;
  }
}
