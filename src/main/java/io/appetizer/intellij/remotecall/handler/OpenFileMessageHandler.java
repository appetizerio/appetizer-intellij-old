package io.appetizer.intellij.remotecall.handler;

import io.appetizer.intellij.remotecall.filenavigator.FileNavigator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import io.appetizer.intellij.remotecall.filenavigator.GroupHighlighter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class OpenFileMessageHandler implements MessageHandler {
  private static final Pattern COLUMN_PATTERN = compile("[:#](\\d+)[:#]?(.*)$");
  private final FileNavigator fileNavigator;
  private static final Logger log = Logger.getInstance(OpenFileMessageHandler.class);

  public OpenFileMessageHandler(FileNavigator fileNavigator) {
    this.fileNavigator = fileNavigator;
  }

  public void handleMessage(String message, boolean isAdd) {
    Matcher matcher = COLUMN_PATTERN.matcher(message);
    int groupid ;
    String lines;
    ArrayList<Integer> linesArrayList = new ArrayList<Integer>();
    if (matcher.find()) {
      if (isAdd) {
        groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
        if (matcher.groupCount() >= 1) {
          lines = StringUtil.notNullize(matcher.group(2));
          String[] strs = lines.split("\\-");
          for (String s : strs) {
            linesArrayList.add(Integer.parseInt(s));
          }
          GroupHighlighter.addGroup(new GroupHighlighter(groupid, linesArrayList));
          fileNavigator.findAndNavigate(matcher.replaceAll(""), linesArrayList, true);
        }
      }else {
        groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
        linesArrayList = GroupHighlighter.getLines(groupid) != null ? GroupHighlighter.getLines(groupid) : null;
        if (linesArrayList == null) {
          return;
        }
        fileNavigator.findAndNavigate(matcher.replaceAll(""), linesArrayList, false);
      }
    }
  }
}
