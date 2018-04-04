// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.LcdException;
import cc.squirreljme.runtime.lcdui.ui.UiCollectable;
import cc.squirreljme.runtime.lcdui.ui.UiDisplayHead;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class is the base manager for displays which are available for the
 * LCDUI subsystem which is used to contain displayables to be shown to the
 * user.
 *
 * This also is the handler for any requests that are made to the LCD server,
 * this enables client threads to call into the server from any thread while
 * the GUI remains in a single thread at all time.
 *
 * It is up to the implementation to properly thread in these events as they
 * are used.
 *
 * @since 2018/03/17
 */
public abstract class LcdDisplays
{
	/** Displays which are currently available. */
	private final Map<Integer, UiDisplayHead> _displays =
		new SortedTreeMap<>();
	
	/**
	 * Internally creates the specified collectable.
	 *
	 * @param __handle The handle to use for the collectable.
	 * @param __type The type of collectable to create.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	protected abstract UiCollectable internalCreateCollectable(int __handle,
		CollectableType __type)
		throws NullPointerException;
	
	/**
	 * Internally queries the displays which are present.
	 *
	 * @param __k An array containing the displays which are currently known
	 * about, this may be used to determine if any need to be re-initialized.
	 * @return An array containing the displays that are present.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	protected abstract UiDisplayHead[] internalQueryDisplays(
		UiDisplayHead[] __k)
		throws NullPointerException;
	
	/**
	 * Invokes the specified request at a future time within the thread which
	 * is not specifically known.
	 *
	 * @param __r The request to handle.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public abstract void invokeLater(LcdRequest __r)
		throws NullPointerException;
	
	/**
	 * Invokes the specified request as soon as possible and blocks until
	 * execution has finished.
	 *
	 * If the thread executing this method is the event handler thread then
	 * this must directly execute the request and return the result of it.
	 *
	 * @param <R> The class type to return.
	 * @param __cl The class type to return.
	 * @param __r The request to handle.
	 * @return The result of the request;
	 * @throws InterruptedException If the thread was interrupted while it
	 * was waiting.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public abstract <R> R invokeNow(Class<R> __cl, LcdRequest __r)
		throws InterruptedException, NullPointerException;
	
	/**
	 * Returns the specified display.
	 *
	 * @param __dx The display to get.
	 * @return The display.
	 * @throws LcdException If the display does not exist.
	 * @since 2018/03/18
	 */
	public final UiDisplayHead get(int __dx)
		throws LcdException
	{
		// {@squirreljme.error EB22 The specified display index is not
		// valid. (The display index)}
		Map<Integer, UiDisplayHead> displays = this._displays;
		UiDisplayHead rv = displays.get(__dx);
		if (rv == null)
			throw new LcdException(String.format("EB22 %d", __dx));
		return rv;
	}
	
	/**
	 * Queries all of the displays which are available for usage.
	 *
	 * @return The displays which are available.
	 * @since 2018/03/17
	 */
	public final UiDisplayHead[] queryDisplays()
	{
		Map<Integer, UiDisplayHead> displays = this._displays;
		
		// Get the displays which are known to the definition so that
		// lookup does not create any duplicates ones potentially
		UiDisplayHead[] known = displays.values().<LcdDisplay>toArray(
			new UiDisplayHead[displays.size()]);
		
		// Query all native displays
		UiDisplayHead[] active = this.internalQueryDisplays(known);
		
		// Cache all displays by their index
		for (UiDisplayHead d : active)
		{
			Integer dx = d.handle();
			
			// If a display is not known about then make it known
			if (!displays.containsKey(dx))
				displays.put(dx, d);
		}
		
		// Return every display which is known about
		return displays.values().<UiDisplayHead>toArray(
			new UiDisplayHead[displays.size()]);
	}
	
	/**
	 * Forwards collectable created to internal display logic.
	 *
	 * @param __handle The handle to use for the widget.
	 * @param __type The type of collectable to create.
	 * @since 2018/03/24
	 */
	final UiCollectable __internalCreateCollectable(int __handle,
		CollectableType __type)
	{
		return this.internalCreateCollectable(__handle, __type);
	}
}

