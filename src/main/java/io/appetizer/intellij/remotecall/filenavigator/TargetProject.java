package io.appetizer.intellij.remotecall.filenavigator;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.intellij.openapi.diagnostic.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TargetProject {
  private static final Logger log = Logger.getInstance(TargetProject.class);
  private static final String pathConstraint = "src";
  private static final String androidManifestName = "AndroidManifest.xml";
  private Project myProject = null;

  public Project getTargetProject(String applicationid) {
    Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = new HashMap<Project, Collection<VirtualFile>>();
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      log.info("project:" + project.getName());
      // TODO: WHAT's wrong?
      foundFilesInAllProjects
        .put(project, FilenameIndex
          .getVirtualFilesByName(project, new File(androidManifestName).getName(), GlobalSearchScope.allScope(project)));
    }
    for (Project project : foundFilesInAllProjects.keySet()) {
      for (VirtualFile directFile : foundFilesInAllProjects.get(project)) {
        if (directFile.getPath().contains(pathConstraint) && directFile.getPath().endsWith(androidManifestName) && isValidProject(directFile.getPath(), applicationid)) {
          log.info("Check project package name:" + directFile.getName());
          myProject = project;
        }
      }
    }
    return myProject;
  }

  private static boolean isValidProject(String path, String applicationId) {
    Element element;
    File f = new File(path);
    log.info("file path: " + path);
    DocumentBuilder db;
    DocumentBuilderFactory dbf;
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
