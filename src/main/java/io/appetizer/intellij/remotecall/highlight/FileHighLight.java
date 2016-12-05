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

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public ArrayList<Integer> getLines() {
    return lines;
  }

  public void addLines(int line) {
    this.lines.add(line);
  }

  public void setLines(ArrayList<Integer> line) {
    this.lines = line;
  }
}
