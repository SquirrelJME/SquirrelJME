// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * String reference command set.
 *
 * @since 2021/03/20
 */
public enum StringReferenceCommandSet
	implements JDWPCommand
{
	/** Return the value of the string. */
	VALUE(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which object do we want?
			JDWPObject object = __controller.state.objects.get(
				__packet.readId());
			if (object == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			
			// Is this not a string?
			JDWPClass classy = object.debuggerClass();
			if (classy == null || !StringReferenceCommandSet._STRING
				.equals(classy.debuggerFieldDescriptor()))
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_STRING);
			
			// Find the internal string array
			JDWPField charField = null;
			for (JDWPField field : classy.debuggerFields())
				if (StringReferenceCommandSet._STRING_CHARS.equals(
					field.debuggerMemberName()))
				{
					charField = field;
					break;
				}
			
			// Is missing? We do not know how strings work then
			if (charField == null)
				return __controller.__reply(
					__packet.id(), ErrorType.NOT_IMPLEMENTED);
			
			// Load the field array
			JDWPArray charArray;
			try (JDWPValue value = __controller.__value())
			{
				// Cannot obtain this field?
				if (!classy.debuggerFieldValue(object,
					charField, value))
					return __controller.__reply(
						__packet.id(), ErrorType.INVALID_STRING);
				
				// This must be an array type
				Object raw = value.get();
				if (!(raw instanceof JDWPArray))
					return __controller.__reply(
						__packet.id(), ErrorType.INVALID_STRING);
				
				charArray = (JDWPArray)raw;
			}
			
			// How big is this string?
			int strLen = charArray.debuggerArrayLength();
			if (strLen < 0)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_STRING);
			
			// Load in characters
			char[] chars = new char[strLen];
			for (int i = 0; i < strLen; i++)
				try (JDWPValue value = __controller.__value())
				{
					// Use an invalid placeholder
					if (!charArray.debuggerArrayGet(i, value))
					{
						chars[i] = StringReferenceCommandSet._BAD_CHAR;
						continue;
					}
					
					// Try to map the value
					Object raw = value.get();
					if (raw instanceof Character)
						chars[i] = (char)raw;
					else if (raw instanceof Number)
						chars[i] = (char)((Number)raw).intValue();
					
					// Could not read it so treat as unknown
					else
						chars[i] = StringReferenceCommandSet._BAD_CHAR;
				}
			
			// Report string value
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeString(new String(chars));
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** Badly represented character. */
	private static final char _BAD_CHAR =
		0xFFFD;
	
	/** String character array. */
	private static final String _STRING_CHARS =
		"_chars";
	
	/** String class. */
	private static final String _STRING = 
		"Ljava/lang/String;";
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/20
	 */
	StringReferenceCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/20
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
