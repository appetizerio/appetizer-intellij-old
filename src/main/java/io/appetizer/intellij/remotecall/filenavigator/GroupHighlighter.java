package io.appetizer.intellij.remotecall.filenavigator;

import java.util.ArrayList;

public class GroupHighlighter {
  private static ArrayList<GroupHighlighter> groupsArrayList = new ArrayList<GroupHighlighter> ();
  private int groupid;
  private ArrayList<Integer> lines = new ArrayList<Integer>();

  public GroupHighlighter(int groupid, ArrayList<Integer> lines){
    this.groupid = groupid;
    this.lines = lines;
  }

  public static void addGroup(GroupHighlighter gh) {
    groupsArrayList.add(gh.groupid, gh);
  }

  public static ArrayList<Integer> getLines(int groupid) {
    if (groupsArrayList == null) {
      return null;
    }
    for (GroupHighlighter gal : groupsArrayList) {
      if (gal.groupid == groupid) {
        return gal.lines;
      }
    }
    return null;
  }
}
