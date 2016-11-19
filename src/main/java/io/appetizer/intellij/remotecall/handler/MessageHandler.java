package io.appetizer.intellij.remotecall.handler;

public interface MessageHandler {

  void handleMessage(String message, boolean isAdd);

}
