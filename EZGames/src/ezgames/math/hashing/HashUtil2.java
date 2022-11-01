package ezgames.math.hashing;

/**
 * {@code HashUtil} is a class to help with creating hash codes.
 * <p>{@code HashUtil} is capable of using many different hashing techniques
 * by accepting any technique that can be implemented as an
 * {@code IHashAlgorithm}.  This utility's main job is to convert any given
 * type to an integer before passing it to its {@code IHashAlgorithm}, which
 * only accepts integers for hashing.</p>
 * <p>Example usage:</p>
 * public int hashCode() {<br>
 * &emsp; 	return HashUtil2.defaultHasher() <br>
 * &emsp;	    .hash(member1)<br>
 * &emsp;	    .hash(member2)<br>
 * &emsp;	    .hashVal<br>
 * }
 * 
 */
public class HashUtil
{
   private IHashAlgorithm strategy;
   public int hashVal;
   
   public HashUtil2(IHashAlgorithm strategy)
   {
      this.strategy = strategy;
      this.hashVal = strategy.getStartingValue()
   }
   //**************************************************************************
   // Built-in HashAlgorithm Getters
   //**************************************************************************
   /**
    * Returns an instance of the {@code FnvHashAlgorithm} class
    * @return an instance of the {@code FnvHashAlgorithm} class
    */
   public static HashUtil2 fnvHasher()
   {
      return new HashUtil2(new nnvHashAlgorithm());
   }
   
   /**
    * Returns an instance of the {@code RotatingHashAlgorithm} class
    * @return an instance of the {@code RotatingHashAlgorithm} class
    */
   public static HashUtil2 rotatingHasher()
   {
      return new HashUtil2(new RotatingHashAlgorithm());
   }
   
   /**
    * Returns an instance of the {@code BernsteinHashAlgorithm} class
    * @return an instance of the {@code BernsteinHashAlgorithm} class
    */
   public static HashUtil2 bernsteinHasher()
   {
      return new HashUtil2(new BernsteinHashAlgorithm());
   }
   
   //**************************************************************************
   // Public Static Factories
   //**************************************************************************
   /**
    * Constructs a new {@code HashUtil} with a default {@code IHashAlgorithm}
    * of {@code FnvHashAlgorithm}.
    */
   public static HashUtil2 defaultHasher()
   {
      return new HashUtil2(new FnvHashAlgorithm());
   }
   
   //**************************************************************************
   // Public API Methods
   //**************************************************************************   
   /**
    * Creates a hash value from a boolean value.
    * <p>The boolean is transformed into an int value of alternating 0s and 1s
    * using {@code (bool ? 1431655765 : -1431655766)}. 
    * Then it is hashed as a int.</p>
    * @param bool - the boolean to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the boolean and the current total
    */
   public HashUtil2 hash(boolean bool)
   {
      int l = (bool ? 1431655765 : -1431655766);
      hashVal = strategy.hashValue(l, hashVal);
      return this'
   }
   
   /**
    * Creates a hash value from a character.
    * <p>The character is transformed with a simple cast to a int. 
    * Then it is hashed as a int.</p>
    * @param aChar - the character to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the character and the current total
    */
   public HashUtil2 hash(char aChar)
   {
      hashVal = strategy.hashValue((int) aChar, hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from a long.
    * <p>The long is transformed with a simple cast to a int. 
    * Then it is hashed as a int.</p>
    * @param num - the integer to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the long and the current total
    */
   public HashUtil2 hash(long num)
   {
      int l = ((int)(num >> 32)) ^ (int) num; //does an XOR of the top 32 bits with the bottom 32 bits.  Thus we don't actually lose any information.
      hashVal = strategy.hashValue(l, hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from an integer.
    * @param num - the int to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the int and the current total
    */
   public HashUtil2 hash(int num)
   {
      hashVal = strategy.hashValue(num, hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from a float.
    * <p>The float is transformed with {@code Float.floatToIntBits()}, 
    * which is then cast to a int. Then it is hashed as a int.</p>
    * @param num - the float to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the float and the current total
    */
   public HashUtil2 hash(float num)
   {
      int l = Float.floatToIntBits(num);
      hashVal = strategy.hashValue(l, hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from a double.
    * <p>The double is transformed with {@code Double.doubleTointBits()}, 
    * which is then cast to a int. Then it is hashed as a int.</p>
    * @param num - the double to be hashed
    * @param currTotal - the current total used in the calculation
    * @return the hash code of the double and the current total
    */
   public HashUtil2 hash(double num)
   {
      hashVal = hash(Double.doubleToLongBits(num), hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from an Object.
    * <p>The Object is transformed by calling its {@code hashCode()} method, 
    * then casting it to a int.  Then it is hashed as a int.</p>
    * @param obj - the Object to be hashed
    * @return the hash code of the Object and the current total
    */
   public HashUtil2 hash(Object obj)
   {
      hashVal = strategy.hashValue(obj.hashCode(), hashVal);
      return this;
   }
   
   /**
    * Creates a hash value from an {@code Iterable} collection.
    * <p>Iterates through the collection, getting the object's hashCode() value
    * and runs it through the hashing technique.</p>
    * @param iter - the {@code Iterable} to iterate through to hash
    * @return the hash code of the of {@code Iterable} and the current total
    */
   public <T> HashUtil2 hash(Iterable<T> iter)
   {
      for(T obj : iter)
      {
	     hash(obj);
      }
      return this;
   }
}
