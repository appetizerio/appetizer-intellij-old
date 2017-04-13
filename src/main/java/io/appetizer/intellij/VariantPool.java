package io.appetizer.intellij;


public class VariantPool {
  private static String myTaggedWords = "";
  private static String myFileName = "";
  private static int myLine = 0;
  private static boolean isJump = false;
  private static String applicationid = "";

  public static void setTaggedWords(String taggedWords) {myTaggedWords = taggedWords; }
  public static void setFileName(String fileName){ myFileName = fileName; }
  public static void setMyLine(int line){ myLine = line; }
  public static void setIsJump(boolean jumpExist){ isJump = jumpExist; }

  public static String getMyFileName(){return myFileName; }
  public static int getMyLine() {return myLine; }
  public static boolean isJump() {return isJump; }
  public static String getMyTaggedWords() {return myTaggedWords;}

  public static void setApplicationid(String applicationid) {
    VariantPool.applicationid = applicationid;
  }
}
