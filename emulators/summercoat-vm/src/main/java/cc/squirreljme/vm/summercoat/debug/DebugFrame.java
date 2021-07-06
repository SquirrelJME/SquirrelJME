// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat.debug;

import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.jdwp.views.JDWPViewFrame;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.summercoat.CPUFrame;
import cc.squirreljme.vm.summercoat.MachineState;
import java.lang.ref.Reference;

/**
 * Viewer for frames within threads.
 *
 * @since 2021/07/06
 */
public class DebugFrame
	extends DebugBase
	implements JDWPViewFrame
{
	/**
	 * Initializes the thread frame viewer.
	 * 
	 * @param __machine The machine used.
	 * @since 2021/07/06
	 */
	public DebugFrame(Reference<MachineState> __machine)
	{
		super(__machine);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public Object atClass(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public long atCodeIndex(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public long atLineIndex(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public int atMethodIndex(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof CPUFrame);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public boolean readValue(Object __which, int __index, JDWPValue __out)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/06
	 */
	@Override
	public int numValues(Object __which)
	{
		throw Debugging.todo();
	}
}
