package io.appetizer.intellij.remotecall.handler;

import io.appetizer.intellij.remotecall.filenavigator.FileNavigator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import io.appetizer.intellij.remotecall.filenavigator.ProcessType;
import io.appetizer.intellij.remotecall.highlight.FileHighLight;
import io.appetizer.intellij.remotecall.highlight.HighLight;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static java.util.regex.Pattern.compile;
import static org.codehaus.groovy.syntax.Types.NAVIGATE;

public class OpenFileMessageHandler implements MessageHandler {
  private static final Pattern COLUMN_PATTERN = compile("[:#](\\d+)[:#]?(.*)$");
  private final FileNavigator fileNavigator;
  private static final Logger log = Logger.getInstance(OpenFileMessageHandler.class);

  public OpenFileMessageHandler(FileNavigator fileNavigator) {
    this.fileNavigator = fileNavigator;
  }

  public void handleMessage(String applicationid, String message, ProcessType.TYPE type) {
    Matcher matcher = COLUMN_PATTERN.matcher(message);
    int groupid ;
    String lines;
    ArrayList<Integer> linesArrayList = new ArrayList<Integer>();
    if (matcher.find()) {
      switch (type) {
        case HIGHLIGHT:
          groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
          if (matcher.groupCount() >= 1) {
            lines = StringUtil.notNullize(matcher.group(2));
            String[] strs = lines.split("\\-");
            for (String s : strs) {
              linesArrayList.add(Integer.parseInt(s));
              log.info("Line:" + s);
            }
            //GroupHighlighter.addGroup(new GroupHighlighter(groupid, linesArrayList));
            FileHighLight fileHighLight = new FileHighLight(matcher.replaceAll(""), linesArrayList);
            HighLight.addHighToGroup(groupid, fileHighLight);
            log.info("FileHighLight: fileName" + matcher.replaceAll(""));
            fileNavigator.findAndNavigate(applicationid, matcher.replaceAll(""), linesArrayList, ProcessType.TYPE.HIGHLIGHT);
          }
          break;
        case NAVIGATE:
          int line = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
          linesArrayList.add(line);
          fileNavigator.findAndNavigate(applicationid, matcher.replaceAll(""), linesArrayList, ProcessType.TYPE.NAVIGATE);
          break;
        case NAVIGATEANDHIGHLIGHT:
          groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
          if (matcher.groupCount() >= 1) {
            lines = StringUtil.notNullize(matcher.group(2));
            String[] strs = lines.split("\\-");
            for (String s : strs) {
              linesArrayList.add(Integer.parseInt(s));
              log.info("Line:" + s);
            }
            //GroupHighlighter.addGroup(new GroupHighlighter(groupid, linesArrayList));
            FileHighLight fileHighLight = new FileHighLight(matcher.replaceAll(""), linesArrayList);
            HighLight.addHighToGroup(groupid, fileHighLight);
            log.info("FileHighLight: fileName" + matcher.replaceAll(""));
            fileNavigator.findAndNavigate(applicationid, matcher.replaceAll(""), linesArrayList, ProcessType.TYPE.NAVIGATEANDHIGHLIGHT);
          }
          break;
        case REMOVEHIGHLIGHT:
          groupid = StringUtil.parseInt(StringUtil.notNullize(matcher.group(1)), 1);
          log.info("groupId : " + groupid);
          ArrayList<FileHighLight> als;
          als = HighLight.getFileLines(groupid);
          if (als == null) {
            return;
          }
          for (FileHighLight al : als) {
            log.info(al.getFileName());
            fileNavigator.findAndNavigate(applicationid, al.getFileName(), al.getLines(), ProcessType.TYPE.REMOVEHIGHLIGHT);
          }
          break;
      }
    }
  }
}
