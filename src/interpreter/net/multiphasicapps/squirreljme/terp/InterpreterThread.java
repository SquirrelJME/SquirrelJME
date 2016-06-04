// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is the base class for any threads which exist within the interpreter.
 *
 * @since 2016/06/03
 */
public abstract class InterpreterThread
{
	/** The owning interpreter. */
	protected final Interpreter interpreter;
	
	/** The owning process. */
	protected final InterpreterProcess process;
	
	/** The main class. */
	protected final ClassNameSymbol mainclass;
	
	/** The main method. */
	protected final CIMethodID mainmethod;
	
	/** The main arguments. */
	protected final List<Object> mainargs;
	
	/**
	 * This initializes the base interpreter thread.
	 *
	 * @param __terp The owning interpreter.
	 * @param __proc The owning process.
	 * @param __mc The main class.
	 * @param __mm The main entry method.
	 * @param __args The arguments used at the start of the method.
	 * @throws IllegalStateException If the process belongs to another
	 * interpreter.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	public InterpreterThread(Interpreter __terp, InterpreterProcess __proc,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__terp == null || __proc == null || __mc == null || __mm == null ||
			__args == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AN01 The process does not belong to the
		// interpreter.}
		if (__proc.interpreter() != __terp)
			throw new IllegalStateException("AN01");
		
		// Set
		this.interpreter = __terp;
		this.process = __proc;
		this.mainclass = __mc;
		this.mainmethod = __mm;
		this.mainargs = UnmodifiableList.<Object>of(Arrays.<Object>asList(
			__args.clone()));
		
		// Make sure the arguments are valid
		// {@squirreljme.error AN02 An argument with the given class type
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
				throw new ClassCastException(String.format("AN02 %s",
					a.getClass()));
	}
}

