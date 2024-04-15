// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests that the {@link TaskShelf} tests invalid inputs properly.
 *
 * @since 2020/07/02
 */
public class TestTaskShelfInvalid
	extends __BaseMleErrorTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/02
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public boolean test(int __index)
		throws MLECallError
	{
		switch (__index)
		{
			case 0:
				TaskShelf.equals(null, null);
				break;
			
			case 1:
				TaskShelf.equals(null, TaskShelf.current());
				break;
			
			case 2:
				TaskShelf.equals(TaskShelf.current(), null);
				break;
			
			case 3:
				TaskShelf.exitCode(null);
				break;
			
			case 4:
				TaskShelf.getTrace(null, null);
				break;
			
			case 5:
				TaskShelf.getTrace(TaskShelf.current(), null);
				break;
			
			case 6:
				TaskShelf.getTrace(null, new String[1]);
				break;
			
			case 7:
				TaskShelf.getTrace(TaskShelf.current(), new String[0]);
				break;
				
			case 8:
				TaskShelf.status(null);
				break;
			
			case 9:
				TaskShelf.start(
					null,
					null,
					null,
					null,
					-1,
					-1);
				break;
			
			case 10:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					null,
					null,
					null,
					-1,
					-1);
				break;
			
			case 11:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					"javax.microedition.midlet.__MainHandler__",
					null,
					null,
					-1,
					-1);
				break;
			
			case 12:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					"javax.microedition.midlet.__MainHandler__",
					new String[0],
					null,
					-1,
					-1);
				break;
			
			case 13:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					"javax.microedition.midlet.__MainHandler__",
					new String[0],
					new String[]{"a"},
					-1,
					-1);
				break;
			
			case 14:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					"javax.microedition.midlet.__MainHandler__",
					new String[0],
					new String[]{"a", "b"},
					-1,
					-1);
				break;
			
			case 15:
				TaskShelf.start(
					JarPackageShelf.classPath(),
					"javax.microedition.midlet.__MainHandler__",
					new String[0],
					new String[]{"a", "b"},
					TaskPipeRedirectType.DISCARD,
					-1);
				break;
			
			case 16:
				TaskShelf.read(TestTaskShelfInvalid.__start(true),
					StandardPipeType.STDERR, new byte[1], 0, 1);
				break;
			
			case 17:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDOUT, new byte[1], 0, 1);
				break;
			
			case 18:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDERR, null, -1, -1);
				break;
			
			case 19:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDERR, new byte[12], -1, -1);
				break;
			
			case 20:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDERR, new byte[12], 2, -1);
				break;
			
			case 21:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDERR, new byte[12], -1, 12);
				break;
			
			case 22:
				TaskShelf.read(TestTaskShelfInvalid.__start(false),
					StandardPipeType.STDERR, new byte[12], 2, 12);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
	
	/**
	 * Starts a task.
	 * 
	 * @param __discardErr Should the error output be discarded?
	 * @return The task.
	 * @since 2020/07/02
	 */
	private static TaskBracket __start(boolean __discardErr)
	{
		return TaskShelf.start(
			JarPackageShelf.classPath(),
			"javax.microedition.midlet.__MainHandler__",
			new String[0],
			new String[]{"a", "b"},
			TaskPipeRedirectType.DISCARD,
			(__discardErr ? TaskPipeRedirectType.DISCARD :
				TaskPipeRedirectType.BUFFER));
	}
}
