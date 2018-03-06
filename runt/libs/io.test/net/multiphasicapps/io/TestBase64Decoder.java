// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.tests.*;

/**
 * This tests the base 64 decoder.
 *
 * @since 2018/03/06
 */
public class TestBase64Decoder
{
	/**
	 * Tests that the decoder works.
	 *
	 * @param __in The input string.
	 * @return The resulting string.
	 * @since 2018/03/06	
	 */
	@DefaultParameters({
		@Parameters(
			returnValue=@Argument(type=ArgumentType.STRING,
				stringValue="Hello World!"),
			args={
				@Argument(type=ArgumentType.STRING,
					stringValue="SGVsbG8gV29ybGQh"),
				}),
		@Parameters(
			returnValue=@Argument(type=ArgumentType.STRING,
				stringValue="I love squirrels!"),
			args={
				@Argument(type=ArgumentType.STRING,
					stringValue="SSBsb3ZlIHNxdWlycmVscyE="),
				}),
		@Parameters(
			returnValue=@Argument(type=ArgumentType.STRING,
				stringValue="I love you!!!"),
			args={
				@Argument(type=ArgumentType.STRING,
					stringValue="SSBsb3ZlIHlvdSEhIQ=="),
				}),
		})
	public String testDecode(String __in)
	{
		throw new todo.TODO();
	}
}

