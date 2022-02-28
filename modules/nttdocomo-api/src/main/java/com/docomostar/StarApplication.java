// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.docomostar;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.UndeterminedArgumentType;
import cc.squirreljme.runtime.cldc.debug.UndeterminedReturnType;
import cc.squirreljme.runtime.nttdocomo.CommonDoJaApplication;

public abstract class StarApplication
	implements CommonDoJaApplication
{
	public static final UndeterminedReturnType LAUNCHED_AFTER_DOWNLOAD =
		Debugging.<UndeterminedReturnType>todoObject();
	
	public static final UndeterminedReturnType LAUNCHED_FROM_EXT =
		Debugging.<UndeterminedReturnType>todoObject();
	
	public static final UndeterminedReturnType LAUNCHED_FROM_TIMER =
		Debugging.<UndeterminedReturnType>todoObject();
	
	public abstract void started(int __unknownInt);
	
	public UndeterminedReturnType activated(UndeterminedArgumentType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType addApplicationListener(
		UndeterminedReturnType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType addEventListener(
		UndeterminedReturnType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType changeAppType(
		UndeterminedArgumentType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType getAppState(
		UndeterminedArgumentType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType stateChanged(
		UndeterminedArgumentType __unknown)
	{
		throw Debugging.todo();
	}
	
	public UndeterminedReturnType suspend(
		UndeterminedArgumentType __unknown)
	{
		throw Debugging.todo();
	}
	
	public void terminate()
	{
		throw Debugging.todo();
	}
}
