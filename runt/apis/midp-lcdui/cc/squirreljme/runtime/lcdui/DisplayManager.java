// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.service.ServiceAccessor;
import cc.squirreljme.runtime.lcdui.event.EventQueue;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Displayable;

/**
 * This is a provider which always returns a fixed set of display heads that
 * are available for usage.
 *
 * This is intended to be used as a system service.
 *
 * @since 2017/08/19
 */
public abstract class DisplayManager
{
	/**
	 * Anything which happens in displays are thread safe and serialized
	 * between all potential displays. Since there can be multiple displays
	 * running at the same time all managing other widgets, it is more
	 * effective for situations where there is global state to maintain that
	 * a single lock is used, despite some performance costs.
	 */
	public static final Object GLOBAL_LOCK =
		new Object();
	
	/** The global display manager instance. */
	public static final DisplayManager DISPLAY_MANAGER;
	
	/** The event queue where events are generated. */
	protected final EventQueue eventqueue =
		new EventQueue();
	
	/**
	 * This locates and initializes the default display manager.
	 *
	 * @since 2017/05/23
	 */
	static
	{
		// Use the default display head manager
		DisplayManager dhp = ServiceAccessor.
			<DisplayManager>service(DisplayManager.class);
		
		// If no manager is available, use a null display manager that does
		// not actually have any real function
		if (dhp == null)
			dhp = new HeadlessDisplayManager();
		
		// {@squirreljme.property
		// cc.squirreljme.runtime.lcdui.compatibility=args
		// This specifies that a compatibility display manager should be
		// used instead for the LCDUI interfaces. This is intended so that
		// old and modern software which depends on certain display quirks can
		// run on any system.}
		String compatdmparms = System.getProperty(
			"cc.squirreljme.runtime.lcdui.compatibility");
		if (compatdmparms != null)
			dhp = new CompatibilityDisplayManager(dhp, compatdmparms);
		
		// Use this one as the manager
		DISPLAY_MANAGER = dhp;
	}
	
	/**
	 * Returns the display heads which are available on the system.
	 *
	 * @return The array of available display heads, this should always
	 * return the same elements.
	 * @since 2017/08/19
	 */
	public abstract DisplayHead[] heads();
	
	/**
	 * Returns the event queue.
	 *
	 * @return The event queue.
	 * @since 2017/10/24
	 */
	public final EventQueue eventQueue()
	{
		return this.eventqueue;
	}
	
	/**
	 * Returns the default display head.
	 *
	 * @return The default display head.
	 * @since 2017/10/20
	 */
	public static final DisplayHead defaultDisplayHead()
	{
		return DISPLAY_MANAGER.heads()[0];
	}
}

