// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.service.ServiceDefinition;
import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class implements the base of the graphical LCDUI display system which
 * is used by the public facing LCDUI code to enable the use of graphics.
 *
 * All operations in the server are performed inside of a single lock because
 * LCDUI for the most part requires that behavior, additionally most operating
 * systems and display interfaces that exist only allow interaction with
 * graphical APIs in the same thread that is using them. Although this has a
 * speed loss, if most graphical operations are performed on buffers this will
 * not cause much issue.
 *
 * @since 2018/03/15
 */
public abstract class LcdDefinition
	extends ServiceDefinition
{
	/** The handler for requests to the LCD server. */
	protected final LcdRequestHandler requesthandler;
	
	/**
	 * Initializes the base definition.
	 *
	 * @param __rh The handler for requests.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public LcdDefinition(LcdRequestHandler __rh)
		throws NullPointerException
	{
		super(LcdServiceCall.Provider.class);
		
		if (__rh == null)
			throw new NullPointerException("NARG");
		
		this.requesthandler = __rh;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/15
	 */
	@Override
	public final ServiceServer newServer(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		return new LcdServer(__task, this.requesthandler);
	}
	
	/**
	 * Returns the request handler that is used to handle any requests made
	 * to the LCDUI interface.
	 *
	 * @return The request handler for events.
	 * @since 2018/03/17
	 */
	public final LcdRequestHandler requestHandler()
	{
		return this.requesthandler;
	}
}

