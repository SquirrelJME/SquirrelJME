// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

/**
 * This class is used by the byte code processor to handle all of the various
 * instructions and such.
 *
 * @since 2019/04/06
 */
public interface ByteCodeHandler
{
	/**
	 * Performs a copy operation.
	 *
	 * @param __in The input.
	 * @param __out The output.
	 * @since 2019/04/07
	 */
	public abstract void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out);
	
	/**
	 * Finishes handling the instruction operation.
	 *
	 * @since 2019/04/07
	 */
	public abstract void instructionFinish();
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	public abstract void instructionSetup();
	
	/**
	 * Returns the state of the byte code, this must always return the
	 * same object.
	 *
	 * @return The byte code state.
	 * @since 2019/04/06
	 */
	public abstract ByteCodeState state();
}

