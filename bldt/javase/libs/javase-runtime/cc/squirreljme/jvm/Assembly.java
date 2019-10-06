// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.lang.ApiLevel;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

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
	/** Errors used in system calls. */
	private static final int[] _ERRORS =
		new int[SystemCallIndex.NUM_SYSCALLS];
	
	/** Unique string map. */
	private static final Map<String, Integer> _STRINGS =
		new HashMap<>();
	
	/** Unique objects. */
	private static final Map<Object, Integer> _OBJECTS =
		new IdentityHashMap<>();
	
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
	public static final int arrayLength(Object __o)
	{
		return Array.getLength(__o);
	}
	
	/**
	 * Trigger breakpoint within the virtual machine.
	 *
	 * @since 2019/04/21
	 */
	public static final void breakpoint()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Packs the given two integers to a double value.
	 *
	 * @param __hi The high value.
	 * @param __lo The low value.
	 * @return The double value.
	 * @since 2019/06/21
	 */
	public static final double doublePack(int __hi, int __lo)
	{
		return Assembly.longBitsToDouble(Assembly.longPack(__hi, __lo));
	}
	
	/**
	 * Double to raw long bits.
	 *
	 * @param __d The input double.
	 * @return The raw long bits.
	 * @since 2018/11/03
	 */
	public static final long doubleToRawLongBits(double __d)
	{
		return Double.doubleToRawLongBits(__d);
	}
	
	/**
	 * Performs explicit exception handling.
	 *
	 * @since 2019/04/28
	 */
	public static final void exceptionHandle()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Float to raw int bits.
	 *
	 * @param __d The input float.
	 * @return The raw int bits.
	 * @since 2018/11/04
	 */
	public static final int floatToRawIntBits(float __f)
	{
		return Float.floatToRawIntBits(__f);
	}
	
	/**
	 * Integer bits to float.
	 *
	 * @param __b The input bits.
	 * @return The resulting float.
	 * @since 2018/11/04
	 */
	public static final float intBitsToFloat(int __b)
	{
		return Float.intBitsToFloat(__b);
	}
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @since 2019/04/28
	 */
	public static final void invoke(int __addr)
	{
		Assembly.__invoke(__addr);
	}
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @since 2019/04/28
	 */
	public static final void invoke(int __addr, int __a)
	{
		Assembly.__invoke(__addr, __a);
	}
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/04/28
	 */
	public static final void invoke(int __addr, int __a, int __b)
	{
		Assembly.__invoke(__addr, __a, __b);
	}
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/04/28
	 */
	public static final void invoke(int __addr, int __a, int __b, int __c)
	{
		Assembly.__invoke(__addr, __a, __b, __c);
	}
	
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
	public static final void invoke(int __addr, int __a, int __b, int __c,
		int __d)
	{
		Assembly.__invoke(__addr, __a, __b, __c, __d);
	}
	
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
	public static final void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e)
	{
		Assembly.__invoke(__addr, __a, __b, __c, __d, __e);
	}
	
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
	public static final void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final void invoke(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * Invoke method at pointer.
	 *
	 * @param __addr The address to invoke.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static final int invokeV(int __addr)
	{
		return Assembly.__invoke(__addr);
	}
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static final int invokeV(int __addr, int __a)
	{
		return Assembly.__invoke(__addr, __a);
	}
	
	/**
	 * Invoke method at pointer, with arguments.
	 *
	 * @param __addr The address to invoke.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/04/28
	 */
	public static final int invokeV(int __addr, int __a, int __b)
	{
		return Assembly.__invoke(__addr, __a, __b);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c)
	{
		return Assembly.__invoke(__addr, __a, __b, __c);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c,
		int __d)
	{
		return Assembly.__invoke(__addr, __a, __b, __c, __d);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e)
	{
		return Assembly.__invoke(__addr, __a, __b, __c, __d, __e);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		return Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		return Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final int invokeV(int __addr, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		return Assembly.__invoke(__addr, __a, __b, __c, __d, __e, __f, __g,
			__h);
	}
	
	/**
	 * Long bits to double.
	 *
	 * @param __b The input bits.
	 * @return The resulting double.
	 * @since 2018/11/03
	 */
	public static final double longBitsToDouble(long __b)
	{
		return Double.longBitsToDouble(__b);
	}
	
	/**
	 * Packs the given two integers to a long value.
	 *
	 * @param __hi The high value.
	 * @param __lo The low value.
	 * @return The long value.
	 * @since 2019/06/21
	 */
	public static final long longPack(int __hi, int __lo)
	{
		return ((((long)__hi) << 32L) | (((long)__lo) & 0xFFFFFFFFL));
	}
	
	/**
	 * Unpack high value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	public static final int longUnpackHigh(long __v)
	{
		return (int)(__v >>> 32);
	}
	
	/**
	 * Unpack low value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	public static final int longUnpackLow(long __v)
	{
		return (int)__v;
	}
	
	/**
	 * Reads byte from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static final int memReadByte(int __p, int __o)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Reads integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/21
	 */
	public static final int memReadInt(int __p, int __o)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Reads big endian Java integer from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static final int memReadJavaInt(int __p, int __o)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Reads big endian Java short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/05/29
	 */
	public static final int memReadJavaShort(int __p, int __o)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Reads short from address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @return The result of the read.
	 * @since 2019/04/22
	 */
	public static final int memReadShort(int __p, int __o)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Writes byte to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static final void memWriteByte(int __p, int __o, int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Writes integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static final void memWriteInt(int __p, int __o, int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Writes big endian Java integer to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static final void memWriteJavaInt(int __p, int __o, int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Writes big endian Java short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/05/29
	 */
	public static final void memWriteJavaShort(int __p, int __o, int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Writes short to address.
	 *
	 * @param __p The pointer.
	 * @param __o The offset.
	 * @param __v The value to write.
	 * @since 2019/04/21
	 */
	public static final void memWriteShort(int __p, int __o, int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Used to convert an object to a pointer.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @since 2019/04/21
	 */
	public static final int objectToPointer(Object __o)
	{
		return Assembly.__uniqueObjectId(__o);
	}
	
	/**
	 * Used to convert an object to a pointer, do use reference queing for it
	 * so that if the object is a candidate for reference counting it will
	 * be uncounted.
	 *
	 * @param __o The object.
	 * @return The pointer of the object.
	 * @since 2019/04/21
	 */
	public static final int objectToPointerRefQueue(Object __o)
	{
		return Assembly.__uniqueObjectId(__o);
	}
	
	/**
	 * Used to convert a pointer to an object.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @since 2019/04/21
	 */
	public static final Object pointerToObject(int __p)
	{
		return Assembly.__uniqueObject(__p);
	}
	
	/**
	 * Used to convert a pointer to a class info type.
	 *
	 * @param __p The pointer.
	 * @return The object of the pointer.
	 * @since 2019/04/21
	 */
	public static final ClassInfo pointerToClassInfo(int __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Perform reference counting logic on object.
	 *
	 * @param __p The object to count up.
	 * @since 2019/05/25
	 */
	public static final void refCount(int __p)
	{
		// No work needed
	}
	
	/**
	 * Perform reference uncounting logic on object.
	 *
	 * @param __p The object to count down.
	 * @since 2019/05/25
	 */
	public static final void refUncount(int __p)
	{
		// No work needed
	}
	
	/**
	 * Return from the current frame.
	 *
	 * @since 2019/04/21
	 */
	public static final void returnFrame()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns from the current frame, returning the given value.
	 *
	 * @param __v The value to return.
	 * @since 2019/04/28
	 */
	public static final void returnFrame(int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns from the current frame, returning both values.
	 *
	 * @param __h The high value.
	 * @param __l The low value.
	 * @since 2019/04/28
	 */
	public static final void returnFrame(int __h, int __l)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the exception register.
	 *
	 * @return The exception register.
	 * @since 2019/04/28
	 */
	public static final int specialGetExceptionRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the value of the current pool register.
	 *
	 * @return The value of the pool register.
	 * @since 2019/05/01
	 */
	public static final int specialGetPoolRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the value of the return register, for long return values this
	 * is the first high register.
	 *
	 * @return The value of the return register.
	 * @since 2019/04/28
	 */
	public static final int specialGetReturnRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the value of the return register, the first high value.
	 *
	 * @return The value of the return register, the first high value.
	 * @since 2019/04/28
	 */
	public static final int specialGetReturnHighRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the value of the return register, the second low value.
	 *
	 * @return The value of the return register, the second low value.
	 * @since 2019/04/28
	 */
	public static final int specialGetReturnLowRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Reads the value of the static field register.
	 *
	 * @return The value of the static field register.
	 * @since 2019/04/22
	 */
	public static final int specialGetStaticFieldRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Returns the register representing the current thread.
	 *
	 * @return The current thread register.
	 * @since 2019/04/22
	 */
	public static final int specialGetThreadRegister()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Sets the value of the exception register.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/28
	 */
	public static final void specialSetExceptionRegister(int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Sets the value of the constant pool register.
	 *
	 * @param __v The new value of the constant pool register.
	 * @since 2019/05/01
	 */
	public static final void specialSetPoolRegister(int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Sets the value of the static field register.
	 *
	 * @param __v The new value of the static field register.
	 * @since 2019/04/22
	 */
	public static final void specialSetStaticFieldRegister(int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Sets the current thread pointer.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/27
	 */
	public static final void specialSetThreadRegister(int __v)
	{
		throw new todo.OOPS();
	}
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __addr The address to invoke.
	 * @since 2019/05/23
	 */
	public static final void sysCall(short __si)
	{
		Assembly.__sysCall(__si);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCall(short __si, int __a)
	{
		Assembly.__sysCall(__si, __a);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCall(short __si, int __a, int __b)
	{
		Assembly.__sysCall(__si, __a, __b);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCall(short __si, int __a, int __b, int __c)
	{
		Assembly.__sysCall(__si, __a, __b, __c);
	}
	
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
	public static final void sysCall(short __si, int __a, int __b, int __c,
		int __d)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d);
	}
	
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
	public static final void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e);
	}
	
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
	public static final void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final void sysCall(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __addr The address to invoke.
	 * @since 2019/05/23
	 */
	public static final void sysCallP(short __si)
	{
		Assembly.__sysCall(__si);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCallP(short __si, int __a)
	{
		Assembly.__sysCall(__si, __a);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCallP(short __si, int __a, int __b)
	{
		Assembly.__sysCall(__si, __a, __b);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @since 2019/05/23
	 */
	public static final void sysCallP(short __si, int __a, int __b, int __c)
	{
		Assembly.__sysCall(__si, __a, __b, __c);
	}
	
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
	public static final void sysCallP(short __si, int __a, int __b, int __c,
		int __d)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d);
	}
	
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
	public static final void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e);
	}
	
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
	public static final void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final void sysCallP(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g, __h);
	}
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallPV(short __si)
	{
		return Assembly.__sysCall(__si);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallPV(short __si, int __a)
	{
		return Assembly.__sysCall(__si, __a);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallPV(short __si, int __a, int __b)
	{
		return Assembly.__sysCall(__si, __a, __b);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c)
	{
		return Assembly.__sysCall(__si, __a, __b, __c);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c,
		int __d)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final int sysCallPV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g,
			__h);
	}
	
	/**
	 * Invoke system call at the given index.
	 *
	 * @param __si System call index.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallV(short __si)
	{
		return Assembly.__sysCall(__si);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallV(short __si, int __a)
	{
		return Assembly.__sysCall(__si, __a);
	}
	
	/**
	 * Invoke system call at the given index, with arguments.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @return The result of the invocation.
	 * @since 2019/05/23
	 */
	public static final int sysCallV(short __si, int __a, int __b)
	{
		return Assembly.__sysCall(__si, __a, __b);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c)
	{
		return Assembly.__sysCall(__si, __a, __b, __c);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c,
		int __d)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g);
	}
	
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
	public static final int sysCallV(short __si, int __a, int __b, int __c,
		int __d, int __e, int __f, int __g, int __h)
	{
		return Assembly.__sysCall(__si, __a, __b, __c, __d, __e, __f, __g,
			__h);
	}
	
	/**
	 * Internal invocation call handling.
	 *
	 * @param __addr Method address.
	 * @param __args Arguments.
	 * @return The result.
	 * @since 2019/05/23
	 */
	private static final int __invoke(int __addr, int... __args)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Internal system call handling.
	 *
	 * @param __si System call index.
	 * @param __args Arguments.
	 * @return The result.
	 * @since 2019/05/23
	 */
	private static final int __sysCall(short __si, int... __args)
	{
		// Make at least 8!
		if (__args == null)
			__args = new int[8];
		if (__args.length < 8)
			__args = Arrays.copyOf(__args, 8);
		
		// Error state for the last call of this type
		int[] errors = _ERRORS;
		
		// Return value with error value, to set if any
		int rv,
			err;
		
		// Depends on the system call type
		switch (__si)
		{
				// Check if system call is supported
			case SystemCallIndex.QUERY_INDEX:
				{
					err = 0;
					switch (__args[0])
					{
						case SystemCallIndex.API_LEVEL:
						case SystemCallIndex.CALL_STACK_HEIGHT:
						case SystemCallIndex.CALL_STACK_ITEM:
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.EXIT:
						case SystemCallIndex.GARBAGE_COLLECT:
						case SystemCallIndex.LOAD_STRING:
						case SystemCallIndex.PD_OF_STDERR:
						case SystemCallIndex.PD_OF_STDIN:
						case SystemCallIndex.PD_OF_STDOUT:
						case SystemCallIndex.PD_WRITE_BYTE:
						case SystemCallIndex.SLEEP:
						case SystemCallIndex.TIME_HI_MILLI_WALL:
						case SystemCallIndex.TIME_HI_NANO_MONO:
						case SystemCallIndex.TIME_LO_MILLI_WALL:
						case SystemCallIndex.TIME_LO_NANO_MONO:
						case SystemCallIndex.VMI_MEM_FREE:
						case SystemCallIndex.VMI_MEM_MAX:
						case SystemCallIndex.VMI_MEM_USED:
							rv = 1;
							break;
						
						default:
							rv = 0;
							break;
					}
				}
				break;
				
				// API level
			case SystemCallIndex.API_LEVEL:
				{
					rv = ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV;
					err = 0;
				}
				break;
				
				// Call trace height
			case SystemCallIndex.CALL_STACK_HEIGHT:
				{
					// Remove traces for sysCall() and this method
					rv = new Throwable().getStackTrace().length - 2;
					err = 0;
				}
				break;
				
				// Call trace item
			case SystemCallIndex.CALL_STACK_ITEM:
				{
					// Get the current stack
					StackTraceElement[] stk = new Throwable().getStackTrace();
					
					// Get element trace here
					// Remove traces for sysCall() and this method
					int depth = 2 + __args[0];
					StackTraceElement ele = (depth < 0 || depth >= stk.length ?
						null : stk[depth]);
					
					// Fail?
					if (ele == null)
					{
						rv = 0;
						err = SystemCallError.VALUE_OUT_OF_RANGE;
					}
					
					// Handle normally
					else
					{
						// Depends
						switch (__args[1])
						{
								// Class name
							case CallStackItem.CLASS_NAME:
								rv = Assembly.__uniqueStringId(
									ele.getClassName().replace('.', '/'));
								err = 0;
								break;

								// The method name.
							case CallStackItem.METHOD_NAME:
								rv = Assembly.__uniqueStringId(
									ele.getMethodName());
								err = 0;
								break;

								// The current file.
							case CallStackItem.SOURCE_FILE:
								rv = Assembly.__uniqueStringId(
									ele.getFileName());
								err = 0;
								break;

								// Source line.
							case CallStackItem.SOURCE_LINE:
								rv = ele.getLineNumber();
								err = 0;
								break;

							default:
								rv = 0;
								err = SystemCallError.VALUE_OUT_OF_RANGE;
								break;
						}
					}
				}
				break;
				
				// Get error
			case SystemCallIndex.ERROR_GET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return the stored error code
					synchronized (errors)
					{
						rv = errors[dx];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
				
				// Set error
			case SystemCallIndex.ERROR_SET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return last error code, and set new one
					synchronized (errors)
					{
						rv = errors[dx];
						errors[dx] = __args[0];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
				
				// Exit the VM
			case SystemCallIndex.EXIT:
				{
					System.exit(__args[0]);
					
					rv = 0;
					err = 0;
				}
				break;
				
				// Invoke the garbage collector
			case SystemCallIndex.GARBAGE_COLLECT:
				{
					Runtime.getRuntime().gc();
					
					rv = 0;
					err = 0;
				}
				break;
				
				// Loads a string
			case SystemCallIndex.LOAD_STRING:
				{
					rv = Assembly.__uniqueObjectId(
						Assembly.__uniqueString(__args[0]));
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard input
			case SystemCallIndex.PD_OF_STDIN:
				{
					rv = 0;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard output
			case SystemCallIndex.PD_OF_STDOUT:
				{
					rv = 1;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard error
			case SystemCallIndex.PD_OF_STDERR:
				{
					rv = 1;
					err = 0;
				}
				break;
				
				// Write single byte to PD
			case SystemCallIndex.PD_WRITE_BYTE:
				{
					// Depends on the stream
					int pd = __args[0];
					OutputStream os = (pd == 1 ? System.out :
						(pd == 2 ? System.err : null));
					
					// Write
					if (os != null)
					{
						try
						{
							os.write(__args[1]);
							
							// Okay
							rv = 1;
							err = 0;
						}
						
						// Failed
						catch (IOException e)
						{
							rv = -1;
							err = SystemCallError.PIPE_DESCRIPTOR_BAD_WRITE;
						}
					}
					
					// Failed
					else
					{
						rv = -1;
						err = SystemCallError.PIPE_DESCRIPTOR_INVALID;
					}
				}
				break;
				
				// Sleep
			case SystemCallIndex.SLEEP:
				try
				{
					Thread.sleep(__args[0], __args[1]);
					
					rv = 0;
					err = SystemCallError.NO_ERROR;
				}
				catch (InterruptedException e)
				{
					rv = 1;
					err = SystemCallError.INTERRUPTED;
				}
				break;
			
				// Current wall clock milliseconds (low).
			case SystemCallIndex.TIME_LO_MILLI_WALL:
				{
					rv = (int)(System.currentTimeMillis());
					err = 0;
				}
				break;

				// Current wall clock milliseconds (high).
			case SystemCallIndex.TIME_HI_MILLI_WALL:
				{
					rv = (int)(System.currentTimeMillis() >>> 32);
					err = 0;
				}
				break;

				// Current monotonic clock nanoseconds (low).
			case SystemCallIndex.TIME_LO_NANO_MONO:
				{
					rv = (int)(System.nanoTime());
					err = 0;
				}
				break;

				// Current monotonic clock nanoseconds (high).
			case SystemCallIndex.TIME_HI_NANO_MONO:
				{
					rv = (int)(System.nanoTime() >>> 32);
					err = 0;
				}
				break;
			
				// VM information: Memory free bytes
			case SystemCallIndex.VMI_MEM_FREE:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().freeMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory used bytes
			case SystemCallIndex.VMI_MEM_USED:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().totalMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory max bytes
			case SystemCallIndex.VMI_MEM_MAX:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().maxMemory());
					err = 0;
				}
				break;
			
			default:
				// Returns no value but sets an error
				rv = -1;
				err = SystemCallError.UNSUPPORTED_SYSTEM_CALL;
				
				// If the ID is valid then a bad array access will be used
				if (__si < 0 || __si >= SystemCallIndex.NUM_SYSCALLS)
					__si = SystemCallIndex.QUERY_INDEX;
				break;
		}
		
		// Set error state as needed
		synchronized (errors)
		{
			errors[__si] = err;
		}
		
		// Use returning value
		return rv;
	}
	
	/**
	 * Returns the unique object for the given ID.
	 *
	 * @param __id The ID to get the object of.
	 * @return The object for the given ID.
	 * @since 2019/06/17
	 */
	private static Object __uniqueObject(int __id)
	{
		if (__id == 0)
			return null;
			
		Map<Object, Integer> objects = Assembly._OBJECTS;
		synchronized (objects)
		{
			for (Map.Entry<Object, Integer> e : objects.entrySet())
				if (__id == e.getValue().intValue())
					return e.getKey();
		}
		
		return null;
	}
	
	/**
	 * Returns the unique ID of the given object.
	 *
	 * @param __o The object to get.
	 * @return The ID of the given object.
	 * @since 2019/06/17
	 */
	private static int __uniqueObjectId(Object __o)
	{
		if (__o == null)
			return 0;
		
		Map<Object, Integer> objects = Assembly._OBJECTS;
		synchronized (objects)
		{
			Integer rv = objects.get(__o);
			if (rv == null)
				objects.put(__o, (rv = 1 + objects.size()));
			return rv;
		}
	}
	
	/**
	 * Returns the unique string for the given ID.
	 *
	 * @param __id The ID to get.
	 * @return The resulting string.
	 * @since 2019/06/17
	 */
	private static String __uniqueString(int __id)
	{
		if (__id == 0)
			return null;
		
		Map<String, Integer> strings = Assembly._STRINGS;
		synchronized (strings)
		{
			for (Map.Entry<String, Integer> e : strings.entrySet())
				if (__id == e.getValue().intValue())
					return e.getKey();
		}
		
		return null;
	}
	
	/**
	 * Returns an ID for the given unique string.
	 *
	 * @param __s The string to get the ID of.
	 * @return The unique string ID.
	 * @since 2019/06/17
	 */
	private static int __uniqueStringId(String __s)
	{
		if (__s == null)
			return 0;
		
		Map<String, Integer> strings = Assembly._STRINGS;
		synchronized (strings)
		{
			Integer rv = strings.get(__s);
			if (rv == null)
				strings.put(__s, (rv = 1 + strings.size()));
			return rv;
		}
	}
}

