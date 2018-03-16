// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.lcdui.DisplayHead;
import cc.squirreljme.runtime.lcdui.DisplayManager;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;

/**
 * This class contains an implementation of a display manager which wraps an
 * existing display manager and is used for compatibility purposes. It is
 * intended to be used for software which is not fully compatible with
 * potential displays across a number of devices.
 *
 * For old software, it can allow programs which are unable to handle the
 * more modern displays by providing scaling and color limitations.
 *
 * For new software, it allows software that does not really support older
 * more limited displays.
 *
 * Note that this class does have overhead and there will be a loss of speed
 * so it is recommended to not use it unless there is a problem running
 * software.
 *
 * @since 2017/10/20
 */
@Deprecated
public class CompatibilityDisplayManager
	extends DisplayManager
{
	/** The display manager to wrap with a compatibility one. */
	protected final DisplayManager wrapped;
	
	/** The parameters for the display. */
	protected final CompatibilityParameters parameters;
	
	/**
	 * Initializes the compatible display manager.
	 *
	 * @param __dm The display manager to wrap.
	 * @param __parms The parameters for the display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public CompatibilityDisplayManager(DisplayManager __dm, String __parms)
		throws NullPointerException
	{
		this(__dm, new CompatibilityParameters(__parms));
	}
	
	/**
	 * Initializes the compatible display manager.
	 *
	 * @param __dm The display manager to wrap.
	 * @param __parms The parameters for the display manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public CompatibilityDisplayManager(DisplayManager __dm,
		CompatibilityParameters __parms)
		throws NullPointerException
	{
		if (__dm ==	null || __parms == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __dm;
		this.parameters = __parms;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	public DisplayHead[] heads()
	{
		throw new todo.TODO();
	}
}

