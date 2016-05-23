// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.swing;

import java.awt.AWTError;
import java.awt.HeadlessException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.kernel.impl.jvm.javase.JVMJavaSEKernel;
import net.multiphasicapps.squirreljme.ui.PIBase;
import net.multiphasicapps.squirreljme.ui.PIDisplay;
import net.multiphasicapps.squirreljme.ui.PIManager;
import net.multiphasicapps.squirreljme.ui.PIMenu;
import net.multiphasicapps.squirreljme.ui.PIMenuItem;
import net.multiphasicapps.squirreljme.ui.UIBase;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIException;
import net.multiphasicapps.squirreljme.ui.UIImage;
import net.multiphasicapps.squirreljme.ui.UIManager;
import net.multiphasicapps.squirreljme.ui.UIMenu;
import net.multiphasicapps.squirreljme.ui.UIMenuItem;

/**
 * This is a display manager which interfaces with Java's Swing and uses it
 * to interact with the user.
 *
 * @since 2016/05/20
 */
public class SwingManager
	extends PIManager
{
	/** The kernel which created this. */
	protected final JVMJavaSEKernel kernel;
	
	/**
	 * Initializes the swing based display manager.
	 *
	 * @param __k The kernel which created this display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public SwingManager(JVMJavaSEKernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public PIDisplay createDisplay(Reference<UIDisplay> __ref)
		throws UIException
	{
		// Create it
		try
		{
			return new SwingDisplay(__ref);
		}
		
		// {@squirreljme.error AZ01 Could not create a display.}
		catch (AWTError|HeadlessException e)
		{
			throw new UIException("AZ01", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public PIMenu createMenu(Reference<UIMenu> __ref)
		throws UIException
	{
		return new SwingMenu(__ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public PIMenuItem createMenuItem(Reference<UIMenuItem> __ref)
		throws UIException
	{
		return new SwingMenuItem(__ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int[] preferredIconSizes()
		throws UIException
	{
		// Use common icon sizes used on desktop systems
		return new int[]
			{
				16, 16,
				20, 20,
				24, 24,
				32, 32,
				40, 40,
				48, 48,
				64, 64
			};
	}
	
	/**
	 * Obtains an internal element from an external one.
	 *
	 * @param <E> The type of element to obtain.
	 * @param __cl The type of element that is expected.
	 * @param __e The internal element to get the external element for.
	 * @return The internal element or {@code null}.
	 * @since 2016/05/22
	 */
	final <E extends PIBase> E __getInternal(Class<E> __cl,
		UIBase __e)
	{
		return super.<E>getInternal(__cl, __e);
	}
}

