// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.bin.LinkingPoint;

/**
 * This is an optional interface which may be called when there has been an
 * update to the JIT and can be used to gauge progress.
 *
 * @since 2017/08/29
 */
public interface JITProgressNotifier
{
	/**
	 * Specifies that the given JAR is being processed.
	 *
	 * @param __n The JAR being processed.
	 * @since 2017/08/29
	 */
	public abstract void beginJar(String __n);
	
	/**
	 * Specifies that the given JAR has finished processing.
	 *
	 * @param __n The JAR that was processed.
	 * @param __ns The number of nanoseconds it took to process the JAR.
	 * @param __lr The number of resources processed.
	 * @param __lc The number of classes processed.
	 * @since 2017/08/29
	 */
	public abstract void endJar(String __n, long __ns, int __lr, int __lc);
	
	/**
	 * Specifies that the high level program has been processed and linked.
	 *
	 * @param __lp The linking point for the program.
	 * @param __ns The number of nano-seconds which have progressed.
	 * @since 2017/08/29
	 */
	public abstract void jitEndHighLevelProgram(LinkingPoint __lp, long __ns);
	
	/**
	 * Specifies that the high level program is about to be processed.
	 *
	 * @param __lp The linking point for the program.
	 * @since 2017/08/29
	 */
	public abstract void jitStartHighLevelProgram(LinkingPoint __lp);
	
	/**
	 * Specifies that the specified class is being processed.
	 *
	 * @param __n The name of the JAR.
	 * @parma __cl The class file name.
	 * @param __num The number of classes processed so far.
	 * @since 2017/08/29
	 */
	public abstract void processClass(String __n, String __cl, int __num);
	
	/**
	 * Specifies that the specified resource is being processed.
	 *
	 * @param __n The name of the JAR.
	 * @parma __rc The resource file name.
	 * @param __num The number of resources processed so far.
	 * @since 2017/08/29
	 */
	public abstract void processResource(String __n, String __rc, int __num);
}

