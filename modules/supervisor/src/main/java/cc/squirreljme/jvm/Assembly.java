// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

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
	 * @return The length of the array.
	 * @since 2019/05/24
	 */
	public static native int arrayLength(Object __o);
	
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
		int __addr);
	
	/**
	 * Atomically decrements a value and returns the result.
	 *
	 * @param __addr The address to decrement.
	 * @return The get value.
	 * @since 2019/07/01
	 */
	public static native int atomicDecrementAndGet(int __addr);
	
	/**
	 * Atomically increments a value.
	 *
	 * @param __addr The address to increment.
	 * @since 2019/07/01
	 */
	public static native void atomicIncrement(int __addr);
	
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
	public static native int classInfoOfBoolean();
	
	/**
	 * Returns the class info pointer of {@code byte}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfByte();
	
	/**
	 * Returns the class info pointer of {@code char}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfCharacter();
	
	/**
	 * Returns the class info pointer of {@code double}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfDouble();
	
	/**
	 * Returns the class info pointer of {@code float}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfFloat();
	
	/**
	 * Returns the class info pointer of {@code int}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfInteger();
	
	/**
	 * Returns the class info pointer of {@code long}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfLong();
	
	/**
	 * Returns the class info pointer of {@code short}.
	 *
	 * @return The class info pointer.
	 * @since 2020/01/19
	 */
	public static native int classInfoOfShort();
	
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
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @since 2019/04/28
	 */
	public static native void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
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
	public static native void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a, int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a, int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static native int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
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
	public static native int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
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
	public static native int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a,
		int __b);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a,
		int __b, int __c);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a, 
		int __b, int __c, int __d);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a, 
		int __b, int __c, int __d, int __e);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/12/08
	 */
	public static native long invokeVL(int __addr, int __pool, int __a, 
		int __b, int __c, int __d, int __e, int __f);
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
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
	public static native long invokeVL(int __addr, int __pool, int __a, 
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
	public static native long invokeVL(int __addr, int __pool, int __a, 
		int __b, int __c, int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Loads a value from the constant pool at the given index.
	 *
	 * @return The index of the value in the constant pool.
	 * @since 2019/04/28
	 */
	public static native int loadPool(int __i);
	
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
	public static native int memReadByte(int __p, int __o);
	
	/**
	 * Reads integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/21
	 */
	public static native int memReadInt(int __p, int __o);
	
	/**
	 * Reads big endian Java integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static native int memReadJavaInt(int __p, int __o);
	
	/**
	 * Reads big endian Java short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static native int memReadJavaShort(int __p, int __o);
	
	/**
	 * Reads short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static native int memReadShort(int __p, int __o);
	
	/**
	 * Writes byte to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteByte(int __p, int __o, int __v);
	
	/**
	 * Writes integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteInt(int __p, int __o, int __v);
	
	/**
	 * Writes big endian Java integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static native void memWriteJavaInt(int __p, int __o, int __v);
	
	/**
	 * Writes big endian Java short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static native void memWriteJavaShort(int __p, int __o, int __v);
	
	/**
	 * Writes short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static native void memWriteShort(int __p, int __o, int __v);
	
	/**
	 * Used to convert an object to a pointer.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @since 2019/04/21
	 */
	public static native int objectToPointer(Object __o);
	
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
	 * Used to convert a pointer to an object.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @since 2019/04/21
	 */
	public static native Object pointerToObject(int __p);
	
	/**
	 * Used to convert a pointer to a class info type.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @since 2019/04/21
	 */
	public static native ClassInfo pointerToClassInfo(int __p);
	
	/**
	 * Perform reference counting logic on object.
	 *
	 * @param __p The object to count up.
	 * @since 2019/05/25
	 */
	public static native void refCount(int __p);
	
	/**
	 * Perform reference uncounting logic on object.
	 *
	 * @param __p The object to count down.
	 * @since 2019/05/25
	 */
	public static native void refUncount(int __p);
	
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
	 * Returns the exception register.
	 *
	 * @return The exception register.
	 * @since 2019/04/28
	 */
	public static native int specialGetExceptionRegister();
	
	/**
	 * Returns the value of the current pool register.
	 *
	 * @return The value of the pool register.
	 * @since 2019/05/01
	 */
	public static native int specialGetPoolRegister();
	
	/**
	 * Returns the value of the return register, for long return values this
	 * is the first high register.
	 *
	 * @return The value of the return register.
	 * @since 2019/04/28
	 */
	public static native int specialGetReturnRegister();
	
	/**
	 * Returns the value of the return register, the first high value.
	 *
	 * @return The value of the return register, the first high value.
	 * @since 2019/04/28
	 */
	public static native int specialGetReturnHighRegister();
	
	/**
	 * Returns the value of the return register, the second low value.
	 *
	 * @return The value of the return register, the second low value.
	 * @since 2019/04/28
	 */
	public static native int specialGetReturnLowRegister();
	
	/**
	 * Reads the value of the static field register.
	 *
	 * @return The value of the static field register.
	 * @since 2019/04/22
	 */
	public static native int specialGetStaticFieldRegister();
	
	/**
	 * Returns the register representing the current thread.
	 *
	 * @return The current thread register.
	 * @since 2019/04/22
	 */
	public static native int specialGetThreadRegister();
	
	/**
	 * Sets the value of the exception register.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/28
	 */
	public static native void specialSetExceptionRegister(int __v);
	
	/**
	 * Sets the value of the constant pool register.
	 *
	 * @param __v The new value of the constant pool register.
	 * @since 2019/05/01
	 */
	public static native void specialSetPoolRegister(int __v);
	
	/**
	 * Sets the value of the static field register.
	 *
	 * @param __v The new value of the static field register.
	 * @since 2019/04/22
	 */
	public static native void specialSetStaticFieldRegister(int __v);
	
	/**
	 * Sets the current thread pointer.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/27
	 */
	public static native void specialSetThreadRegister(int __v);
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __si The address to invoke.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @since 2019/05/23
	 */
	public static native void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke pure system call at the given index.
	 *
	 * @param __si The address to invoke.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @since 2019/05/27
	 */
	public static native void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke pure system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
		
	/**
	 * Invoke pure system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke pure system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/27
	 */
	public static native long sysCallPVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
		
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c,
		int __d);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c,
		int __d, int __e);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g);
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static native long sysCallVL(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h);
}
