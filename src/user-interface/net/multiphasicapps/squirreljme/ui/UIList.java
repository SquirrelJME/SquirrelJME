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
 * This handles a list which contains items to be displayed, the displayed
 * items may have an icon and a text associated with them.
 *
 * @since 2016/05/24
 */
public class UIList
	extends UIComponent
	implements UIContainer<UILabel>
{
	/** This handles the containerization of labels. */
	private final __Container__<UIList, PIList, UILabel> _container =
		new __Container__<>(this.manager, this, UILabel.class);
	
	/**
	 * Initializes the icon box.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/24
	 */
	public UIList(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final void add(int __i, UILabel __l)
	{
		this._container.add(__i, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final UILabel get(int __i)
		throws UIException
	{
		return this._container.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/25
	 */
	@Override
	public final UILabel remove(int __i)
		throws UIException
	{
		return this._container.remove(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public final int size()
		throws UIException
	{
		return this._container.size();
	}
}

