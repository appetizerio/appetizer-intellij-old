package io.appetizer.intellij.remotecall.notifier;

import io.appetizer.intellij.remotecall.handler.MessageHandler;

public interface MessageNotifier extends Runnable {

  void addMessageHandler(MessageHandler handler);

}
