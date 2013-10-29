package ru.st.surrealtodo.fw;

import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ListOf<T extends AppObject<T,?>> extends ForwardingList<T> {

  private List<T> list = Lists.newArrayList();
  private ImmutableMap<String, T> idToItem;

  public ListOf() {
  }

  public ListOf(ListOf<T> listToCopy) {
    list = Lists.newArrayList(listToCopy);
  }

  @Override
  protected List<T> delegate() {
    return list;
  }

  public ListOf<T> withAppended(T item) {
    ListOf<T> newItems = new ListOf<T>();
    newItems.list = Lists.newArrayList(this.list);
    newItems.list.add(item);
    return newItems;
  }

  public ListOf<T> without(T item) {
    ListOf<T> newItems = new ListOf<T>();
    newItems.list = Lists.newArrayList(this.list);
    newItems.list.remove(item);
    return newItems;
  }

  public ListOf<T> withPrepended(T item) {
    ListOf<T> newItems = new ListOf<T>();
    newItems.list = Lists.newArrayList();
    newItems.list.add(item);
    newItems.list.addAll(this.list);
    return newItems;
  }

  public T getById(String id) {
    if (idToItem == null) {
      idToItem = Maps.uniqueIndex(list, new Function<T, String> () {
        public String apply(T item) {
          return item.id();
        }
      });
    }
    return idToItem.get(id);
  }

  public T getSome() {
    if (size() == 0) {
      return null;
    } else {
      return list.get(new Random().nextInt(size()));
    }
  }
  
}
