package io.appetizer.intellij.remotecall.handler;

import io.appetizer.intellij.remotecall.filenavigator.ProcessType;

public interface MessageHandler {

  void handleMessage(String applicationid, String message, ProcessType.TYPE type);

}
