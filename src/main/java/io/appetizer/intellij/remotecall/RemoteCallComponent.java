package io.appetizer.intellij.remotecall;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import io.appetizer.intellij.remotecall.handler.OpenFileMessageHandler;
import io.appetizer.intellij.remotecall.listener.DocumentChangeListener;
import io.appetizer.intellij.remotecall.notifier.MessageNotifier;
import io.appetizer.intellij.remotecall.notifier.SocketMessageNotifier;
import io.appetizer.intellij.remotecall.settings.RemoteCallSettings;
import io.appetizer.intellij.remotecall.filenavigator.FileNavigatorImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class RemoteCallComponent implements ApplicationComponent {
  private static final Logger log = Logger.getInstance(RemoteCallComponent.class);
  private final RemoteCallSettings mySettings;
  public final static String version = "Appetizer.io 1.0";

  private ServerSocket serverSocket;
  private Thread listenerThread;

  public RemoteCallComponent(RemoteCallSettings settings) {
    mySettings = settings;
  }

  public void initComponent() {
    final int port = mySettings.getPortNumber();
    final boolean allowRequestsFromLocalhostOnly = mySettings.isAllowRequestsFromLocalhostOnly();
    EditorEventMulticaster eventMulticaster = EditorFactory.getInstance().getEventMulticaster();
    eventMulticaster.addDocumentListener(new DocumentChangeListener());

    //final JTextPane messageComponent = new JTextPane();
    //Messages.configureMessagePaneUi(messageComponent, message);
    //Messages.showMessageDialog("<html>Go to <a href=\"http://www.appetizer.io/\">appetizer</a></html>", "Appetizer",
    //                          Messages.getInformationIcon());
    //essages.configureMessagePaneUi()
    /*
    DialogWrapper.DoNotAskOption option = new DialogWrapper.DoNotAskOption() {
      @Override
      public boolean isToBeShown() {
        return RemoteCallSettings.getInstance().getAskAgain();
      }

      @Override
      public void setToBeShown(boolean value, int exitCode) {
        RemoteCallSettings.getInstance().setAskAgain(value);
      }

      @Override
      public boolean canBeHidden() {
        return true;
      }

      @Override
      public boolean shouldSaveOptionsOnCancel() {
        return false;
      }

      @NotNull
      @Override
      public String getDoNotShowMessage() {
        return "Do not ask me again";
      }
    };


    int result = MessageDialogBuilder.yesNo("Appetizer", "<html> Appetizer is a DevOps tool intended for mobile app development. " +
                                                         "<br/>Would you like to download Appetizer from <a href=\"http://www.appetizer.io/\"> here </a>?</html>")
      .yesText("OK, I know it !")
      .doNotAsk(option)
      .show();
*/
    try {
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(allowRequestsFromLocalhostOnly ? "localhost" : "0.0.0.0", port));
      log.info("Listening " + port);
    }
    catch (IOException e) {
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        public void run() {
          Messages.showMessageDialog("Can't bind with " + port + " port. Appetizer plugin won't work", "Appetizer Plugin Error",
                                     Messages.getErrorIcon());
        }
      });
      return;
    }

    MessageNotifier messageNotifier = new SocketMessageNotifier(serverSocket);
    messageNotifier.addMessageHandler(new OpenFileMessageHandler(new FileNavigatorImpl()));
    listenerThread = new Thread(messageNotifier);
    listenerThread.start();
  }

  public void disposeComponent() {
    try {
      if (listenerThread != null) {
        listenerThread.interrupt();
      }
      serverSocket.close();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  public String getComponentName() {
    return "Appetizer.io";
  }

}