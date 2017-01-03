package io.appetizer.intellij.remotecall.highlight;

import java.util.ArrayList;

public class GroupHighLight {
  private int groupid;
  public ArrayList<FileHighLight> myFileHighLights = new ArrayList<FileHighLight>();

  public int getGroupid() {
    return groupid;
  }

  public void setGroupid(int groupid) {
    this.groupid = groupid;
  }

  public ArrayList<FileHighLight> getHighlines() {
    return myFileHighLights;
  }

  public void addHighlines(FileHighLight highline) {
    this.myFileHighLights.add(highline);
  }
}
