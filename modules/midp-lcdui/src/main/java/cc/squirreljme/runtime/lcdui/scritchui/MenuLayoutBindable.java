// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEventLoopInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Menu;
import org.jetbrains.annotations.Async;

/**
 * Base class for menu components that can be bound to a ScritchUI bracket.
 * 
 * Because {@link Menu}s and {@link Command}s can be added to multiple
 * different {@link Displayable}s this keeps track of this and maps each,
 * accordingly to a single ScritchUI bracket.
 *
 * @param <M> The MIDP menu type.
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public abstract class MenuLayoutBindable<M>
{
	/** The event loop interface. */
	protected final ScritchEventLoopInterface loop;
	
	/** The item this is bound to. */
	protected final WeakReference<M> item;
	
	/** Triggers that an update should occur. */
	private volatile boolean _triggerUpdate;
	
	/**
	 * Initializes the bindable.
	 *
	 * @param __loop The loop interface.
	 * @param __item The item this is bound to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/18
	 */
	protected MenuLayoutBindable(ScritchEventLoopInterface __loop,
		M __item)
		throws NullPointerException
	{
		if (__loop == null)
			throw new NullPointerException("NARG");
		
		this.loop = __loop;
		this.item = new WeakReference<>(__item);
	}
	
	/**
	 * Performs recursive refreshing of this bindable.
	 *
	 * @throws IllegalStateException If this is called outside the event
	 * loop.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	public abstract void refresh()
		throws IllegalStateException;
	
	/**
	 * Returns the MIDP item for this bindable.
	 *
	 * @return The MIDP item for this item.
	 * @throws IllegalStateException If this was garbage collected.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public final M getMidp()
		throws IllegalStateException
	{
		M result = this.item.get();
		if (result == null)
			throw new IllegalStateException("GCGC");
		return result;
	}
	
	/**
	 * Triggers that an update to a menu should occur.
	 * 
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	@Async.Schedule
	protected final void triggerUpdate()
	{
		// Set trigger flag
		synchronized (this)
		{
			// Set that we want to trigger
			this._triggerUpdate = true;
			
			// Enqueue an update
			this.__execTriggerEnqueue();
		}
	}
	
	/**
	 * Performs the actual updating logic, which should be called in one
	 * entire chunk.
	 *
	 * @since 2024/07/18
	 */
	@Async.Execute
	final void __execTrigger()
	{
		synchronized (this)
		{
			// Perform recursive updating
			boolean trigger = this._triggerUpdate;
			if (trigger)
				try
				{
					throw Debugging.todo();
				}
				
				// Clear trigger tag
				finally
				{
					this._triggerUpdate = false;
				}
		}
	}
	
	/**
	 * Enqueues a trigger update.
	 *
	 * @since 2024/07/18
	 */
	@Async.Schedule
	final void __execTriggerEnqueue()
	{
		synchronized (this)
		{
			// Do not run if there is no need to update
			boolean trigger = this._triggerUpdate;
			if (trigger == false)
				return;
			
			this.loop.executeLater(new __ExecMenuLayoutTrigger__(this.loop,
				this, false));
		}
	}
}
