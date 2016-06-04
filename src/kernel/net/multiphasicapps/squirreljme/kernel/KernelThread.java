// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is an abstract representation of a thread which runs within the kernel
 * that is implemented using native means.
 *
 * @since 2016/05/28
 */
public abstract class KernelThread
	implements __Identifiable__
{
	/** The kernel which owns this thread. */
	protected final Kernel kernel;
	
	/** The owning process. */
	protected final KernelProcess process;
	
	/** The thread ID. */
	protected final int id;
	
	/** The main class. */
	protected final ClassNameSymbol mainclass;
	
	/** The main method. */
	protected final CIMethodID mainmethod;
	
	/** The main arguments. */
	protected final List<Object> mainargs;
	
	/**
	 * Initializes the base kernel thread.
	 *
	 * @param __k The owning kernel.
	 * @param __proc The owning process.
	 * @param __mc The main class.
	 * @param __mm The main method.
	 * @param __args The arguments to the thread.
	 * @throws IllegalStateException If the owning process belongs to another
	 * kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/28
	 */
	public KernelThread(Kernel __k, KernelProcess __proc, ClassNameSymbol __mc,
		CIMethodID __mm, Object... __args)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__k == null || __proc == null || __mc == null || __mm == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0f The process belongs to another kernel.}
		if (__proc.kernel() != __k)
			throw new IllegalStateException("AY0f");
		
		// Set
		this.kernel = __k;
		this.process = __proc;
		this.id = __k._threadidgen.__next();
		
		// Defensive copy
		__args = __args.clone();
		
		// Adjust entry points last
		this.mainclass = Objects.<ClassNameSymbol>requireNonNull(
			(__mc = adjustMainClass(__mc)), "NARG");
		this.mainmethod = Objects.<CIMethodID>requireNonNull(
			(__mm = adjustMainMethod(__mm)), "NARG");
		this.mainargs = UnmodifiableList.<Object>of(Arrays.<Object>asList(
			Objects.<Object[]>requireNonNull(
			(__args = adjustMainArguments(__args))).clone()));
		
		// Make sure the arguments are valid
		// {@squirreljme.error AY0i An argument with the given class type
		// cannot be passed to a thread's initial arguments. (The class of the
		// argument)}
		for (Object a : this.mainargs)
			if (a != null &&
				!(a instanceof String) && !(a instanceof Boolean) &&
				!(a instanceof Byte) && !(a instanceof Short) &&
				!(a instanceof Character) && !(a instanceof Integer) &&
				!(a instanceof Long) && !(a instanceof Float) &&
				!(a instanceof Double) && !(a instanceof String[]) &&
				!(a instanceof boolean[]) && !(a instanceof byte[]) &&
				!(a instanceof short[]) && !(a instanceof char[]) &&
				!(a instanceof int[]) && !(a instanceof long[]) &&
				!(a instanceof float[]) && !(a instanceof double[]))
				throw new ClassCastException(String.format("AY0i %s",
					a.getClass()));
	}
	
	/**
	 * Potentially adjusts the arguments to be used by this thread.
	 *
	 * @param __args The original arguments.
	 * @return The adjusted arguments.
	 * @since 2016/06/03
	 */
	protected Object[] adjustMainArguments(Object... __args)
	{
		return __args;
	}
	
	/**
	 * Potentially adjusts the main entry class to be used by this thread.
	 *
	 * @param __mc The original class.
	 * @return The adjusted main class.
	 * @since 2016/06/03
	 */
	protected ClassNameSymbol adjustMainClass(ClassNameSymbol __mc)
	{
		return __mc;
	}
	
	/**
	 * Potentially adjusts the main entry method to be used by this thread.
	 *
	 * @param __mm The original method.
	 * @return The adjusted main method.
	 * @since 2016/06/03
	 */
	protected CIMethodID adjustMainMethod(CIMethodID __mm)
	{
		return __mm;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
	
	/**
	 * Returns the owning kernel.
	 *
	 * @return The owning kernel.
	 * @since 2016/06/03
	 */
	public final Kernel kernel()
	{
		return this.kernel;
	}
	
	/**
	 * Returns the owning process.
	 *
	 * @return The owning process.
	 * @since 2016/06/03
	 */
	public final KernelProcess process()
	{
		return this.process;
	}
}

