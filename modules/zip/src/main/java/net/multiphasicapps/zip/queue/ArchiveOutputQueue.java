// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.queue;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
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
	
	/** Files which have been output to this archive and their CRC. */
	protected final Map<String, Integer> outputFiles =
		new SortedTreeMap<>();
	
	/** Self reference, to lower object creation. */
	private final Reference<ArchiveOutputQueue> _self =
		new WeakReference<>(this);
	
	/** Already closed? */
	private volatile boolean _isClosed;
	
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
		// Already closed?
		if (this._isClosed)
			return;
		this._isClosed = true;
		
		/* {@squirreljme.error NC04 An entry is currently being written and
		has not yet been closed. (The file being written)} */
		for (Map.Entry<String, Integer> entry : this.outputFiles.entrySet())
			if (entry.getValue() == null)
				throw new IOException("NC04 " + entry.getKey());
		
		// Finish the Zip off
		ZipStreamWriter zip = this.zip;
		zip.flush();
		zip.close();
		zip.flush();
	}
	
	/**
	 * Has this file been output?
	 *
	 * @param __file The file to check.
	 * @return If it has been output already.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public boolean hasOutput(String __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return this.outputFiles.containsKey(__file);
	}
	
	/**
	 * Creates a new entry to write data to.
	 *
	 * @param __name The name of the file to write.
	 * @return The stream to write the file.
	 * @throws IOException If an entry with the given name already exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public OutputStream nextEntry(String __name)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error NC03 Duplicate output file with different
		content. (The file)} */
		Map<String, Integer> outputFiles = this.outputFiles;
		if (outputFiles.containsKey(__name))
			throw new IOException("NC03 " + __name);
		
		// Setup new entry
		return new __QueuedOutput__(this._self, __name);
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
