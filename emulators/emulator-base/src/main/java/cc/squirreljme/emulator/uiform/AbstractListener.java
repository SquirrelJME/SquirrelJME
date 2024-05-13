// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Base implementation for listeners according to components.
 *
 * @since 2020/10/17
 */
@Deprecated
public abstract class AbstractListener
{
	/** Reference to the widget item. */
	private final Reference<SwingWidget> ref;
	
	/**
	 * Initializes the base listener.
	 * 
	 * @param __item The item to notify.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public AbstractListener(SwingWidget __item)
		throws NullPointerException
	{
		if (__item == null)
			throw new NullPointerException("NARG");
		
		this.ref = new WeakReference<>(__item);
	}
	/**
	 * Returns the callback for the form.
	 * 
	 * @return The callback for the item.
	 * @since 2020/10/17
	 */
	public final UIFormCallback callback()
	{
		SwingWidget widget = this.ref.get();
		if (widget == null)
			return null;
		
		return widget.callback();
	}
	
	/**
	 * Returns the item this maps to.
	 * 
	 * @return The mapped item.
	 * @since 2020/10/17
	 */
	public final SwingWidget item()
	{
		return this.ref.get();
	}
}
