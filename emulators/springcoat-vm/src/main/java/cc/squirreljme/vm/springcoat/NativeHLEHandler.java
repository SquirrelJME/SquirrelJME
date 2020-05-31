// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;

/**
 * This contains the native HLE handler for SpringCoat, all functions that
 * are performed on the MLE layer will be truly implemented here.
 *
 * @since 2020/05/30
 */
public final class NativeHLEHandler
{
	/** Reference shelf class prefix. */
	private static final String REFERENCE_SHELF_PREFIX =
		"cc/squirreljme/jvm/mle/ReferenceShelf::";
	
	/** Prefix length. */
	private static final int REFERENCE_SHELF_PREFIX_LEN =
		NativeHLEHandler.REFERENCE_SHELF_PREFIX.length();
	
	/**
	 * Not used.
	 *
	 * @since 2020/05/30
	 */
	private NativeHLEHandler()
	{
	}
	
	/**
	 * @param __link The link to set in.
	 * @param __object The object to set to.
	 */
	private static void refLinkLinkSetObject(RefLinkBracketObject __link,
		SpringObject __object)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Creates a new reference link.
	 *
	 * @return The newly created reference link.
	 * @since 2020/05/30
	 */
	private static RefLinkBracketObject refLinkNewLink()
	{
		return new RefLinkBracketObject();
	}
	
	/**
	 * Handles the dispatching of the native method.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatch(SpringThreadWorker __thread, String __func,
		Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		// Reference calls
		if (__func.startsWith(NativeHLEHandler.REFERENCE_SHELF_PREFIX))
			return NativeHLEHandler.dispatchReference(__thread,
				__func.substring(NativeHLEHandler.REFERENCE_SHELF_PREFIX_LEN),
				__args);
		
		// Currently Unsupported
		else
			throw new SpringVirtualMachineException(String.format(
				"Unknown MLE native call: %s %s", __func,
				Arrays.asList(__args)));
	}
	
	/**
	 * Handles the dispatching of reference shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatchReference(SpringThreadWorker __thread,
		String __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
			case "newLink:()Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;":
				return NativeHLEHandler.refLinkNewLink();
			
			case "linkSetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
				"RefLinkBracket;Ljava/lang/Object;)V":
				NativeHLEHandler.refLinkLinkSetObject(
					(RefLinkBracketObject)__args[0],
					(SpringObject)__args[1]);
				return null;
				
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Reference MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
}
