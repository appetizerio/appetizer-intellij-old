package io.appetizer.intellij;

public class ProjectInfo {
  private static String applicationId;
  private static String projectPath;
  private static String projectName;
  private static String apkPath;
  private static String baseProjectPath;

  public static String getBaseProjectPath() {
    return baseProjectPath;
  }

  public static void setBaseProjectPath(String baseProjectPath) {
    ProjectInfo.baseProjectPath = baseProjectPath;
  }

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

  public static void setApkpath(String apkpath) {
    apkPath = apkpath;
  }

  public static String getProjectInfo() {
    setApkpath(baseProjectPath + "/build/outputs/apk/");
    String projectinfo = "{";
    projectinfo += "\"projectname\":\"" + projectName + "\",";
    projectinfo += "\"projectpath\":\"" + projectPath + "\",";
    projectinfo += "\"projectapkpath\":\"" + apkPath + "\",";
    projectinfo += "\"baseprojectpath\":\"" + baseProjectPath + "\"";
    projectinfo += "}";
    return projectinfo;
  }
}
