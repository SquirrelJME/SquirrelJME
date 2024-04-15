// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * Encodes character data to ISO-8859-15.
 *
 * @since 2019/04/29
 */
public final class ISO885915Encoder
	implements Encoder
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final double averageSequenceLength()
	{
		return 1.0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public int encode(char __c, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Always encodes to one character, so if one character cannot fit in
		// the buffer then fail
		if (__l < 1)
			return -1;
			
		// Remap some characters?
		switch (__c)
		{
				// Convert
			case 0x20AC:	__c = (char)0x00A4; break;
			case 0x0160:	__c = (char)0x00A6; break;
			case 0x0161:	__c = (char)0x00A8; break;
			case 0x017D:	__c = (char)0x00B4; break;
			case 0x017E:	__c = (char)0x00B8; break;
			case 0x0152:	__c = (char)0x00BC; break;
			case 0x0153:	__c = (char)0x00BD; break;
			case 0x0178:	__c = (char)0x00BE; break;
			
				// Cannot be encoded
			case 0x00A4:
			case 0x00A6:
			case 0x00A8:
			case 0x00B4:
			case 0x00B8:
			case 0x00BC:
			case 0x00BD:
			case 0x00BE:	__c = (char)0xFFFD; break;
		}
		
		// These characters are invalid, so they all become the replacement
		// character
		if (__c >= 0x100)
			__b[__o] = '?';
		
		// Encode as is
		else
			__b[__o] = (byte)__c;
		
		// Only single characters written
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final String encodingName()
	{
		return "iso-8859-15";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/29
	 */
	@Override
	public final int maximumSequenceLength()
	{
		return 1;
	}
}

