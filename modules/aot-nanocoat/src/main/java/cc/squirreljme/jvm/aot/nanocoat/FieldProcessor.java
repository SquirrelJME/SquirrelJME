// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueClass;
import net.multiphasicapps.classfile.Field;

/**
 * Processor for fields.
 *
 * @since 2023/05/31
 */
public class FieldProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The output source writer. */
	protected final CSourceWriter out;
	
	/** The field being processed. */
	protected final Field field;
	
	/**
	 * Initializes the method processor.
	 * 
	 * @param __glob The link glob this is under.
	 * @param __out The source output.
	 * @param __classProcessor The class file this is processing under.
	 * @param __field The method to be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public FieldProcessor(NanoCoatLinkGlob __glob, CSourceWriter __out,
		ClassProcessor __classProcessor, Field __field)
	{
		if (__glob == null || __out == null || __classProcessor == null ||
			__field == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.out = __out;
		this.classFile = __classProcessor.classFile;
		this.field = __field;
	}
	
	/**
	 * Processes headers for field.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processHeader()
		throws IOException
	{
		// Nothing needs to be done currently
	}
	
	/**
	 * Processes field information outside the class struct.
	 *
	 * @param __array The array to write into.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public void processInFieldsStruct(CArrayBlock __array)
		throws IOException, NullPointerException
	{
		if (__array == null)
			throw new NullPointerException("NARG");
		
		Field field = this.field;
		try (CStructVariableBlock struct = __array.struct())
		{
			struct.memberSet("name",
				Utils.quotedString(field.name().toString()));
			struct.memberSet("type",
				Utils.quotedString(field.type().toString()));
			struct.memberSet("flags",
				field.flags().toJavaBits());
			
			// Constant value?
			ConstantValue value = field.constantValue();
			if (value != null)
			{
				struct.memberSet("valueType",
					"SJME_VALUETYPE_" + value.type().name());
				try (CStructVariableBlock valueStruct = struct.memberStructSet(
					"value"))
				{
					switch (value.type())
					{
						case INTEGER:
							valueStruct.memberSet("jint",
								value.boxedValue());
							break;
							
						case LONG:
							valueStruct.memberSet("jlong",
								value.boxedValue());
							break;
							
						case FLOAT:
							valueStruct.memberSet("jfloat",
								value.boxedValue());
							break;
							
						case DOUBLE:
							valueStruct.memberSet("jdouble",
								value.boxedValue());
							break;
							
						case STRING:
							valueStruct.memberSet("jstring",
								value);
							break;
							
						case CLASS:
							valueStruct.memberSet("jclass",
								((ConstantValueClass)value).className());
							break;
							
						default:
							throw Debugging.todo();
					}
				}
			}
		}
	}
}
