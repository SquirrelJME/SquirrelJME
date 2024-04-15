// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests that arrays of negative sizes cannot be made.
 *
 * @since 2022/06/26
 */
public class TestArrayNegativeSize
	extends TestConsumer<String>
	implements ArrayTypeMultiParameters
{
	/**
	 * {@inheritDoc}
	 * @since 2022/06/26
	 */
	@Override
	public void test(String __what)
	{
		Object array;
		switch (__what)
		{
			case "BOOLEAN":
				array = new boolean[TestArrayNegativeSize.__size()];
				break;
				
			case "BYTE":
				array = new byte[TestArrayNegativeSize.__size()];
				break;
				
			case "SHORT":
				array = new short[TestArrayNegativeSize.__size()];
				break;
				
			case "CHAR":
				array = new char[TestArrayNegativeSize.__size()];
				break;
				
			case "INT":
				array = new int[TestArrayNegativeSize.__size()];
				break;
				
			case "LONG":
				array = new long[TestArrayNegativeSize.__size()];
				break;
				
			case "FLOAT":
				array = new float[TestArrayNegativeSize.__size()];
				break;
				
			case "DOUBLE":
				array = new double[TestArrayNegativeSize.__size()];
				break;
				
			case "OBJECT":
				array = new Object[TestArrayNegativeSize.__size()];
				break;
			
			default:
				array = new Object[0];
				break;
		}
		
		this.secondary("array", array.getClass().toString());
	}
    
    /**
     * Returns a negative size.
     * 
     * @return A negative value.
     * @since 2022/06/26
     */
	private static int __size()
	{
		return System.identityHashCode(new Object()) | Integer.MIN_VALUE;
	}
}
