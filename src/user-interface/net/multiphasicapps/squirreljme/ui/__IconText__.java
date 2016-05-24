// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This is a common class for handling elements which can be given an icon
 * and text.
 *
 * @since 2016/05/24
 */
final class __IconText__
	implements UIIconAndText
{
	/** Lock. */
	protected final Object lock;
	
	/** The outside icon and text element. */
	protected final UIIconAndText iconandtext;
	
	/** The current menu text. */
	private volatile String _text =
		"";
	
	/** The current icon used. */
	private volatile UIImage _icon;
	
	/**
	 * Initializes the icon and text.
	 *
	 * @param __it The Icon and text to set this for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/24
	 */
	__IconText__(UIIconAndText __it)
		throws NullPointerException
	{
		// Check
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.iconandtext = __it;
		lock = ((UIBase)__it).displayManager().__lock();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final UIImage getIcon()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return this._icon;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final String getText()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return this._text;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final UIImage setIcon(UIImage __icon)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// Get old
			UIImage rv = this._icon;
			
			// Set the new icon
			((UIBase)this.iconandtext).<PIIconAndText>__platform(
				PIIconAndText.class).setIcon(__icon);
			this._icon = __icon;
			
			// Return the old one
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final String setText(String __text)
		throws UIException
	{
		// If null, becomes blank
		if (__text == null)
			__text = "";
		
		// Lock
		synchronized (this.lock)
		{
			// Get old
			String rv = this._text;
			
			// Set the new text
			((UIBase)this.iconandtext).<PIIconAndText>__platform(
				PIIconAndText.class).setText(__text);
			this._text = __text;
			
			// Return it
			return rv;
		}
	}
}

