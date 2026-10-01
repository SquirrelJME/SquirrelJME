// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui.extra;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Displayable;

/**
 * An instance which contains an underlying {@link Displayable}, this is
 * generally intended for vendor specific UIs such as DoJa.
 *
 * @since 2026/09/25
 */
@SquirrelJMEVendorApi
public final class ExtraDisplayable
	implements ExtraState
{
	/** The displayable this refers to. */
	private final WeakReference<Displayable> _ref;
	
	/**
	 * Initializes the extra displayable.
	 *
	 * @param __displayable The displayable to refer to.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	public ExtraDisplayable(Displayable __displayable)
		throws NullPointerException
	{
		if (__displayable == null)
			throw new NullPointerException("NARG");
		
		this._ref = new WeakReference<>(__displayable);
	}
	
	/**
	 * Returns the mapped displayable, or {@code null} if it has been GCed.
	 *
	 * @return The mapped displayable, or {@code null} if it has been GCed.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public final Displayable get()
	{
		return this._ref.get();
	}
}
