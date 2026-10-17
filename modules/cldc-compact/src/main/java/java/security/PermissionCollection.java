// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Enumeration;

/**
 * This contains a collection of {@link Permissions}, in SquirrelJME this class
 * only exists for legacy compatibility and should not be used.
 *
 * @see SecurityManager
 * @since 2026/10/06
 */
@Api
@ApiDefinedDeprecated
@KeepAbsolutelyEverything("Lightweight 'Security' permission system.")
public abstract class PermissionCollection
{
	@Api
	@ApiDefinedDeprecated
	public PermissionCollection()
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
	public abstract void add(Permission __a);
	
	@Api
	@ApiDefinedDeprecated
	public abstract Enumeration<Permission> elements();
	
	@Api
	@ApiDefinedDeprecated
	public abstract boolean implies(Permission __a);
	
	@Api
	@ApiDefinedDeprecated
	public boolean isReadOnly()
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
	public void setReadOnly()
	{
		throw Debugging.todo();
	}
	
	@Override
	@ApiDefinedDeprecated
	public String toString()
	{
		throw Debugging.todo();
	}
}

