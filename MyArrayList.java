package DataStructure;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MyArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;//指定默认大小为10
    private static final Object[] EMPTY_ELEMENTDATA = {};//用于空实例的共享空数组实例。
    public transient T[] elementData;//底层的泛型数组用来保存元素
    public int size;//记录元素的个数


    /*
     * 实现迭代器
     */

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = (T[]) new Object[initialCapacity];
            this.size = 0;
        } else if (initialCapacity == 0) {
            this.elementData = (T[]) EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        }
    }

    public MyArrayList() {
        this.elementData = (T[]) new Object[10];
    }

    public MyArrayList(Collection<Object> c) {
        elementData = (T[]) c.toArray();//将集合转换为数组，数组容量为集合元素个数
        if ((size = elementData.length) != 0) {    //此时数组元素个数等于数组容量
            elementData = (T[]) Arrays.copyOf(elementData, size, Object[].class);
        }
    }

    public static void main(String[] args) {
        MyArrayList<Integer> lst = new MyArrayList<>();

        lst.add(1);
        lst.add(3);
        lst.add(5);
        lst.add(7);
        lst.add(9);
        Iterator<Integer> iterator = lst.iterator();
        /*while(iterator.hasNext()){
            System.out.println(iterator.next());
        }*/

        lst.add(2, 8);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        //System.out.println(lst.get(2));

        /*lst.Remove(5);
        lst.removeRange(1,3);
        System.out.println(lst.size);*/
    }

    //覆写Iterator的构造方法
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    //调整底层数组容量以契合当前元素数量,避免空元素部分太多而浪费内存。size是数组中实际存在的元素个数
    public void trimToSize() {
        int cur = elementData.length;
        if (size < cur) {
            elementData = Arrays.copyOf(elementData, size);
        }
    }

    //实现容量扩充
    public void ensureCapacity(int newCapacity) {
        int cur = elementData.length;
        if (newCapacity > cur) {
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    public void RangeCheck(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    /*
     *返回此列表中指定元素的第一个匹配项的索引，如果此列表不包含该元素，则返回-1
     */
    public int indexof(Object obj) {
        if (obj == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (obj.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /*
     *判断此列表中是否包含想要查找的元素 。有，返回true 没有，则返回false
     */
    public boolean Contains(Object obj) {
        return indexof(obj) >= 0;
    }

    //获取指定位置处元素的值
    public T get(int index) {
        RangeCheck(index);

        return elementData[index];
    }

    //修改指定位置的元素
    public void set(int index, T newElement) {
        RangeCheck(index);

        elementData[index] = newElement;
    }

    //转化为普通数组
    public T[] toArray() {
        T[] a = (T[]) new Object[size];
        System.arraycopy(elementData, 0, a, 0, size);
        return a;
    }


    //清空链表
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    //在指定位置处添加一个元素
    public void add(int index, T element) {
        RangeCheck(index);

        //再检查当前数组元素个数是否已经达到最大值，即达到数组容量，是，则扩充数组
        if (size == elementData.length) {
            ensureCapacity(elementData.length * 2 + 1);
        }
        //将插入位置的原数据以及其后的数据往右移动一位，腾出空间
        System.arraycopy(elementData, index, elementData, index + 1, size - index);

        /*
         public static native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);
         方法解读：第一个参数指明数据的来源数组，第二个参数说明数据源的起始位置
                   第三个参数说明数据去向的目标数组，第四个参数说明数据在目标数组中存放时的起始位置，
                   最后一个参数说明移动的数据长度（个数）。
         作用： 将数组从指定的源数组(从指定位置开始)复制到目标数组的指定位置
         该方法调用了C语言的memmove()函数，比一般的复制方法的实现效率要高很多，也安全很多，很适合用来批量处理数组。
        强烈推荐在复制大量数组元素时用该方法，以取得更高的效率。
         */

        elementData[index] = element;
        size++;
    }


    //在列表末尾追加一个元素
    public boolean add(T element) {
        if (size == elementData.length) {
            ensureCapacity(elementData.length * 2 + 1);
        }
        elementData[size++] = element;
        return true;
    }

    //将一个集合插入到列表某位置
    public void addAll(int index, Collection<? extends T> c) {
        RangeCheck(index);//先检查是否越界

        //获取插入元素以及个数
        T[] a = (T[]) c.toArray();
        int numNewlength = a.length;

        //扩充数组的容量
        ensureCapacity(size + numNewlength);

        //计算插入位置以及其后元素的个数，即：需要右移的元素个数
        int numMoved = size - index;
        //将原数组index~size范围的元素移动，腾出位置
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNewlength,
                    numMoved);
        }
        //将插入数组元素复制到elementData数组中腾出的位置
        System.arraycopy(a, 0, elementData, index, numNewlength);
        size += numNewlength;//元素个数也随之增加
    }


    //删除指定位置处元素
    public T Remove(int index) {
        RangeCheck(index);

        T oldelement = elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            //原位置后面的元素左移
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;//修改原数组最后一个元素位置为空,且元素个数相应的减少
        return oldelement;
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null;
    }

    //此列表中移除指定元素的第一个匹配项。
    public boolean Remove(Object obj) {
        if (obj == null) {
            for (int index = 0; index < size; index++) {
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
            }
        } else {
            for (int index = 0; index < size; index++) {
                if (obj.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
            }
        }
        return false;
    }

    //移除某个范围内的元素(不包括end）
    public void removeRange(int start, int end) {
        int NumMoved = size - end;

        //左移end之后的元素
        System.arraycopy(elementData, end, elementData, start, NumMoved);

        int newSize = size - (end - start);
        //修改左移留下的位置为空
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

    /*
     *覆写Iterator的next()、hasNext()以及remove方法
     */
    private class ArrayListIterator implements Iterator<T> {
        private int cur = 0;

        @Override
        public boolean hasNext() {
            return cur < size();
        }

        @Override
        public T next() {
            return elementData[cur++];
        }

        @Override
        public void remove() {
            MyArrayList.this.Remove(--cur);

        }
    }
}