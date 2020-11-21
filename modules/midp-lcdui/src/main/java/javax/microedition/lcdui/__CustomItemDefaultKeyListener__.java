// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Not Described.
 *
 * @since 2020/10/16
 */
public class __CustomItemDefaultKeyListener__
	implements KeyListener
{
	/** The item to reference. */
	protected final Reference<CustomItem> customItem;
	
	/**
	 * Initializes the key listener.
	 * 
	 * @param __cItem The canvas to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/16
	 */
	__CustomItemDefaultKeyListener__(CustomItem __cItem)
		throws NullPointerException
	{
		if (__cItem == null)
			throw new NullPointerException("NARG");
		
		this.customItem = new WeakReference<>(__cItem); 
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyPressed(int __kc, int __km)
	{
		CustomItem widget = this.customItem.get();
		if (widget != null)
			widget.keyPressed(__kc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyReleased(int __kc, int __km)
	{
		CustomItem widget = this.customItem.get();
		if (widget != null)
			widget.keyReleased(__kc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public final void keyRepeated(int __kc, int __km)
	{
		CustomItem widget = this.customItem.get();
		if (widget != null)
			widget.keyRepeated(__kc);
	}
}
