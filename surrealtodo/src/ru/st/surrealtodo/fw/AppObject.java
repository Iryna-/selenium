package ru.st.surrealtodo.fw;

public abstract class AppObject<T extends AppObject<T,Child>, Child extends AppObject<Child,?>> extends HelperWithWebDriverBase {

  protected String id;
  protected String text;
  protected String creationDate;

  protected ListOf<Child> cachedChildren;

  public AppObject(Application manager) {
    super(manager);
  }
  
  public T withId(String id) {
    this.id = id;
    return (T) this;
  }
  
  public String id() {
    return id;
  }

  public T withText(String text) {
    this.text = text;
    return (T) this;
  }

  public String text() {
    return text;
  }

  public T withCreationDate(String creationDate) {
    this.creationDate = creationDate;
    return (T) this;
  }

  public String creationDate() {
    return creationDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AppObject<T,Child> other = (AppObject<T,Child>) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "AppObject [id=" + id + ", name=" + text + "]";
  }
  
  public ListOf<Child> getChildren() {
    if (cachedChildren == null) {
      updateCachedChildren();
    }
    return new ListOf<Child>(cachedChildren);
  }
  
  protected ListOf<Child> updateCachedChildren() {
    cachedChildren = updateCachedChildrenInternal();
    return cachedChildren;
  }

  protected abstract ListOf<Child> updateCachedChildrenInternal();
  public abstract T changeTextTo(String newText);
  public abstract T changeCreationDateTo(String newDate);
  public abstract Child createChild();
  public abstract void delete();
}
