// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.NativeArchiveShelf;
import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.CloseableBracket;
import cc.squirreljme.jvm.mle.brackets.NativeArchiveBracket;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * This manages the cleanup of anything related to applications, MIDlet related
 * methods, or any other method.
 *
 * @since 2020/07/03
 */
@SquirrelJMEVendorApi
public final class CleanupHandler
{
	/** Queue of handles waiting to be closed. */
	private static final Queue<AutoCloseable> _QUEUE =
		new LinkedList<>();
	
	/** The brackets to be closed. */
	private static final ReferenceQueue<? super Object> _BRACKET_QUEUE =
		new ReferenceQueue<>();
	
	/** The mapping of references to brackets for closing. */
	private static final Map<Reference<? super Object>,
		CloseableBracket> _BRACKETS =
		new LinkedHashMap<>();
	
	/**
	 * Not used.
	 * 
	 * @since 2020/07/03
	 */
	private CleanupHandler()
	{
	}
	
	/**
	 * Adds a task to be called when the MIDlet exits.
	 * 
	 * @param __task The task to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/03
	 */
	@SquirrelJMEVendorApi
	public static void add(AutoCloseable __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		synchronized (CleanupHandler.class)
		{
			CleanupHandler._QUEUE.add(__task);
		}
	}
	
	/**
	 * Adds a bracket to be cleaned up by reference.
	 *
	 * @param __object The object to be cleaned up.
	 * @param __bracket The bracket to "close" when the specified object is
	 * no longer being referenced by any other object.
	 * @since 2026/07/09
	 */
	@SquirrelJMEVendorApi
	public static void bracketAdd(Object __object, CloseableBracket __bracket)
		throws NullPointerException
	{
		if (__object == null || __bracket == null)
			throw new NullPointerException("NARG");
		
		// Need to set up a reference into this queue first
		Reference<? super Object> ref = new WeakReference<>(__object,
			CleanupHandler._BRACKET_QUEUE);
		
		// Then this gets registered into the map
		synchronized (CleanupHandler.class)
		{
			CleanupHandler._BRACKETS.put(ref, __bracket);
		}
	}
	
	/**
	 * Checks for any brackets that need to be cleaned up.
	 *
	 * @since 2026/07/09
	 */
	@SquirrelJMEVendorApi
	public static void bracketCheck()
	{
		Map<Reference<? super Object>, CloseableBracket> brackets =
			CleanupHandler._BRACKETS;
		
		// We need to pull from the reference queue anything that was GCed
		ReferenceQueue<? super Object> queue = CleanupHandler._BRACKET_QUEUE;
		for (;;)
		{
			// Grab the next reference, stop when none are left
			Reference<?> ref = queue.poll();
			if (ref == null)
				break;
			
			// Grab the bracket to close
			CloseableBracket bracket;
			synchronized (CleanupHandler.class)
			{
				// Get and clear from the map
				bracket = brackets.get(ref);
				brackets.remove(ref);
			}
			
			// If this is a valid bracket, then close it
			if (bracket != null)
				try
				{
					CleanupHandler.bracketClose(bracket);
				}
				catch (MLECallError __e)
				{
					__e.printStackTrace();
				}
		}
	}
	
	/**
	 * Closes the given bracket.
	 *
	 * @param __bracket The bracket to close.
	 * @throws MLECallError If the bracket could not be closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/07/09
	 */
	@SquirrelJMEVendorApi
	public static void bracketClose(CloseableBracket __bracket)
		throws MLECallError, NullPointerException
	{
		if (__bracket == null)
			throw new NullPointerException("NARG");
		
		// Close based on the type of the bracket
		Class<?> type = __bracket.getClass();
		if (AudioConnectionBracket.class.isAssignableFrom(type))
			AudioStreamShelf.disconnect((AudioConnectionBracket)__bracket);
		else if (NativeArchiveBracket.class.isAssignableFrom(type))
			NativeArchiveShelf.archiveClose((NativeArchiveBracket)__bracket);
		else if (PencilBracket.class.isAssignableFrom(type))
			PencilShelf.hardwareCloseGraphics((PencilBracket)__bracket);
		else if (PipeBracket.class.isAssignableFrom(type))
			TerminalShelf.close((PipeBracket)__bracket);
		else
			throw Debugging.todo(type);
	}
	
	/**
	 * Runs all the cleanup handlers and drains from the queue.
	 * 
	 * @since 2020/07/03
	 */
	@SquirrelJMEVendorApi
	public static void runAll()
	{
		// Check brackets
		CleanupHandler.bracketCheck();
		
		// Clear out the queue and drain everything to an array
		AutoCloseable[] drain;
		synchronized (CleanupHandler.class)
		{
			Queue<AutoCloseable> queue = CleanupHandler._QUEUE;
			drain = queue.<AutoCloseable>toArray(
				new AutoCloseable[queue.size()]);
			queue.clear();
		}
		
		// Close all those items now
		for (AutoCloseable closing : drain)
			try
			{
				closing.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
}
