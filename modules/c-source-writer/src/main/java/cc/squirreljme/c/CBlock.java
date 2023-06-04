// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.Reference;

/**
 * Represents a C block, which can be used for the preprocessor or curly
 * braces.
 *
 * @since 2023/05/29
 */
public class CBlock
	implements Closeable
{
	/** The character to close with. */
	final String _closeString;
	
	/** The referenced writer. */
	private final Reference<CSourceWriter> _writer;
	
	/**
	 * Initializes the C Block.
	 * 
	 * @param __ref The reference to use.
	 * @param __close The close string to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CBlock(Reference<CSourceWriter> __ref, String __close)
		throws NullPointerException
	{
		if (__ref == null || __ref.get() == null)
			throw new NullPointerException("NARG");
		
		this._writer = __ref;
		this._closeString = __close;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public void close()
		throws IOException
	{
		this.writer().__close(this);
	}
	
	/**
	 * Returns the used source writer, for inlining.
	 * 
	 * @return The source writer user.
	 * @throws IllegalStateException If the writer was garbage collected.
	 * @since 2023/05/29
	 */
	public CSourceWriter writer()
		throws IllegalStateException
	{
		CSourceWriter rv = this._writer.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		return rv;
	}
	
	/**
	 * Finishes the output.
	 * 
	 * @param __writer The writer to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	void __finish(CSourceWriter __writer)
		throws IOException, NullPointerException
	{
		if (__writer == null)
			throw new NullPointerException("NARG");
		
		if (this._closeString != null)
			__writer.token(this._closeString);
	}
}
