package ru.st.surrealtodo.testlib;

import java.util.Random;

public class StringProvider implements Provider {

  private final int maxSize;

  public StringProvider(int maxSize) {
    this.maxSize = maxSize;
  }

  public String getData() {
    Random rnd = new Random();
    int len = rnd.nextInt(maxSize-1) + 1;
    String name = "";
    for (int i = 0; i < len; i++) {
      if (rnd.nextInt(10) == 0) {
        name += " ";
      } else {
        name += (char) (rnd.nextInt(94) + 32);
      }
    }
    return name.trim();
  }
}
