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
	 * Entry point marker (no op).
	 *
	 * @since 2019/04/21
	 */
	public static native void entryMarker();
	
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
	 * Sets the value of the static field register.
	 *
	 * @param __v The new value of the static field register.
	 * @since 2019/04/22
	 */
	public static native void specialSetStaticFieldRegister(int __v);
}

