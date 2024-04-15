// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.Closeable;
import java.io.IOException;

/**
 * Represents a C block, which can be used for the preprocessor or curly
 * braces.
 *
 * @since 2023/05/29
 */
public class CBlock
	extends CFileProxy
	implements Closeable
{
	/** Extra indentation that was performed here. */
	protected int extraIndent;
	
	/** The character to close with. */
	final String _closeString;
	
	/**
	 * Initializes the C Block.
	 * 
	 * @param __ref The reference to use.
	 * @param __close The close string to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CBlock(CSourceWriter __ref, String __close)
		throws NullPointerException
	{
		super(__ref);
		
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
		this._file.__close(this);
	}
	
	/**
	 * Finishes the output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	void __finish()
		throws IOException
	{
		if (this._closeString != null)
			this.token(this._closeString);
	}
}
