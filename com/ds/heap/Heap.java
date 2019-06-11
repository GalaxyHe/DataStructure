package DataStructureDemo.com.ds.heap;

/*
 * @author He
 * @Date 2019/6/6
 * @Time 19:44
 * @Description 实现一个大根堆
 */

import java.util.Arrays;
import java.util.Comparator;


public class Heap<E> {
    private E[] elementData;
    private static final int DEFAULT_CAPACITY = 15;
    private Comparator<E> comparator;
    private int size;

    //构造一个初始容量为15的堆
    public Heap() {
        this(DEFAULT_CAPACITY,null);
    }

    public Heap(int initialCapacity) {
        this(initialCapacity,null);
    }

    public Heap(int initialCapacity,Comparator<E> comparator) {
        this.elementData = (E[]) new Object[initialCapacity];
        this.comparator = comparator;
    }



    //将任意数组堆化，时间复杂度为O（n）
    public Heap(E[] array) {
        elementData = (E[])new Object[array.length];

        System.arraycopy(array, 0, elementData, 0, array.length);

        this.size = elementData.length;
        //堆化
        //从最后一个非叶子节点开始进行下沉操作（siftdown）
        for (int i = parentIndex(array.length-1); i >=0; i--) {
            SiftDown(i);
        }
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * 比较两个元素的大小
     * @param o1
     * @param o2
     * @return
     */
    private int compare(E o1,E o2) {
        if (comparator == null) {
            // 此时E必须为Compareable接口子类
            return ((Comparable<E>)o1).compareTo(o2);
        }

        return comparator.compare(o1,o2);
    }

    //对底层数组进行扩容
    public void ensureCapacity(){
        int cur = elementData.length;
        int newCapacity = cur +
                (cur < 64 ? cur : cur >> 1);

        if(cur <=newCapacity){
            elementData = Arrays.copyOf(elementData,newCapacity);
        }
    }

    //调整当前节点在堆中的位置,元素上浮
    private void SiftUp(int index){
        while (index > 0 && compare(elementData[index],
                elementData[parentIndex(index)]) > 0) {

            // 交换当前节点与父节点的值
           Swap(index,parentIndex(index));
            index = parentIndex(index);
        }
    }


    //元素下沉
    private void SiftDown(int index){
        while (leftChildIndex(index) < size) {
            // 当前节点左孩子下标
            int j = leftChildIndex(index);
            // 判断是左孩子大还是右孩子大
            if (j + 1 < size) {
                // 此时有右孩子
                if (compare(elementData[j],elementData[j + 1]) < 0)
                    // j指向右孩子索引下标
                    j ++;
            }
            // elementData[j]一定是左右孩子的最大值
            if (compare(elementData[index],elementData[j]) > 0){
                break;
            }

            Swap(index,j);
            index = j;
        }
    }

    public void add(E e) {
        if (size == elementData.length) {
            // 扩容
           ensureCapacity();
        }

        // 向数组末尾添加元素
        elementData[size ++] = e;
        // siftUp
        SiftUp(size - 1);
    }

    public E findMax() {
        //判断当前堆是否为空
        if (isEmpty()){
            throw new IndexOutOfBoundsException("heap is empty");
        }

        return elementData[0];
    }

    //取出堆中最大元素，即堆顶元素
    public E extractMax() {
        E result = findMax();
        // 交换堆顶元素与最后一个元素的位置
        Swap(0,size - 1);
        //删除堆中最后一个元素
        elementData[-- size] = null;

        // siftDown操作
        SiftDown(0);
        return result;
    }


    //取出堆中最大元素并返回，同时替换成新元素newVal
    public E replace(E newVal){
        E ret = findMax();
        elementData[0] = newVal;

        SiftDown(0);
        return ret;
    }


    private void Swap(int indexA, int  indexB) {
        E temp = elementData[indexA];
        elementData[indexA] = elementData[indexB];
        elementData[indexB] = temp;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E element : elementData) {
            if (element != null) {
                sb.append(element).append("->");
            }
        }
        return sb.toString().substring(0,sb.length()-2);
    }

    /*
     * 返回当前节点的左孩子节点下标
     * @param index
     * @return
     */
    private int leftChildIndex(int index) {
        return index * 2 + 1;
    }

    /*
     * 返回当前节点的右孩子节点下标
     * @param index
     * @return
     */
    private int rightChildIndex(int index) {
        return index * 2 + 2;
    }

    /*
     * 取得当前节点父节点索引下标
     * @param index
     * @return 父节点下标
     */
    private int parentIndex(int index) {
        if (index == 0) {
            throw new IllegalArgumentException("不存在父亲节点！");
        }

        return (index - 1) / 2;
    }


    public static void main(String[] args) {
        Heap<Integer> heap = new Heap<>();
        int[] num = {1,3,5,10,2,8,4,20};
        for(int n:num){
            heap.add(n);
        }

        System.out.println(heap.toString());
        System.out.println(heap.findMax());

        System.out.println(heap.replace(12));
        System.out.println(heap.findMax());

        System.out.println(heap.size);
    }
}
