// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.summercoat.register.ExecutablePointer;
import dev.shadowtail.classfile.summercoat.register.InterfaceOfObject;
import dev.shadowtail.classfile.summercoat.register.InterfaceVTIndex;
import dev.shadowtail.classfile.summercoat.register.RuntimePoolPointer;
import dev.shadowtail.classfile.summercoat.register.TypedRegister;
import dev.shadowtail.classfile.summercoat.register.Volatile;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a stack which is used to manage which volatile registers are used.
 *
 * @since 2019/05/24
 */
public final class VolatileRegisterStack
{
	/** The base register. */
	protected final int base;
	
	/** Registers being used. */
	private final Collection<Integer> _used =
		new ArrayList<>();
	
	/**
	 * Initializes the volatile stack.
	 *
	 * @param __b The base register.
	 * @since 2019/05/25
	 */
	public VolatileRegisterStack(int __b)
	{
		this.base = __b;
	}
	
	/**
	 * Clears all of the used volatile registers.
	 *
	 * @since 2019/05/25
	 */
	public final void clear()
	{
		this._used.clear();
	}
	
	/**
	 * Returns a volatile executable pointer.
	 * 
	 * @return The executable pointer.
	 * @since 2020/11/24
	 */
	public Volatile<ExecutablePointer> getExecutablePointer()
	{
		return new Volatile<>(this,
			new ExecutablePointer(this.getUnmanaged()));
	}
	
	/**
	 * Returns a volatile to represent this as an interface of an object.
	 * 
	 * @return Interface of object register.
	 * @since 2020/11/24
	 */
	public Volatile<InterfaceOfObject> getInterfaceOfObject()
	{
		return new Volatile<>(this,
			new InterfaceOfObject(this.getUnmanaged()));
	}
	
	/**
	 * Returns a register to store an interface virtual table index.
	 * 
	 * @return Volatile to store an interface vtable index.
	 * @since 2020/11/24
	 */
	public Volatile<InterfaceVTIndex> getInterfaceVTIndex()
	{
		return new Volatile<>(this,
			new InterfaceVTIndex(this.getUnmanaged()));
	}
	
	/**
	 * Returns a volatile to represent a runtime pool pointer.
	 * 
	 * @return A register to store the runtime pool pointer.
	 * @since 2020/11/24
	 */
	public Volatile<RuntimePoolPointer> getRuntimePoolPointer()
	{
		return new Volatile<>(this,
			new RuntimePoolPointer(this.getUnmanaged()));
	}
	
	/**
	 * Returns a volatile typed volatile register.
	 * 
	 * @param <T> The typed register type.
	 * @param __cl The typed register type.
	 * @return A volatile typed register.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/24
	 */
	public final <T> Volatile<TypedRegister<T>> getTyped(Class<T> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return new Volatile<>(this,
			new TypedRegister<T>(__cl, this.getUnmanaged()));
	}
	
	/**
	 * Returns the next volatile register.
	 *
	 * @return The next volatile register.
	 * @throws IllegalStateException If no registers are available.
	 * @since 2019/05/24
	 */
	public final int getUnmanaged()
		throws IllegalStateException
	{
		// Find next register to use from the base, use any register which
		// was not previously recorded
		int at = this.base;
		Collection<Integer> used = this._used;
		while (used.contains(at))
			at++;
		
		// {@squirreljme.error JC4l Exceeded maximum permitted registers.
		// (The base register)}
		if (at >= NativeCode.MAX_REGISTERS)
			throw new IllegalStateException("JC4l " + this.base);
		
		// Record it
		used.add(at);
		return at;
	}
	
	/**
	 * Removes the given volatile register from usage.
	 *
	 * @param __r The register to remove.
	 * @throws IllegalStateException If it was never used.
	 * @since 2019/05/24
	 */
	public final void removeUnmanaged(int __r)
		throws IllegalStateException
	{
		// {@squirreljme.error JC13 Register to remove was never previously
		// returned or was removed multiple times. (The register)}
		if (!this._used.remove(__r))
			throw new IllegalStateException("JC13 " + __r);
	}
}

