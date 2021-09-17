// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPView;
import cc.squirreljme.jdwp.views.JDWPViewKind;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the complete state for the debugging instance.
 *
 * @since 2021/03/13
 */
public final class JDWPState
{
	/** References to everything the debugger knows about. */
	public final JDWPLinker<Object> items =
		new JDWPLinker<>(Object.class);
	
	/** The binding used. */
	private final Reference<JDWPBinding> _binding;
	
	/** The views that are available. */
	private final JDWPView[] _views =
		new JDWPView[JDWPViewKind.values().length];
	
	/**
	 * Initializes the state.
	 * 
	 * @param __binding The binding used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	JDWPState(Reference<JDWPBinding> __binding)
		throws NullPointerException
	{
		if (__binding == null)
			throw new NullPointerException("NARG");
		
		this._binding = __binding;
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
		
		// {@squirreljme.error AG0m The binding does not know about this kind
		// of view? (The kind)}
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
	final JDWPBinding __binding()
		throws IllegalStateException
	{
		// {@squirreljme.error AG0l The Binding was GCed.}
		JDWPBinding rv = this._binding.get();
		if (rv == null)
			throw new IllegalStateException("AG0l");
		
		return rv;
	}
}
