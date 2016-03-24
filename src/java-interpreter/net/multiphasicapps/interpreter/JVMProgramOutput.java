// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This is the base class for a generator which is used to output an actual
 * program.
 *
 * @since 2016/03/23
 */
public abstract class JVMProgramOutput
{
	/** Native register setup. */
	protected final JVMNativeRegisters nativeregs;
	
	/**
	 * Initializes the program outputter.
	 *
	 * @param __nr The mappings of native registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/24
	 */
	public JVMProgramOutput(JVMNativeRegisters __nr)
		throws NullPointerException
	{
		// Check
		if (__nr == null)
			throw new NullPointerException();
		
		// Set
		nativeregs = __nr;
	}
	
	/**
	 * Returns the native register setup.
	 *
	 * @return The native registers.
	 * @since 2016/03/24
	 */
	public final JVMNativeRegisters nativeRegisters()
	{
		return nativeregs;
	}
	
	/**
	 * This is a factory which creates output programs.
	 *
	 * @since 2016/03/23
	 */
	public static abstract class Factory
	{
		/**
		 * Creates a program output.
		 *
		 * @return Program output.
		 * @since 2016/03/24
		 */
		public abstract JVMProgramOutput create();
	}
}

