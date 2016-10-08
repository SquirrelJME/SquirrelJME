// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui.framebuffer.javase;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.bui.framebuffer.Framebuffer;
import net.multiphasicapps.squirreljme.bui.framebuffer.FramebufferProvider;

/**
 * This is a provider that provides Java SE backed framebuffer that utilize
 * Swing to provide a common user interface.
 *
 * @since 2016/10/08
 */
public class JavaSEFramebufferProvider
	implements FramebufferProvider
{
	/** The pre-existing framebuffer. */
	private volatile Reference<Framebuffer> _framebuffer;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public Framebuffer[] uis()
	{
		// Get
		Reference<Framebuffer> ref = this._framebuffer;
		Framebuffer rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._framebuffer = new WeakReference<>(
				(rv = new JavaSEFramebuffer()));
		
		// Fill
		return new Framebuffer[]{rv};
	}
}

