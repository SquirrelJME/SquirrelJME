// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a queue which manages which handles displayable which are bound to
 * the remote server, but when they are garbage collected allows them to be
 * cleaned up by the server accordingly.
 *
 * @since 2018/03/17
 */
final class __Queue__
	implements Runnable
{
	/** Single queue instance. */
	static final __Queue__ INSTANCE =
		new __Queue__();
		
	/** Internal queue access lock. */
	protected final Object lock =
		new Object();
	
	/** Reference to index mapping, for sending to the server. */
	protected final Map<Reference<__Cleanupable__>, Integer> _distoid =
		new HashMap<>();
	
	/** Index to cleanupable so the client knows what the server wanted. */
	protected final Map<Integer, Reference<__Cleanupable__>> _idtodis =
		new HashMap<>();
	
	/** Queue to tell the remote server that handles should be cleaned up. */
	protected final ReferenceQueue<__Cleanupable__> _disqueue =
		new ReferenceQueue<>();
	
	/** Terminate the queue? */
	private volatile boolean _terminate;
	
	/**
	 * Internally initialized.
	 *
	 * @since 2018/03/17
	 */
	private __Queue__()
	{
		Thread cleanuper = new Thread(this, "LCDUI-Cleanup-Thread");
		SystemCall.EASY.setDaemonThread(cleanuper);
		cleanuper.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public void run()
	{
		Object lock = this.lock;
		ReferenceQueue<__Cleanupable__> disqueue = this._disqueue;
		Map<Reference<__Cleanupable__>, Integer> distoid = this._distoid;
		Map<Integer, Reference<__Cleanupable__>> idtodis = this._idtodis;
		
		// Loop forever looking for displayable that are no longer
		// referenced ever
		for (;;)
		{
			// Terminate thread?
			if (this._terminate)
				return;
			
			// Get the next reference which went away
			Reference<? extends __Cleanupable__> bye;
			try
			{
				bye = disqueue.remove();
			}
			
			// Ignore
			catch (InterruptedException e)
			{
				continue;
			}
			
			// Remove
			int svdx;
			synchronized (lock)
			{
				// Only cleanup valid references
				Integer dx = distoid.get(bye);
				if (dx == null)
					continue;
				
				// Remove from mappings
				distoid.remove(bye);
				idtodis.remove(dx);
				
				// The server is notified of this
				svdx = dx;
			}
			
			// If the server failed to clean it up properly then just ignore it
			try
			{
				LcdServiceCall.<VoidType>call(VoidType.class,
					LcdFunction.CLEANUPABLE_CLEANUP, svdx);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the displayable used for the given index.
	 *
	 * @param <X> The type of cleanupable to get.
	 * @param __cl The type of cleanupable to get.
	 * @param __dx The index to get.
	 * @return The cleanupable for the given index.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	final <X extends __Cleanupable__> X __get(Class<X> __cl, int __dx)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		Map<Integer, Reference<__Cleanupable__>> idtodis = this._idtodis;
		synchronized (this.lock)
		{
			Reference<__Cleanupable__> ref = idtodis.get(__dx);
			
			// Do not know what this is?
			if (ref == null)
				return;
			__Cleanupable__ rv = ref.get();
			
			// If this is not the right kind of class, ignore
			if (!__cl.isInstance(rv))
				return null;
			return __cl.cast(rv);
		}
	}
	
	/**
	 * Registers the given cleanupable and returns the remote handle to it.
	 *
	 * @param __d The cleanupable to register.
	 * @return The handle of the cleanupable on the remote end.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	final int __register(__Cleanupable__ __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// The remote index
		int dx;
		if (__d instanceof Displayable)
		{
			// Determine the type of displayable this is first
			DisplayableType type;
			if (__d instanceof Canvas)
				type = DisplayableType.CANVAS;
			else if (__d instanceof Alert)
				type = DisplayableType.ALERT;
			else if (__d instanceof FileSelector)
				type = DisplayableType.FILE_SELECTOR;
			else if (__d instanceof Form)
				type = DisplayableType.FORM;
			else if (__d instanceof List)
				type = DisplayableType.LIST;
			else if (__d instanceof TabbedPane)
				type = DisplayableType.TABBED_PANE;
			else if (__d instanceof TextBox)
				type = DisplayableType.TEXT_BOX;
			
			// {@squirreljme.error EB1x Could not determine the type displayable
			// that this is. (The displayable type)}
			else
				throw new RuntimeException(String.format(
					"EB1x %s", __d.getClass()));
			
			// Register and get the index for it
			dx = LcdServiceCall.<Integer>call(Integer.class,
				LcdFunction.CREATE_DISPLAYABLE, type);
		}
		
		// Do not know what to do here
		else
			throw new RuntimeException("OOPS");
		
		// Reference the cleanupable for future cleanup on the remote end
		Map<Reference<__Cleanupable__>, Integer> distoid = this._distoid;
		Map<Integer, Reference<__Cleanupable__>> idtodis = this._idtodis;
		synchronized (this.lock)
		{
			Reference<__Cleanupable__> ref =
				new WeakReference<>(__d, this._disqueue);
			Integer idx = dx;
			distoid.put(ref, idx);
			idtodis.put(idx, ref);
		}
		
		// The displayable uses this index to interact with the server
		return dx;
	}
}

