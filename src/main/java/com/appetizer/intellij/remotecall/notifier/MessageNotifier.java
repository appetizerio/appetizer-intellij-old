package com.appetizer.intellij.remotecall.notifier;

import com.appetizer.intellij.remotecall.handler.MessageHandler;

public interface MessageNotifier extends Runnable {

  void addMessageHandler(MessageHandler handler);

}
