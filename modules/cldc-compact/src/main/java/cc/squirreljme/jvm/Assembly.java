// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.summercoat.SystemCall;

/**
 * This class is used special by the compiler to transform all the various
 * operations into regular instructions rather than method calls.
 *
 * The compiler will take all of the method arguments and instead use their
 * inputs and outputs from the values of registers instead. However due to
 * this, this means that these instructions are purely primitive in that
 * they must not depend on any aspect of the virtual machine.
 *
 * @since 2019/04/20
 */
@SuppressWarnings("NewMethodNamingConvention")
public final class Assembly
{
	/**
	 * Not used.
	 *
	 * @since 2019/04/20
	 */
	private Assembly()
	{
	}
	
	/**
	 * Returns the array length of the given object.
	 *
	 * @param __o The object to get the length of.
	 * @return The length of the array, will return a negative value if not
	 * an array.
	 * @since 2020/02/23
	 */
	public static native int arrayLength(long __o);
	
	/**
	 * Returns the array length of the given object.
	 *
	 * @param __o The object to get the length of.
	 * @return The length of the array, will return a negative value if not
	 * an array.
	 * @since 2019/05/24
	 */
	public static native int arrayLength(Object __o);
	
	/**
	 * Sets the array length of an array.
	 *
	 * @param __o The object to set.
	 * @param __l The length to set.
	 * @since 2020/02/23
	 */
	public static native void arrayLengthSet(long __o, int __l);
	
	/**
	 * Sets the array length of an array.
	 *
	 * @param __o The object to set.
	 * @param __l The length to set.
	 * @since 2020/02/23
	 */
	public static native void arrayLengthSet(Object __o, int __l);
	
	/**
	 * Atomic comparison and set.
	 *
	 * @param __comp The value to compare and if matches, {@code __set} is
	 * written.
	 * @param __set The value to set if matched.
	 * @param __addr The address to write to.
	 * @return The value that was read before the set.
	 * @since 2019/07/01
	 */
	public static native int atomicCompareGetAndSet(int __comp, int __set,
		long __addr);
	
	/**
	 * Atomically decrements a value and returns the result.
	 *
	 * @param __addr The address to decrement.
	 * @return The get value.
	 * @since 2019/07/01
	 */
	public static native int atomicDecrementAndGet(long __addr);
	
	/**
	 * Atomically increments a value.
	 *
	 * @param __addr The address to increment.
	 * @since 2019/07/01
	 */
	public static native void atomicIncrement(long __addr);
	
	/**
	 * Trigger breakpoint within the virtual machine.
	 *
	 * @since 2019/04/21
	 */
	public static native void breakpoint();
	
	/**
	 * Returns the class info pointer of {@code boolean}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfBoolean();
	
	/**
	 * Returns the class info pointer of {@code boolean}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfBooleanPointer();
	
	/**
	 * Returns the class info pointer of {@code byte}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfByte();
	
	/**
	 * Returns the class info pointer of {@code byte}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfBytePointer();
	
	/**
	 * Returns the class info pointer of {@code char}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfCharacter();
	
	/**
	 * Returns the class info pointer of {@code char}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfCharacterPointer();
	
	/**
	 * Returns the class info pointer of {@code double}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfDouble();
	
	/**
	 * Returns the class info pointer of {@code double}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfDoublePointer();
	
	/**
	 * Returns the class info pointer of {@code float}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfFloat();
	
	/**
	 * Returns the class info pointer of {@code float}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfFloatPointer();
	
	/**
	 * Returns the class info pointer of {@code int}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfInteger();
	
	/**
	 * Returns the class info pointer of {@code int}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfIntegerPointer();
	
	/**
	 * Returns the class info pointer of {@code long}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfLong();
	
	/**
	 * Returns the class info pointer of {@code long}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfLongPointer();
	
	/**
	 * Returns the class info pointer of {@code short}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native ClassInfo classInfoOfShort();
	
	/**
	 * Returns the class info pointer of {@code short}.
	 *
	 * @return The class info pointer.
	 * @since 2020/02/24
	 */
	public static native long classInfoOfShortPointer();
	
	/**
	 * Packs the given two integers to a double value.
	 *
	 * @param __hi The high value.
	 * @param __lo The low value.
	 * @return The double value.
	 * @since 2019/06/21
	 */
	public static native double doublePack(int __hi, int __lo);
	
	/**
	 * Double to raw long bits.
	 *
	 * @param __d The input double.
	 * @return The raw long bits.
	 * @since 2018/11/03
	 */
	public static native long doubleToRawLongBits(double __d);
	
	/**
	 * Unpacks the high value of a double.
	 *
	 * @param __d The double to unpack.
	 * @return The unpacked high value.
	 * @since 2020/02/24
	 */
	public static native int doubleUnpackHigh(double __d);
	
	/**
	 * Unpacks the low value of a double.
	 *
	 * @param __d The double to unpack.
	 * @return The unpacked low value.
	 * @since 2020/02/24
	 */
	public static native int doubleUnpackLow(double __d);
	
	/**
	 * Performs explicit exception handling.
	 *
	 * @since 2019/04/28
	 */
	public static native void exceptionHandle();
	
	/**
	 * Float to raw int bits.
	 *
	 * @param __f The input float.
	 * @return The raw int bits.
	 * @since 2018/11/04
	 */
	public static native int floatToRawIntBits(float __f);
	
	/**
	 * Integer bits to float.
	 *
	 * @param __b The input bits.
	 * @return The resulting float.
	 * @since 2018/11/04
	 */
	public static native float intBitsToFloat(int __b);
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c, int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c, int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(long __addr, long __pool, int __a,
		int __b, int __c, int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a,
		int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a,
		int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a, 
		int __b, int __c, int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a, 
		int __b, int __c, int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a, 
		int __b, int __c, int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool address to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a, 
		int __b, int __c, int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __pool The pool pointer to load.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(long __addr, long __pool, int __a, 
		int __b, int __c, int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Long bits to double.
	 *
	 * @param __b The input bits.
	 * @return The resulting double.
	 * @since 2018/11/03
	 */
	public static native double longBitsToDouble(long __b);
	
	/**
	 * Packs the given two integers to a long value.
	 *
	 * @param __hi The high value.
	 * @param __lo The low value.
	 * @return The long value.
	 * @since 2019/06/21
	 */
	public static native long longPack(int __hi, int __lo);
	
	/**
	 * Unpack high value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	public static native int longUnpackHigh(long __v);
	
	/**
	 * Unpack low value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	public static native int longUnpackLow(long __v);
	
	/**
	 * Reads byte from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static native int memReadByte(long __p, int __o);
	
	/**
	 * Reads integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/21
	 */
	public static native int memReadInt(long __p, int __o);
	
	/**
	 * Reads big endian Java integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static native int memReadJavaInt(long __p, int __o);
	
	/**
	 * Reads big endian Java long from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2020/02/24
	 */
	public static native long memReadJavaLong(long __p, int __o);
	
	/**
	 * Reads big endian Java short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static native int memReadJavaShort(long __p, int __o);
	
	/**
	 * Reads pointer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static native long memReadPointer(long __p, int __o);
	
	/**
	 * Reads short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static native int memReadShort(long __p, int __o);
	
	/**
	 * Writes byte to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteByte(long __p, int __o, int __v);
	
	/**
	 * Writes integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteInt(long __p, int __o, int __v);
	
	/**
	 * Writes big endian Java integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static native void memWriteJavaInt(long __p, int __o, int __v);
	
	/**
	 * Writes big endian Java long to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2020/02/24
	 */
	public static native void memWriteJavaLong(long __p, int __o, long __v);
	
	/**
	 * Writes big endian Java short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static native void memWriteJavaShort(long __p, int __o, int __v);
	
	/**
	 * Writes a pointer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2020/02/24
	 */
	public static native void memWritePointer(long __p, int __o, long __v);
	
	/**
	 * Writes short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteShort(long __p, int __o, int __v);
	
	/**
	 * Atomically decrements an object's monitor count.
	 * 
	 * @param __p The object to count.
	 * @return The resulting value.
	 * @since 2020/02/24
	 */
	public static native int monitorCountDecrementAndGetAtomic(long __p);
	
	/**
	 * Atomically decrements an object's monitor count.
	 * 
	 * @param __p The object to count.
	 * @return The resulting value.
	 * @since 2020/02/24
	 */
	public static native int monitorCountDecrementAndGetAtomic(Object __p);
	
	/**
	 * Atomically increments an object's monitor count.
	 * 
	 * @param __p The object to count.
	 * @return The resulting value.
	 * @since 2020/02/24
	 */
	public static native int monitorCountIncrementAndGetAtomic(long __p);
	
	/**
	 * Atomically increments an object's monitor count.
	 * 
	 * @param __p The object to count.
	 * @return The resulting value.
	 * @since 2020/02/24
	 */
	public static native int monitorCountIncrementAndGetAtomic(Object __p);
	
	/**
	 * Atomically reads the object's monitor count.
	 * 
	 * @param __p The object to read.
	 * @return The current monitor count.
	 * @since 2020/02/24
	 */
	public static native int monitorCountGetAtomic(long __p);
	
	/**
	 * Atomically reads the object's monitor count.
	 * 
	 * @param __p The object to read.
	 * @return The current monitor count.
	 * @since 2020/02/24
	 */
	public static native int monitorCountGetAtomic(Object __p);
	
	/**
	 * Atomically writes the object's monitor count.
	 * 
	 * @param __p The object to write.
	 * @param __v The count to write.
	 * @since 2020/02/24
	 */
	public static native void monitorCountSetAtomic(long __p, int __v);
	
	/**
	 * Atomically writes the object's monitor count.
	 * 
	 * @param __p The object to write.
	 * @param __v The count to write.
	 * @since 2020/02/24
	 */
	public static native void monitorCountSetAtomic(Object __p, int __v);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerCompareGetAndSetAtomic(
		long __p, long __comp, long __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerCompareGetAndSetAtomic(
		Object __p, long __comp, long __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerCompareGetAndSetAtomic(
		long __p, Thread __comp, Thread __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerCompareGetAndSetAtomic(
		Object __p, Thread __comp, Thread __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerCompareGetAndSetAtomicPointer(
		long __p, long __comp, long __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerCompareGetAndSetAtomicPointer(
		Object __p, long __comp, long __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerCompareGetAndSetAtomicPointer(
		long __p, Thread __comp, Thread __set);
	
	/**
	 * Atomically compares the owner of the object's monitor and sets it,
	 * returning the value before it was set (or was unchanged).
	 * 
	 * @param __p The object to potentially modify.
	 * @param __comp The owner to compare against.
	 * @param __set The owner to set to, if the comparison succeeds.
	 * @return The owner that originally was set.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerCompareGetAndSetAtomicPointer(
		Object __p, Thread __comp, Thread __set);
	
	/**
	 * Returns the owner of an object's monitor.
	 * 
	 * @param __p The object to get the owner from.
	 * @return The owner of the object's monitor.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerGetAtomic(long __p);
	
	/**
	 * Returns the owner of an object's monitor.
	 * 
	 * @param __p The object to get the owner from.
	 * @return The owner of the object's monitor.
	 * @since 2020/02/24
	 */
	public static native Thread monitorOwnerGetAtomic(Object __p);
	
	/**
	 * Returns the owner of an object's monitor.
	 * 
	 * @param __p The object to get the owner from.
	 * @return The owner of the object's monitor.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerGetPointerAtomic(long __p);
	
	/**
	 * Returns the owner of an object's monitor.
	 * 
	 * @param __p The object to get the owner from.
	 * @return The owner of the object's monitor.
	 * @since 2020/02/24
	 */
	public static native long monitorOwnerGetPointerAtomic(Object __p);
	
	/**
	 * Sets the owner of an object's monitor.
	 * 
	 * @param __p The object which will be set the new owner.
	 * @param __t The owner to set.
	 * @since 2020/02/24
	 */
	public static native void monitorOwnerSetAtomic(long __p, long __t);
	
	/**
	 * Sets the owner of an object's monitor.
	 * 
	 * @param __p The object which will be set the new owner.
	 * @param __t The owner to set.
	 * @since 2020/02/24
	 */
	public static native void monitorOwnerSetAtomic(long __p, Thread __t);
	
	/**
	 * Sets the owner of an object's monitor.
	 * 
	 * @param __p The object which will be set the new owner.
	 * @param __t The owner to set.
	 * @since 2020/02/24
	 */
	public static native void monitorOwnerSetAtomic(Object __p, long __t);
	
	/**
	 * Sets the owner of an object's monitor.
	 * 
	 * @param __p The object which will be set the new owner.
	 * @param __t The owner to set.
	 * @since 2020/02/24
	 */
	public static native void monitorOwnerSetAtomic(Object __p, Thread __t);
	
	/**
	 * Gets the ClassInfo of an object.
	 *
	 * @param __o The object to read from.
	 * @return The resulting class info.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native ClassInfo objectGetClassInfo(long __o);
	
	/**
	 * Gets the ClassInfo of an object.
	 *
	 * @param __o The object to read from.
	 * @return The resulting class info.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native ClassInfo objectGetClassInfo(Object __o);
	
	/**
	 * Gets the ClassInfo of an object.
	 *
	 * @param __o The object to read from.
	 * @return The resulting class info.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native long objectGetClassInfoPointer(long __o);
	
	/**
	 * Gets the ClassInfo of an object.
	 *
	 * @param __o The object to read from.
	 * @return The resulting class info.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native long objectGetClassInfoPointer(Object __o);
	
	/**
	 * Sets the ClassInfo of an object.
	 *
	 * @param __o The object to set the class of.
	 * @param __v The class info to set.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native void objectSetClassInfo(long __o, long __v);
	
	/**
	 * Sets the ClassInfo of an object.
	 *
	 * @param __o The object to set the class of.
	 * @param __v The class info to set.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native void objectSetClassInfo(Object __o, long __v);
	
	/**
	 * Sets the ClassInfo of an object.
	 *
	 * @param __o The object to set the class of.
	 * @param __v The class info to set.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native void objectSetClassInfo(long __o, ClassInfo __v);
	
	/**
	 * Sets the ClassInfo of an object.
	 *
	 * @param __o The object to set the class of.
	 * @param __v The class info to set.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2020/02/23
	 */
	@Deprecated
	public static native void objectSetClassInfo(Object __o, ClassInfo __v);
	
	/**
	 * Used to convert an object to a pointer.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @since 2020/11/29
	 */
	public static native int objectToPointer(Object __o);
	
	/**
	 * Used to convert an object to a pointer.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @deprecated Wide pointers are going away.
	 * @since 2019/04/21
	 */
	@Deprecated
	public static native long objectToPointerWide(Object __o);
	
	/**
	 * Used to convert an object to a pointer, do use reference queing for it
	 * so that if the object is a candidate for reference counting it will
	 * be uncounted.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @since 2019/04/21
	 */
	public static native int objectToPointerRefQueue(Object __o);
	
	/**
	 * Used to convert an object to a pointer, do use reference queing for it
	 * so that if the object is a candidate for reference counting it will
	 * be uncounted.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @deprecated Wide pointers are going away.
	 * @since 2019/04/21
	 */
	@Deprecated
	public static native long objectToPointerRefQueueWide(Object __o);
	
	/**
	 * Used to convert a pointer to an object.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @since 2019/04/21
	 */
	public static native Object pointerToObject(int __p);
	
	/**
	 * Used to convert a pointer to an object.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @deprecated Wide pointers are going away.
	 * @since 2019/04/21
	 */
	@Deprecated
	public static native Object pointerToObject(long __p);
	
	/**
	 * Used to convert a pointer to a class info type.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @deprecated {@link ClassInfo} is going away.
	 * @since 2019/04/21
	 */
	@Deprecated
	public static native ClassInfo pointerToClassInfo(long __p);
	
	/**
	 * Loads a value from the constant pool at the given index.
	 *
	 * @param __p The memory address of the pool to access.
	 * @param __i The index to load.
	 * @return The read value, this may be truncated to 32-bits on 32-bit
	 * systems.
	 * @since 2020/02/24
	 */
	public static native long poolLoad(long __p, int __i);
	
	/**
	 * Loads a value from the constant pool at the given index.
	 *
	 * @param __p The object representation of the pool to access.
	 * @param __i The index to load.
	 * @return The read value, this may be truncated to 32-bits on 32-bit
	 * systems
	 * @since 2020/02/24
	 */
	public static native long poolLoad(Object __p, int __i);
	
	/**
	 * Writes a value to the constant pool of a class.
	 * 
	 * @param __p The address of the constant pool.
	 * @param __i The index to write.
	 * @param __v The value to write, note that on 32-bit .
	 * @since 2019/02/24
	 */
	public static native void poolStore(long __p, int __i, long __v);
	
	/**
	 * Writes a value to the constant pool of a class.
	 * 
	 * @param __p The object representation of the pool to access.
	 * @param __i The index to write.
	 * @param __v The value to write, note that on 32-bit .
	 * @since 2019/02/24
	 */
	public static native void poolStore(Object __p, int __i, long __v);
	
	/**
	 * Perform reference counting logic on object.
	 *
	 * @param __p The object to count up.
	 * @since 2019/05/25
	 */
	public static native void refCount(int __p);
	
	/**
	 * Perform reference counting logic on object.
	 *
	 * @param __p The object to count up.
	 * @since 2020/02/24
	 */
	public static native void refCount(Object __p);
	
	/**
	 * Get reference count of object.
	 * 
	 * @param __p The object to get the count for.
	 * @return The reference count of the object.
	 * @since 2020/02/24
	 */
	public static native int refGetCount(int __p);
	
	/**
	 * Get reference count of object.
	 * 
	 * @param __p The object to get the count for.
	 * @return The reference count of the object.
	 * @since 2020/02/24
	 */
	public static native int refGetCount(Object __p);
	
	/**
	 * Set reference count of object.
	 * 
	 * @param __p The object to set the count for.
	 * @param __v The value to set.
	 * @since 2020/02/24
	 */
	public static native void refSetCount(int __p, int __v);
	
	/**
	 * Set reference count of object.
	 * 
	 * @param __p The object to set the count for.
	 * @param __v The value to set.
	 * @since 2020/02/24
	 */
	public static native void refSetCount(Object __p, int __v);
	
	/**
	 * Perform reference uncounting logic on object.
	 *
	 * @param __p The object to count down.
	 * @since 2019/05/25
	 */
	public static native void refUncount(int __p);
	
	/**
	 * Perform reference uncounting logic on object.
	 *
	 * @param __p The object to count down.
	 * @since 2020/02/24
	 */
	public static native void refUncount(Object __p);
	
	/**
	 * Return from the current frame.
	 *
	 * @since 2019/04/21
	 */
	public static native void returnFrame();
	
	/**
	 * Returns from the current frame, returning the given value.
	 *
	 * @param __v The value to return.
	 * @since 2019/04/28
	 */
	public static native void returnFrame(int __v);
	
	/**
	 * Returns from the current frame, returning both values.
	 *
	 * @param __h The high value.
	 * @param __l The low value.
	 * @since 2019/04/28
	 */
	public static native void returnFrame(int __h, int __l);
	
	/**
	 * Returns from the current frame, returning a long value.
	 *
	 * @param __v The value.
	 * @since 2020/02/24
	 */
	public static native void returnFrameLong(long __v);
	
	/**
	 * Returns the size of base arrays.
	 * 
	 * @return The base array size.
	 * @since 2020/02/24
	 */
	public static native int sizeOfBaseArray();
	
	/**
	 * Returns the size of base objects.
	 * 
	 * @return The base object size.
	 * @since 2020/02/24
	 */
	public static native int sizeOfBaseObject();
	
	/**
	 * Returns the size of pointers and object references.
	 * 
	 * @return The pointer size.
	 * @since 2020/02/24
	 */
	public static native int sizeOfPointer();
	
	/**
	 * Returns the exception register.
	 *
	 * @return The exception register.
	 * @since 2020/02/24
	 */
	public static native Object specialGetExceptionRegister();
	
	/**
	 * Returns the exception register.
	 *
	 * @return The exception register.
	 * @since 2020/02/24
	 */
	public static native Throwable specialGetExceptionRegisterThrowable();
	
	/**
	 * Returns the exception register.
	 *
	 * @return The exception register.
	 * @since 2019/04/28
	 */
	public static native long specialGetExceptionRegisterPointer();
	
	/**
	 * Returns the value of the current pool register.
	 *
	 * @return The value of the pool register.
	 * @since 2020/02/24
	 */
	public static native Object specialGetPoolRegister();
	
	/**
	 * Returns the value of the current pool register.
	 *
	 * @return The value of the pool register.
	 * @since 2019/05/01
	 */
	public static native long specialGetPoolRegisterPointer();
	
	/**
	 * Returns the value of the return register, for long return values this
	 * is the first high register.
	 *
	 * @return The value of the return register.
	 * @since 2019/04/28
	 */
	public static native int specialGetReturnRegister();
	
	/**
	 * Reads the long value from the return register, treating it as a long
	 * value.
	 *
	 * @return The value of the return register.
	 * @since 2020/02/24
	 */
	public static native long specialGetReturnRegisterLong();
	
	/**
	 * Reads the value of the static field register.
	 *
	 * @return The value of the static field register.
	 * @since 2019/04/22
	 */
	public static native long specialGetStaticFieldRegister();
	
	/**
	 * Returns the register representing the current thread.
	 *
	 * @return The current thread register.
	 * @since 2019/04/22
	 */
	public static native Thread specialGetThreadRegister();
	
	/**
	 * Returns the register representing the current thread.
	 *
	 * @return The current thread register.
	 * @since 2020/02/24
	 */
	public static native long specialGetThreadRegisterPointer();
	
	/**
	 * Sets the value of the exception register.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/28
	 */
	public static native void specialSetExceptionRegister(long __v);
	
	/**
	 * Sets the value of the exception register.
	 *
	 * @param __v The value to use.
	 * @since 2020/02/24
	 */
	public static native void specialSetExceptionRegister(Object __v);
	
	/**
	 * Sets the value of the constant pool register.
	 *
	 * @param __v The new value of the constant pool register.
	 * @since 2019/05/01
	 */
	public static native void specialSetPoolRegister(long __v);
	
	/**
	 * Sets the value of the constant pool register.
	 *
	 * @param __v The new value of the constant pool register.
	 * @since 2020/02/24
	 */
	public static native void specialSetPoolRegister(Object __v);
	
	/**
	 * Sets the value of the static field register.
	 *
	 * @param __v The new value of the static field register.
	 * @since 2019/04/22
	 */
	public static native void specialSetStaticFieldRegister(long __v);
	
	/**
	 * Sets the current thread pointer.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/27
	 */
	public static native void specialSetThreadRegister(long __v);
	
	/**
	 * Sets the current thread pointer.
	 *
	 * @param __v The value to use.
	 * @since 2020/02/24
	 */
	public static native void specialSetThreadRegister(Thread __v);
}
