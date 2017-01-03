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
  public static Map<Project, Collection<VirtualFile>> foundFilesInAllProjects = new HashMap<Project, Collection<VirtualFile>>();
  private static final String androidManifestName = "AndroidManifest.xml";

  public static void setFoundFilesInAllProjects() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      foundFilesInAllProjects.put(project, FilenameIndex
        .getVirtualFilesByName(project, new File(androidManifestName).getName(), GlobalSearchScope.allScope(project)));
    }
  }
}
