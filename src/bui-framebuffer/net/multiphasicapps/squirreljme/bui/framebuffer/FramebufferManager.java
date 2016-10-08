// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui.framebuffer;

import net.multiphasicapps.squirreljme.bui.BasicUIManager;

/**
 * This is used to manage framebuffers that are available to SquirrelJME.
 *
 * @since 2016/10/08
 */
public class FramebufferManager
	extends BasicUIManager<Framebuffer, FramebufferProvider>
{
	/** The instance of this manager. */
	private static FramebufferManager _INSTANCE =
		new FramebufferManager();
	
	/**
	 * Initializes the manager.
	 *
	 * @since 2016/10/08
	 */
	public FramebufferManager()
	{
		super(FramebufferProvider.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	protected Framebuffer[] newArray(int __len)
	{
		return new Framebuffer[__len];
	}
	
	/**
	 * Returns the single instance of the framebuffer manager.
	 *
	 * @return The global instance.
	 * @since 2016/10/08
	 */
	public static FramebufferManager instance()
	{
		return _INSTANCE;
	}
}

