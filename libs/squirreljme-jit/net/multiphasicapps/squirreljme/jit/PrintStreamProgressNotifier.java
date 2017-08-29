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

import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.bin.LinkingPoint;

/**
 * This is a progress notifier which outputs to the given stream.
 *
 * @since 2017/08/29
 */
public class PrintStreamProgressNotifier
	implements JITProgressNotifier
{
	/** The target stream to print to. */
	protected final PrintStream out;
	
	/**
	 * Initializes the progress notifier which writes to the given output
	 * stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/29
	 */
	public PrintStreamProgressNotifier(PrintStream __out)
		throws NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void beginJar(String __n)
	{
		// {@squirreljme.error JI01 Starting processing of the specified JAR.
		// (The name of the JAR)}
		this.out.printf("JI01 %s%n", __n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void endJar(String __n, int __lr, int __lc)
	{
		// {@squirreljme.error JI0t Finished processing the specified JAR.
		// (The name of the JAR; The number of resources; The number of
		// classes)}
		this.out.printf("JI0t %s %d %d%n", __n, __lr, __lc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitEndHighLevelProgram(LinkingPoint __lp, long __ns)
	{
		// {@squirreljme.error JI0u Finished JIT compiling the specified high
		// level program. (The linking point of the program; The number of
		// nanoseconds it took for compilation)}
		this.out.printf("JI0u %s %d%n", __lp, __ns);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void jitStartHighLevelProgram(LinkingPoint __lp)
	{
		// {@squirreljme.error JI0v Starting JIT compilation of the specified
		// program. (The linking point of the program)}
		this.out.printf("JI0v %s%n", __lp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processClass(String __n, String __cl, int __num)
	{
		// {@squirreljme.error JI0w Processing the specified class. (The name
		// of the JAR; The name of the class; The number of classes which
		// are being processed)}
		this.out.printf("JI0w %s %s %d%n", __n, __cl, __num);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/29
	 */
	@Override
	public void processResource(String __n, String __rc, int __num)
	{
		// {@squirreljme.error JI0x Processing the specified resource. (The
		// name of the JAR; The name of the resource; The number of resource
		// which are being processed)}
		this.out.printf("JI0x %s %s %d%n", __n, __rc, __num);
	}
}

