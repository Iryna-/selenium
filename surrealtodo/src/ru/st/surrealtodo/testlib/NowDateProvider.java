package ru.st.surrealtodo.testlib;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowDateProvider implements Provider {

  @Override
  public String getData() {
    return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
  }

}
