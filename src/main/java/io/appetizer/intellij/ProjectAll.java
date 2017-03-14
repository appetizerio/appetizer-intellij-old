package io.appetizer.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectAll {
  private static final String androidManifestName = "AndroidManifest.xml";

  public static Map<Project, Collection<VirtualFile>> setFoundFilesInAllProjects(String filename, boolean isId) {
    Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = new HashMap<Project, Collection<VirtualFile>>();
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      if (isId) {
        foundFilesInAllProjects.put(project, FilenameIndex.getVirtualFilesByName(project, new File(androidManifestName).getName(), GlobalSearchScope.allScope(project)));
      } else {
        foundFilesInAllProjects.put(project, FilenameIndex.getVirtualFilesByName(project, new File(filename).getName(), GlobalSearchScope.allScope(project)));
      }
    }
    return foundFilesInAllProjects;
  }
}
