// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.clsyscall;

import java.io.InputStream;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

/**
 * This is a system caller which uses a basic input stream and a basic output
 * stream to communicate with the kernel to provide system call support.
 *
 * Not all functionality is supported and as such still requires a native
 * implementation.
 *
 * @since 2017/12/31
 */
public abstract class ClientCaller
	extends SystemCaller
{
	/** The input stream. */
	protected final InputStream in;
	
	/** The output stream. */
	protected final OutputStream out;
	
	/**
	 * Initializes the client caller.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public ClientCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgramInstallReport installProgram(
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask launchTask(SystemProgram __program,
		String __mainclass, int __perms, String... __props)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgram[] listPrograms(int __typemask)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask[] listTasks(boolean __incsys)
		throws SecurityException
	{
		throw new todo.TODO();
	}
}

