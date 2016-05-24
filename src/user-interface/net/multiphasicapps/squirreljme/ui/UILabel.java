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
 * This represents a label which can be placed in a container (such as a list).
 *
 * @since 2016/05/24
 */
public class UILabel
	extends UIComponent
	implements UIIconAndText
{
	/** Icon and text data. */
	private final __IconText__ _it =
		new __IconText__(this);
	
	/**
	 * Initializes the label.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/24
	 */
	public UILabel(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public UIImage getIcon()
		throws UIException
	{
		return this._it.getIcon();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public String getText()
		throws UIException
	{
		return this._it.getText();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public UIImage setIcon(UIImage __icon)
		throws UIException
	{
		return this._it.setIcon(__icon);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/24
	 */
	@Override
	public String setText(String __text)
		throws UIException
	{
		return this._it.setText(__text);
	}
}

