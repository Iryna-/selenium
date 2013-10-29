package ru.st.surrealtodo.tests2;

import ru.st.surrealtodo.fw.AppObject;

public interface ParentProvider<T extends AppObject<T,?>> {
  T getObject();
  String newObjectDefaultText();
  boolean newObjectPrepending();
}
