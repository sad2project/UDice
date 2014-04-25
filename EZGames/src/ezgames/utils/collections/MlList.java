package ezgames.utils.collections;

import java.util.Iterator;
import ezgames.annotations.Immutable;
import ezgames.math.hashing.HashUtil;
import ezgames.utils.DataChecker;
import ezgames.utils.IterableUtil;
import ezgames.utils.Weighted;

//TODO: testing and documentation
/**
 * <p>MlList was originally created because I looked into the ML language (which is
 * a functional language) and I really liked how it worked, so I created my own
 * for fun.  But I learned that it's also really good at building {@link Iterable}s
 * quite quickly. It's main use is in connection with the {@link Stackable} 
 * interface.</p>
 * <p>It's good for building {@code Iterable}s quickly because it returns the new
 * MlList with each add() or and() call, so one could be built as follows:<br>
 * {@code Iterable<Integer> ints = MlList.empty().add(1).and(2).and(3).and(4);}<br>
 * With a Stackable type, you can do it even more simply:<br>
 * {@code Iterable<StackableInt> ints = one.and(two).and(three).and(four);}
 * where one, two, three, and four are hypothetical StackableInt 
 * (Stackable<Integer>) objects. See the {@link Stackable} interface to learn
 * more about how it works with MlList to increase fluency and conciseness.</p>
 * <p>The only real downside of MlList is that is stores the objects in the 
 * reverse order that you put them in. It sort of functions like a non-popping
 * Stack. This problem is offset by two different ideas:<br>
 * + The UDiceSystem does not require anything to be in any specific order, other
 * than {@link Range}s in a {@link RangeCollection}, but the ordering is dealt
 * with by the Builder.<br>
 * + MlList comes with a {@code reverse()} method for reversing the list.
 * @param <E> - The type of elements stored in the MlList
 */
@Immutable
public class MlList<E> implements Iterable<E>
{
	//**************************************************************************
	// Public static factories
	//**************************************************************************
	public static <E> MlList<E> fromIterable(Iterable<E> iterable) throws IllegalArgumentException
	{
		//if it's already an MlList, then just send it back
		if (iterable instanceof MlList) { return (MlList<E>) iterable; }
		
		//let's create the new one
		MlList<E> backwards = MlList.<E>empty();
		for (E o : iterable)
		{
			backwards = backwards.add(o);
		}
		return backwards.reverse();
	}
	
	@SuppressWarnings("unchecked")
	public static <E> MlList<E> fromValues(E... arr)
	{
		DataChecker.checkArrayDataNotNull(arr, "Cannot create MlList with null elements");
		
		if(0 == arr.length)
		{
			return (MlList<E>)empty();
		}
		
		MlList<E> backwards = MlList.<E>empty();
		for(E o : arr)
		{
			backwards = backwards.add(o);
		}
		return backwards.reverse();
	}
	
	@SuppressWarnings("unchecked")
	public static <E> MlList<E> empty()
	{
		return (MlList<E>) EMPTY;
	}
	
	public static <E> MlList<E> append(MlList<E> l1, MlList<E> l2)
	{
		if (l2 == EMPTY)
		{
			return l1;
		}
		else if (l1 == EMPTY)
		{
			return l2;
		}
		else
		{
			return append(l1.tail, l2).add(l1.head);
		}
	}
	
	public static <E> MlList<E> reverse(MlList<E> list)
	{
		if (list.isNull() || list.tail.isNull()) { return list; }
		
		return reverse(list, MlList.<E>empty());
	}
	
	//**************************************************************************
	// Public API methods
	//**************************************************************************
	public MlList<E> add(final E element)
	{
		if (element == null) { return this; }
		MlList<E> out = new MlList<>(element, this);
		return out;
	}
	
	/**
	 * Same as the {@link add(E)} method. Can be used to make building MlLists more fluent.
	 * For example, {@code list.add(4).and(5).and(6)} reads a little better than
	 * {@code list.add(4).add(5).add(6)}.
	 */
	public MlList<E> and(final E element)
	{
		return add(element);
	}
	
	public boolean contains(final E obj)
	{
		return IterableUtil.contains(this, obj);
	}
	
	public boolean equals(Object o)
	{
		if (o == this) { return true; }
		
		if (!(o instanceof MlList<?>)) { return false; }
		
		MlList<?> oList = (MlList<?>) o;
		
		return head.equals(oList.head) && tail.equals(oList.tail);
	}
	
	public E get(int index)
	{
		return IterableUtil.getFrom(this, index);
	}
	
	public int hashCode()
	{
		HashUtil hasher = HashUtil.createDefaultHashUtil();
		int code = hasher.getStartingValue();
		code = hasher.hash(head, code);
		return hasher.hash(tail, code);
	}
	
	public E head()
	{
		return head;
	}
	
	public int indexOf(E obj)
	{
		return IterableUtil.indexOf(this, obj);
	}
	
	public boolean isNull()
	{
		return head == null;
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new MllIterator<E>(this);
	}
	
	public MlList<E> reverse()
	{
		return reverse(this);
	}
	
	public int size()
	{
		return IterableUtil.sizeOf(this);
	}
	
	public MlList<E> tail()
	{
		return tail;
	}
	
	public String toString()
	{
		if (isNull()) { return ""; }
		return "[" + head.toString() + "]" + tail.toString();
	}
	
	@SuppressWarnings("unchecked")
	public MlList<Weighted<E>> withWeight(int weight)
	{
		if(head instanceof Weighted<?>)
		{
			Weighted<E> newHead = new Weighted<E>(((Weighted<E>) head).object(), weight);
			return ((MlList<Weighted<E>>) tail).add(newHead);
		}
		//set all tail elements to have a weight of 1
		MlList<Weighted<E>> out = tail.withWeight(1);
		//make a new version of the head with weight
		Weighted<E> newHead = new Weighted<>(head, weight);
		//add the head to the new tail and return it
		return out.add(newHead);
	}
	
	//**************************************************************************
	// Private members
	//**************************************************************************
	@SuppressWarnings("rawtypes")
	private static final MlList EMPTY = new MlList();
	private final E head;
	private final MlList<E> tail;
	private final int size;
		
	//**************************************************************************
	// Private constructors
	//**************************************************************************
	private MlList()
	{
		head = null;
		tail = null;
		size = 0;
	}
	
	@SuppressWarnings("unchecked")
	private MlList(E o)
	{
		head = o;
		tail = (MlList<E>) EMPTY;
		size = 1;
	}
	
	private MlList(E o, MlList<E> list)
	{
		head = o;
		tail = list;
		size = list.size + 1;
	}
	
	//**************************************************************************
	// Private static helpers
	//**************************************************************************
	private static <E> MlList<E> reverse(MlList<E> oldList, MlList<E> newList)
	{
		if(oldList.isNull()) {return newList;}
		
		return reverse(oldList.tail, newList.add(oldList.head));
	}
	
	//**************************************************************************
	// Private Iterator Type
	//**************************************************************************
	private static class MllIterator<E> implements Iterator<E>
	{
		private MlList<E> next;
		
		public MllIterator(MlList<E> list)
		{
			next = list;
		}
		
		public boolean hasNext()
		{
			return !next.isNull();
		}
		
		public E next()
		{
			E out = next.head;
			next = next.tail;
			return out;
		}
		
		public void remove()
		{
			throw new UnsupportedOperationException("Cannot remove elements from MlList");
		}
	}
}