package io.appetizer.intellij.remotecall.notifier;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import io.appetizer.intellij.VariantPool;
import io.appetizer.intellij.remotecall.RemoteCallComponent;
import io.appetizer.intellij.remotecall.filenavigator.ProcessType;
import io.appetizer.intellij.remotecall.filenavigator.TargetProject;
import io.appetizer.intellij.remotecall.handler.MessageHandler;
import com.intellij.openapi.diagnostic.Logger;
import io.appetizer.intellij.remotecall.highlight.HighLight;
import org.apache.commons.codec.Charsets;
import org.apache.commons.net.io.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static io.appetizer.intellij.ProjectInfo.getProjectInfo;
import static java.net.URLDecoder.decode;

public class SocketMessageNotifier implements MessageNotifier {

  private static final Logger log = Logger.getInstance(SocketMessageNotifier.class);
  private final Collection<MessageHandler> messageHandlers = new HashSet<MessageHandler>();
  private final ServerSocket serverSocket;
  private static final String CRLF = "\r\n";
  private static final String NL = "\n";
  private final InspectionProfileEntry owner = null;

  public SocketMessageNotifier(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void addMessageHandler(MessageHandler handler) {
    messageHandlers.add(handler);
  }

  public void run() {
    while (true) {
      Socket clientSocket;
      try {
        //noinspection SocketOpenedButNotSafelyClosed
        clientSocket = serverSocket.accept();
      }
      catch (IOException e) {
        if (serverSocket.isClosed()) {
          break;
        }
        else {
          log.error("Error while accepting", e);
          continue;
        }
      }

      InputStream inputStream = null;
      try {
        inputStream = clientSocket.getInputStream();
      }
      catch (IOException e) {
        log.error(e);
      }
      if (inputStream != null) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        try {
          String inputLine, requestString = "";
          while ((inputLine = in.readLine()) != null && !inputLine.equals(CRLF) && !inputLine.equals(NL) && !inputLine.isEmpty()) {
            requestString += inputLine;
          }
          StringTokenizer tokenizer = new StringTokenizer(requestString);
          String method = tokenizer.hasMoreElements() ? tokenizer.nextToken() : "";
          if (!method.equals("GET")) {
            log.warn("Only GET requests allowed");
            continue;
          }

          log.info("Received request " + requestString);
          Map<String, String> parameters = getParametersFromUrl(tokenizer.nextToken());
          String Operation = parameters.get("Operation") != null ? decode(parameters.get("Operation").trim(), Charsets.UTF_8.name()) : "";
          if (Operation.equals("Version")) {
            clientSocket.getOutputStream().write(("HTTP/1.1 200 OK" + CRLF + CRLF + RemoteCallComponent.version).getBytes(Charsets.UTF_8.name()));
            clientSocket.close();
          } else {
            final String applicationid = parameters.get("id") != null ? decode(parameters.get("id").trim(), Charsets.UTF_8.name()) : "";
            if (applicationid.equals("None")) {
              clientSocket.getOutputStream().write(("HTTP/1.1 200 OK" + CRLF + CRLF + "ApplicationId needed!").getBytes(Charsets.UTF_8.name()));
              clientSocket.close();
              return;
            }
            String message = "";
            if (Operation.equals("Query")) {
              String querygroupId =
                parameters.get("querygroupId") != null ? decode(parameters.get("querygroupId").trim(), Charsets.UTF_8.name()) : "0";
              int id = Integer.parseInt(querygroupId);
              String json = HighLight.getFileLinesJson(id);
              clientSocket.getOutputStream().write(("HTTP/1.1 200 OK" + CRLF + CRLF + json).getBytes(Charsets.UTF_8.name()));
            }
            else if (Operation.equals("QueryProjectInfo")) {
              clientSocket.getOutputStream().write(("HTTP/1.1 200 OK" + CRLF + CRLF + getProjectInfo()).getBytes(Charsets.UTF_8.name()));
            }
            else {
              clientSocket.getOutputStream().write(("HTTP/1.1 204 No Content" + CRLF + CRLF).getBytes(Charsets.UTF_8.name()));
            }
            clientSocket.close();
            if (Operation.equals("HightLight")) {
              String fileName = parameters.get("fileName") != null ? decode(parameters.get("fileName").trim(), Charsets.UTF_8.name()) : "";
              String groupId = parameters.get("groupId") != null ? decode(parameters.get("groupId").trim(), Charsets.UTF_8.name()) : "0";
              String lines = parameters.get("lines") != null ? decode(parameters.get("lines").trim(), Charsets.UTF_8.name()) : "";
              message = fileName + ":" + groupId + ":" + lines;
              log.info("Received message " + message);
              handleMessage(applicationid, message, ProcessType.TYPE.HIGHLIGHT);
            }
            else if (Operation.equals("HightLightAndNavigate")) {
              String fileName = parameters.get("fileName") != null ? decode(parameters.get("fileName").trim(), Charsets.UTF_8.name()) : "";
              String groupId = parameters.get("groupId") != null ? decode(parameters.get("groupId").trim(), Charsets.UTF_8.name()) : "0";
              String line = parameters.get("lines") != null ? decode(parameters.get("lines").trim(), Charsets.UTF_8.name()) : "";
              message = fileName + ":" + groupId + ":" + line;
              log.info("Received message " + message);
              handleMessage(applicationid, message, ProcessType.TYPE.NAVIGATEANDHIGHLIGHT);
            }
            else if (Operation.equals("RemoveHightLight")) {
              String groupId =
                parameters.get("removeGroupId") != null ? decode(parameters.get("removeGroupId").trim(), Charsets.UTF_8.name()) : "0";
              message = " " + ":" + groupId;
              log.info("RemoveHightLight : " + message);
              handleMessage(applicationid, message, ProcessType.TYPE.REMOVEHIGHLIGHT);
            }
            else if (Operation.equals("Tag")) {
              String taggedWords =
                parameters.get("taggedWords") != null ? decode(parameters.get("taggedWords").trim(), Charsets.UTF_8.name()) : "";
              String relatedFileName =
                parameters.get("relatedFileName") != null ? decode(parameters.get("relatedFileName").trim(), Charsets.UTF_8.name()) : "";
              String relatedLine =
                parameters.get("relatedline") != null ? decode(parameters.get("relatedline").trim(), Charsets.UTF_8.name()) : "0";
              VariantPool.setTaggedWords(taggedWords);
              VariantPool.setIsJump(true);
              VariantPool.setFileName(relatedFileName);
              VariantPool.setApplicationid(applicationid);
              int rl = Integer.parseInt(relatedLine);
              if (rl - 1 > 0) {
                VariantPool.setMyLine(rl - 1);
              }
              ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                  Project p = TargetProject.getTargetProject(applicationid);
                  log.info("ApplicationManager");
                  if (p == null) {
                    //TODO : Return error to appetizer
                    log.info("Project is null");
                  }
                  else {
                    DaemonCodeAnalyzer.getInstance(p).restart();
                  }
                }
              });
            }
          }
        }
        catch (IOException e) {
          log.error("Error", e);
        }
        finally {
          Util.closeQuietly(in);
        }
      }
    }
  }

  private static Map<String, String> getParametersFromUrl(String url) throws UnsupportedEncodingException {
    String parametersString = url.substring(url.indexOf('?') + 1);
    Map<String, String> parameters = new HashMap<String, String>();
    StringTokenizer tokenizer = new StringTokenizer(parametersString, "&");
    while (tokenizer.hasMoreElements()) {
      String[] parametersPair = tokenizer.nextToken().split("=", 2);
      if (parametersPair.length > 1) {
        parameters.put(parametersPair[0], decode(parametersPair[1], "utf-8"));
      }
    }
    return parameters;
  }

  private void handleMessage(String applicationid, String message, ProcessType.TYPE type) {
    for (MessageHandler handler : messageHandlers) {
      handler.handleMessage(applicationid, message, type);
    }
  }
}
