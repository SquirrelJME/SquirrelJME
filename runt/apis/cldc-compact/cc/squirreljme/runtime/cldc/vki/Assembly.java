// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

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
	 * Trigger breakpoint within the virtual machine.
	 *
	 * @since 2019/04/21
	 */
	public static native void breakpoint();
	
	/**
	 * Performs explicit exception handling.
	 *
	 * @since 2019/04/28
	 */
	public static native void exceptionHandle();
	
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
	 * Loads a value from the class table at the given index.
	 *
	 * @return The index of the value in the class table.
	 * @since 2019/04/28
	 */
	public static native int loadClass(int __i);
	
	/**
	 * Loads a value from the constant pool at the given index.
	 *
	 * @return The index of the value in the constant pool.
	 * @since 2019/04/28
	 */
	public static native int loadPool(int __i);
	
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
	 * Gets the value of the class table.
	 *
	 * @return The value of the class table register.
	 * @since 2019/04/27
	 */
	public static native int specialGetClassTableRegister();
	
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
	 * Sets the current class table pointer.
	 *
	 * @param __v The value to use.
	 * @since 2019/04/27
	 */
	public static native void specialSetClassTableRegister(int __v);
	
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
}

