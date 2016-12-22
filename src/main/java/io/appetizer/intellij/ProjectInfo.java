package io.appetizer.intellij;

public class ProjectInfo {
  private static String applicationId;
  private static String projectPath;
  private static String projectName;
  private String apkPath;

  public String getProjectName() {
    return projectName;
  }

  public static void setProjectName(String projectname) {
    projectName = projectname;
  }

  public String getApplicationId() {
    return applicationId;
  }

  public static void setApplicationId(String applicationid) { applicationId = applicationid; }

  public String getProjectpath() {
    return projectPath;
  }

  public static void setProjectpath(String projectpath) {
    projectPath = projectpath;
  }

  public String getApkpath() {
    return apkPath;
  }

  public void setApkpath(String apkpath) {
    this.apkPath = apkpath;
  }
}
