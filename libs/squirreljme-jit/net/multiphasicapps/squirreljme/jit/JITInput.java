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

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class is used to provide class and resource input for the JIT.
 *
 * This class is thread safe.
 *
 * @since 2017/09/26
 */
public final class JITInput
{
	/** The progress notifier to use. */
	protected final JITProgressNotifier notifier;
	
	/**
	 * Initializes the JIT input.
	 *
	 * @since 2017/09/26
	 */
	public JITInput()
	{
		this.notifier = new NullProgressNotifier();
	}
	
	/**
	 * Initializes the JIT input with the given progress notifier.
	 *
	 * @param __pn The notifier for progress.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public JITInput(JITProgressNotifier __pn)
		throws NullPointerException
	{
		// Check
		if (__pn == null)
			throw new NullPointerException("NARG");
		
		this.notifier = new CatchingProgressNotifier(__pn);
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __gn The group name of the ZIP.
	 * @param __in The input stream to be treated as a ZIP file for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public final JITInput readZip(String __gn, InputStream __in)
		throws IOException, JITException, NullPointerException
	{
		if (__gn == null || __in == null)
			throw new NullPointerException("NARG");
		
		return readZip(__gn, new ZipStreamReader(__in));
	}
	
	/**
	 * Reads the specified ZIP file and appends it to the input for the JIT.
	 *
	 * @param __gn The group name of the ZIP.
	 * @param __in The ZIP file to read from for input.
	 * @return {@code this}.
	 * @throws IOException On read errors.
	 * @throws JITException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/26
	 */
	public final JITInput readZip(String __gn, ZipStreamReader __in)
		throws IOException, NullPointerException
	{
		if (__gn == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

