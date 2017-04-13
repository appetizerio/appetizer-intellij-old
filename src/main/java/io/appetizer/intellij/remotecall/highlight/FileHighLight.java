package io.appetizer.intellij.remotecall.highlight;

import java.util.ArrayList;

public class FileHighLight {
  private String fileName = "";
  private ArrayList<Integer> lines = new ArrayList<Integer>();

  public FileHighLight(String myfileName, ArrayList<Integer> mylines){
    this.fileName = myfileName;
    this.lines = mylines;
  }

  public String getFileName() {
    return fileName;
  }

  public ArrayList<Integer> getLines() {
    return lines;
  }

  public String getJson(){
    String json = "";
    json += "\"" + fileName + "\"" + ":[";
    for (int l : lines) {
      json += l + ",";
    }
    json = json.substring(0, json.length() - 1);
    json += "]";
    return json;
  }
}
