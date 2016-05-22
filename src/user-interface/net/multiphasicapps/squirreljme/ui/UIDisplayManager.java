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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a class which provides the ability to create {@link UIDisplay}s as
 * needed to interact and show information to the user. A display manager can
 * create multiple displays at the same time, however there may be limitations
 * as to the number of displays which are active at any one time. For example,
 * on modern desktop systems there may be a single window for each unique
 * display, while on a mobile device there might only be just a single display
 * at a time.
 *
 * This wraps an {@link InternalDisplayManager} so that if it lacks
 * specific functionality it can be emulated by the front layer set of classes.
 *
 * @since 2016/05/20
 */
public final class UIDisplayManager
	extends UIElement
{
	/** The internal display manager to wrap. */
	protected final InternalDisplayManager internal;
	
	/** The UI element cleanup thread. */
	protected final Thread cleanupthread;
	
	/** The reference queue for element cleanup. */
	protected final ReferenceQueue<? extends UIElement> rqueue =
		new ReferenceQueue<>();
	
	/** The mapping between references and internal elements. */
	protected final Map<Reference<? extends UIElement>, InternalElement>
		elements =
		new HashMap<>();
	
	/**
	 * Initializes the display manager which wraps the internal representation
	 * of the user interface.
	 *
	 * @param __f The factory which creates internal display managers.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the display manager could not be created.
	 * @since 2016/05/21
	 */
	public UIDisplayManager(Factory __f)
		throws NullPointerException, UIException
	{
		super(null);
		
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Create internal display manager
		InternalDisplayManager internal = __f.create(
			new WeakReference<>(this));
		this.internal = internal;
		
		// Setup cleanup thread
		Thread cleanupthread = new Thread(new __Cleanup__());
		this.cleanupthread = cleanupthread;
		cleanupthread.start();
	}
	
	/**
	 * Creates a new display.
	 *
	 * @return The newly created display.
	 * @throws UIException If the display could not be created.
	 * @since 2016/05/21
	 */
	public UIDisplay createDisplay()
		throws UIException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Registers that a new element was created under the UI and adds it
	 * and its reference to the internal mapping.
	 *
	 * @param __ref The reference to the external element.
	 * @param __ie The internal element representation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	final void __newElement(Reference<? extends UIElement> __ref,
		InternalElement __ie)
		throws NullPointerException
	{
		// Check
		if (__ref == null || __ie == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<Reference<? extends UIElement>, InternalElement> e =
			this.elements;
		synchronized (e)
		{
			// Add it
			e.put(__ref, __ie);
		}
	}
	
	/**
	 * The factory interface for creating internal display managers.
	 *
	 * @since 2016/05/22
	 */
	public static interface Factory
	{
		/**
		 * Creates a new internal display manager.
		 *
		 * @param __ref The reference to the display manager.
		 * @return The newly created display manager.
		 * @throws UIException If it could not be created.
		 * @since 2016/05/22
		 */
		public abstract InternalDisplayManager create(
			Reference<UIDisplayManager> __ref)
			throws UIException;
	}
	
	/**
	 * This is the cleanup thread which removes elements and garbage collects
	 * them so that native resources (if any) are freed automatically.
	 *
	 * @since 2016/05/22
	 */
	private	final class __Cleanup__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/05/22
		 */
		@Override
		public void run()
		{
			// Infinite loop
			for (;;)
			{
				// Sleep for a bit
				try
				{
					Thread.sleep(3_000L);
				}
				
				// Yield instead
				catch (InterruptedException e)
				{
					Thread.yield();
				}
			}
		}
	}
}

