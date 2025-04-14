// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.host.views.JDWPView;
import cc.squirreljme.jdwp.host.views.JDWPViewKind;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the complete state for the debugging instance.
 *
 * @since 2021/03/13
 */
public final class JDWPHostState
{
	/** References to everything the debugger knows about. */
	public final JDWPHostLinker<Object> items =
		new JDWPHostLinker<>(Object.class);
	
	/** The binding used. */
	private final Reference<JDWPHostBinding> _binding;
	
	/** The views that are available. */
	private final JDWPView[] _views =
		new JDWPView[JDWPViewKind.values().length];
	
	/** Has this been latched? */
	private volatile boolean _latchStart;
	
	/**
	 * Initializes the state.
	 * 
	 * @param __binding The binding used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	JDWPHostState(Reference<JDWPHostBinding> __binding)
		throws NullPointerException
	{
		if (__binding == null)
			throw new NullPointerException("NARG");
		
		this._binding = __binding;
	}
	
	/**
	 * Latches onto whether this is the first thread being added.
	 *
	 * @return If this is the first thread, this method only
	 * returns {@code true} once.
	 * @since 2024/01/21
	 */
	public boolean latchFirstThread()
	{
		synchronized (this)
		{
			// Once set, this always returns false
			if (this._latchStart)
				return false;
			
			// Latch and return true, so we cannot call again
			this._latchStart = true;
			return true;
		}
	}
	
	/**
	 * Returns the view of the given type.
	 * 
	 * @param <V> The type to view.
	 * @param __type The type to view.
	 * @param __kind The kind of viewer to use.
	 * @return The view for the given type.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public final <V extends JDWPView> V view(Class<V> __type,
		JDWPViewKind __kind)
		throws NullPointerException
	{
		if (__type == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Use a pre-cached view if available
		JDWPView[] views = this._views;
		JDWPView view = views[__kind.ordinal()];
		if (view != null)
			return __type.cast(view);
		
		// Obtain the view for this kind of object
		view = this.__binding().debuggerView(__type, __kind,
			new WeakReference<>(this));
		
		/* {@squirreljme.error AG0m The binding does not know about this kind
		of view? (The kind)} */
		if (view == null)
			throw new IllegalStateException("AG0m " + __kind);
		
		// Cache for later and use it now
		views[__kind.ordinal()] = view;
		return __type.cast(view);
	}
	
	/**
	 * Returns the used binding.
	 * 
	 * @return The binding used.
	 * @throws IllegalStateException If the binding was GCed.
	 * @since 2021/04/10
	 */
	final JDWPHostBinding __binding()
		throws IllegalStateException
	{
		/* {@squirreljme.error AG0l The Binding was GCed.} */
		JDWPHostBinding rv = this._binding.get();
		if (rv == null)
			throw new IllegalStateException("AG0l");
		
		return rv;
	}
}
