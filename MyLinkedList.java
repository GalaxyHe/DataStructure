package DataStructure;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * @author John He
 * @date 2019/2/9
 */

public class MyLinkedList<T> implements Iterable<T> {
    private Node first;//指向头节点的指针
    private Node last;//指向尾节点的指针
    private int modCount = 0;//用来记录修改此链表的次数（增、删、查、改等）
    private int size = 0;//此链表中元素的个数




    /*
     *实现迭代器
     */
    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListItr();
    }

    //构建一个空链表
    public MyLinkedList() {
        clear();
    }
    //-----------------------------------------------------------------------

    public static void main(String[] args) {
        MyLinkedList<Integer> lst = new MyLinkedList<>();

        lst.add(1, 1);
        lst.add(2, 3);
        lst.add(3, 5);
        lst.add(4, 7);
        lst.Remove(1);

        Iterator<Integer> itr = lst.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        System.out.println();
        System.out.println(lst.getSize());
        System.out.println(lst.isEmpty());
        System.out.println(lst.Contains(2));
    }
    //----------------------------------------------------------------------

    //----------------------------------------------------------------------
    /*
     * 私有的内部类，用来构造节点
     * 一个节点包含指向前一个节点的指针和到下一个节点的指针以及包含的数据
     */
    private static class Node<T> {
        public Node<T> prev;
        public Node<T> next;
        public T data;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.prev = prev;
            this.next = next;
            this.data = data;
        }
    }

    public void clear() {
        //连接头节点和尾节点，确保链表为空
        first = new Node<T>(null, null, null);
        last = new Node<T>(null, first, null);
        first.next = last;

        size = 0;
        modCount++;
    }

    //返回链表中元素的个数
    public int getSize() {
        return size;
    }

    //判断链表是否为空
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    //目标对象的第一个匹配项的索引值；没有，则返回-1
    public int indexof(Object obj) {
        int index = 0;
        if (obj == null) {
            for (Node<T> i = first; i != null; i = i.next) {
                if (i.data == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Node<T> i = first; i != null; i = i.next) {
                if (obj.equals(i.data)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    //判断此链表是否包含目标元素
    public boolean Contains(Object obj) {
        return indexof(obj) != -1;
    }

    //在非空节点p之前插入元素x
    private void addBefore(Node<T> p, T x) {
        assert (x != null);

        Node<T> pred = p.prev;
        Node<T> newNode = new Node<>(x, pred, p);
        p.prev = newNode;
        if (pred == null) {
            first = newNode;
        } else {
            pred.next = newNode;
        }

        size++;
        modCount++;
    }

    //--------------------------------------------------------------------------------------
    /*
     * 底下的五个私有方法为别的一些方法服务
     * */

    //将元素x插入到链表的尾端
    private void addLast(T x) {
        Node<T> l = last;
        Node<T> newNode = new Node<>(x, l, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }

        size++;
        modCount++;
    }

    //返回指定元素索引处的(非空)节点。
    private Node<T> getNode(int index) {
        if (index < 0 || index > getSize() + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> p;
        //在前半边查找
        if (index < (size >> 1)) {
            p = first.next;
            for (int i = 1; i < index; i++) {
                p = p.next;
            }
        }
        //在后半边查找
        else {
            p = last;
            for (int i = getSize(); i >= index; i--) {
                p = p.prev;
            }
        }
        return p;
    }

    private T Remove(Node<T> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        size--;
        modCount++;

        return p.data;
    }

    //检查目标索引是否越界
    private void RangeCheck(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("索引越界！");
        }
    }

    public void add(int index, T x) {
        addBefore(getNode(index), x);
    }
    //-------------------------------------------------------------------

    /*
     * 增、删、查、改操作
     */

    public boolean add(T x) {
        addLast(x);
        return true;
    }

    public T Remove(int index) {
        RangeCheck(index);

        return Remove(getNode(index));
    }

    public T set(int index, T newValue) {
        RangeCheck(index);

        Node<T> p = getNode(index);
        T oldValue = p.data;
        p.data = newValue;
        return oldValue;
    }

    private class MyLinkedListItr implements Iterator<T> {
        private Node<T> cur = first.next;//记录当前位置的指针
        private int expectedModCount = modCount;//记录迭代器对集合进行修改的次数
        private boolean Removeflag = false;//判断能否进行Remove的标志

        @Override
        public boolean hasNext() {
            return cur != last;
        }

        @Override
        public T next() {
            //********迭代期间不允许越过迭代器修改集合元素！
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException("你已越过迭代器修改集合元素！");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("没有元素了！");
            }

            T nextItem = cur.data;
            cur = cur.next;
            Removeflag = true;
            return nextItem;
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException("你已越过迭代器修改集合元素！");
            }
            if (!Removeflag) {
                throw new IllegalStateException();
            }

            MyLinkedList.this.Remove(cur.prev);
            expectedModCount++;
            Removeflag = false;//确保在next（)后能进行删除操作
        }
    }


}