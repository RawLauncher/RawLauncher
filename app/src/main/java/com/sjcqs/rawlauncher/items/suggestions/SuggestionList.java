package com.sjcqs.rawlauncher.items.suggestions;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.util.SortedList;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;

import java.text.Collator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by satyan on 8/25/17.
 */

public class SuggestionList implements List<Suggestion>  {

    private SortedList<Suggestion> list;
    private Map<Suggestion, Item> itemMap;
    private final SortedList.Callback<Suggestion> callback = new SortedList.Callback<Suggestion>() {
        private final Collator COLLATOR = Collator.getInstance(Locale.getDefault());

        @Override
        public int compare(Suggestion item1, Suggestion item2) {
            double rate1 = item1.getRate(), rate2 = item2.getRate();
            int rateCmp = Double.compare(rate1,rate2);
            return (rateCmp != 0 ?
                    rateCmp : (COLLATOR.compare(item1.getLabel(), item2.getLabel())));
        }

        @Override
        public void onChanged(int position, int count) {

        }

        @Override
        public boolean areContentsTheSame(Suggestion oldItem, Suggestion newItem) {
            return oldItem.getRate() == newItem.getRate();
        }

        @Override
        public boolean areItemsTheSame(Suggestion item1, Suggestion item2) {
            return item1.getItem().equals(item2.getItem());
        }

        @Override
        public void onInserted(int position, int count) {

        }

        @Override
        public void onRemoved(int position, int count) {

        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
        }
    };

    public SuggestionList() {
        list = new SortedList<>(Suggestion.class, callback);
        itemMap = new ArrayMap<>();
    }

    public boolean containsItem(Item item){
        return itemMap.containsValue(item);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof Suggestion && list.indexOf((Suggestion) o) != SortedList.INVALID_POSITION;
    }

    @NonNull
    @Override
    public Iterator<Suggestion> iterator() {
        return new Iterator<Suggestion>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public Suggestion next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                Suggestion suggestion = get(index);
                index++;
                return suggestion;
            }
        };
    }

    @NonNull
    @Override
    public Suggestion[] toArray() {
        Suggestion array[] = new Suggestion[size()];
        for (int i = 0; i < this.size(); i++) {
            array[i] = get(i);
        }
        return array;
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {

        for (int i = 0; i < ts.length; i++) {
            if (i < size()){
                ts[i] = (T) get(i);
            } else {
                ts[i] = null;
            }
        }
        return ts;
    }

    @Override
    public boolean add(Suggestion suggestion) {
        list.add(suggestion);
        itemMap.put(suggestion,suggestion.getItem());
        return contains(suggestion);
    }

    @Override
    public boolean remove(Object o) {
        itemMap.remove(o);
        return list.remove((Suggestion) o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        for (Object o : collection) {
            Suggestion suggestion = (Suggestion)o;
            if (!contains(suggestion)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Suggestion> collection) {
        list.addAll((Collection<Suggestion>) collection);
        for (Suggestion suggestion : collection) {
            itemMap.put(suggestion, suggestion.getItem());
        }
        return containsAll(collection);
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends Suggestion> collection) {
        list.addAll((Collection<Suggestion>) collection);
        for (Suggestion suggestion : collection) {
            itemMap.put(suggestion, suggestion.getItem());
        }
        return containsAll(collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean changed = false;
        for (Object o : collection) {
            Suggestion suggestion = (Suggestion)o;
            if(remove(suggestion)){
                itemMap.remove(suggestion);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        list.clear();
        itemMap.clear();
    }

    @Override
    public Suggestion get(int i) {
        return list.get(i);
    }

    @Override
    public Suggestion set(int i, Suggestion suggestion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int i, Suggestion suggestion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Suggestion remove(int i) {
        Suggestion suggestion = list.removeItemAt(i);
        itemMap.remove(suggestion);
        return suggestion;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf((Suggestion) o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.indexOf((Suggestion) o);
    }

    @Override
    public ListIterator<Suggestion> listIterator() {
        return listIterator(0);
    }

    @NonNull
    @Override
    public ListIterator<Suggestion> listIterator(final int i) {
        return new ListIterator<Suggestion>() {
            private int index = i;
            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public Suggestion next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                Suggestion suggestion = get(index);
                index++;
                return suggestion;
            }

            @Override
            public boolean hasPrevious() {
                int prev = index - 1;
                return prev > 0 && prev < size();
            }

            @Override
            public Suggestion previous() {
                if (!hasPrevious()){
                    throw new NoSuchElementException();
                }
                Suggestion suggestion = get(index);
                index--;
                return suggestion;
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                Suggestion suggestion = list.removeItemAt(index);
                itemMap.remove(suggestion);
            }

            @Override
            public void set(Suggestion suggestion) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(Suggestion suggestion) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @NonNull
    @Override
    public List<Suggestion> subList(int fromIndex, int toIndex) {
        SuggestionList list = new SuggestionList();
        if((fromIndex < 0 || toIndex > size() || fromIndex > toIndex)){
            throw new IndexOutOfBoundsException();
        }
        for (int i = fromIndex; i < toIndex; i++) {
            list.add(get(i));
        }
        return list;
    }
}
