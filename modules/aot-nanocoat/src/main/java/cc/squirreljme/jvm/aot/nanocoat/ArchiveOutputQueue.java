// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CFile;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Queue that can handle writing of multiple files at any one time and write
 * to a target Zip archive, this is used for the multiples of individual files
 * that need creating.
 *
 * @since 2023/08/12
 */
public class ArchiveOutputQueue
	implements Closeable
{
	/** The Zip to write to. */
	protected final ZipStreamWriter zip;
	
	/** Files which have been output to this archive. */
	protected final Set<String> outputFiles =
		new SortedTreeSet<>();
	
	/** Self reference, to lower object creation. */
	private final Reference<ArchiveOutputQueue> _self =
		new WeakReference<>(this);
	
	/**
	 * Initializes the archive output.
	 *
	 * @param __zip The Zip to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public ArchiveOutputQueue(ZipStreamWriter __zip)
		throws NullPointerException
	{
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Builds a new C File for output.
	 *
	 * @param __name The name of the file.
	 * @return The resultant C File.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public CFile nextCFile(String __name)
		throws IOException, NullPointerException
	{
		return Utils.cFile(this.nextEntry(__name));
	}
	
	/**
	 * Creates a new entry to write data to.
	 *
	 * @param __name The name of the file to write.
	 * @return The stream to write the file.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public OutputStream nextEntry(String __name)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Setup new entry
		return new __QueuedOutput__(this._self);
	}
	
	/**
	 * Creates a new {@link PrintStream} output.
	 *
	 * @param __name The name of the file.
	 * @return The print stream to write to the file.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public PrintStream nextPrintStream(String __name)
		throws IOException, NullPointerException
	{
		return new PrintStream(this.nextEntry(__name),
			true,
			"utf-8");
	}
}
