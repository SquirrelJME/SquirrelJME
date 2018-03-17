// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a queue which manages which handles displayable which are bound to
 * the remote server, but when they are garbage collected allows them to be
 * cleaned up by the server accordingly.
 *
 * @since 2018/03/17
 */
final class __Queue__
{
	/** Single queue instance. */
	static final __Queue__ INSTANCE =
		new __Queue__();
	
	/** Displayables to their ID. */
	protected final Map<Reference<Displayable>, Integer> _distoid =
		new HashMap<>();
	
	/** Queue to tell the remote server that handles should be cleaned up. */
	protected final ReferenceQueue<Displayable> _queue =
		new ReferenceQueue<>();
	
	/**
	 * Internally initialized.
	 *
	 * @since 2018/03/17
	 */
	private __Queue__()
	{
	}
	
	/**
	 * Registers the given displayable and returns the remote handle to it.
	 *
	 * @param __d The displayable to register.
	 * @return The handle of the displayable on the remote end.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	final int __register(Displayable __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Determine the type of displayable this is first
		DisplayableType type;
		if (__d instanceof Canvas)
			type = DisplayableType.CANVAS;
		else if (__d instanceof Alert)
			type = DisplayableType.ALERT;
		else if (__d instanceof FileSelector)
			type = DisplayableType.FILE_SELECTOR;
		else if (__d instanceof Form)
			type = DisplayableType.FORM;
		else if (__d instanceof List)
			type = DisplayableType.LIST;
		else if (__d instanceof TabbedPane)
			type = DisplayableType.TABBED_PANE;
		else if (__d instanceof TextBox)
			type = DisplayableType.TEXT_BOX;
		
		// {@squirreljme.error EB1x Could not determine the type displayable
		// that this is. (The displayable type)}
		else
			throw new RuntimeException(String.format(
				"EB1x %s", __d.getClass()));
		
		// Register and get the index for it
		int dx = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.CREATE_DISPLAYABLE, type);
		
		// Reference the displayable for future cleanup on the remote end
		Map<Reference<Displayable>, Integer> distoid = this._distoid;
		synchronized (distoid)
		{
			distoid.put(new WeakReference<>(__d, this._queue), dx);
		}
		
		// The displayable uses this index to interact with the server
		return dx;
	}
}

