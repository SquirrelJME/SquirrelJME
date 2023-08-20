// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;

/**
 * Base class for implementations of {@link Path}.
 *
 * @since 2023/08/20
 */
public abstract class AbstractPath
	implements Path
{
	/** The cached root. */
	private volatile Reference<Path> _root;
	
	/** Is the root known to be null? */
	private volatile boolean _isNullRoot;
	
	/** The cached name count. */
	private volatile int _nameCount =
		-1;
	
	/**
	 * Returns the name count of the path.
	 *
	 * @return The path name count.
	 * @since 2023/08/20
	 */
	protected abstract int getInternalNameCount();
	
	/**
	 * Returns the root of this path.
	 *
	 * @return The root of this path, may be {@code null} if there is none.
	 * @since 2023/08/20
	 */
	protected abstract Path getInternalRoot();
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final int getNameCount()
	{
		// Check for cached value
		int result = this._nameCount;
		if (result < 0)
		{
			result = this.getInternalNameCount();
			this._nameCount = result;
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/20
	 */
	@Override
	public final Path getRoot()
	{
		// Known to be null already?
		if (this._isNullRoot)
			return null;
		
		Reference<Path> ref = this._root;
		Path rv;
		
		// Determine the root of the path
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = this.getInternalRoot();
			
			// Either flag it as null or cache it
			if (rv == null)
				this._isNullRoot = true;
			else
				this._root = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
