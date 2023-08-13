// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.table.FieldTypeStaticTable;
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
	
	/** The field being processed. */
	protected final Field field;
	
	/**
	 * Initializes the method processor.
	 *
	 * @param __glob The link glob this is under.
	 * @param __classProcessor The class file this is processing under.
	 * @param __field The method to be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public FieldProcessor(NanoCoatLinkGlob __glob,
		ClassProcessor __classProcessor, Field __field)
	{
		if (__glob == null || __classProcessor == null ||
			__field == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.classFile = __classProcessor.classFile;
		this.field = __field;
	}
	
	/**
	 * Processes headers for field.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	public void processHeader(CSourceWriter __out)
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
		
		NanoCoatLinkGlob glob = this.glob;
		
		Field field = this.field;
		FieldTypeStaticTable fieldTypes = glob.tables.fieldType();
		try (CStructVariableBlock struct = __array.struct())
		{
			struct.memberSet("name",
				CBasicExpression.string(field.name().toString()));
			struct.memberSet("type",
				CBasicExpression.reference(fieldTypes.put(field.type())));
			struct.memberSet("flags",
				CBasicExpression.number(Constants.JINT_C,
					field.flags().toJavaBits()));
			
			// Constant value?
			ConstantValue value = field.constantValue();
			if (value != null)
			{
				struct.memberSet("valueType",
					CBasicExpression.number(value.type().ordinal()));
				try (CStructVariableBlock valueStruct = struct.memberStructSet(
					"value"))
				{
					switch (value.type())
					{
						case INTEGER:
							valueStruct.memberSet("jint",
								CExpressionBuilder.builder()
									.number(Constants.JINT_C,
										(Integer)value.boxedValue())
									.build());
							break;
							
						case LONG:
							try (CStructVariableBlock bits =
								 valueStruct.memberStructSet(
									 "jlong"))
							{
								long unboxed = (Long)value.boxedValue();
								bits.memberSet("hi",
									CBasicExpression.number(
										(int)(unboxed >>> 32)));
								bits.memberSet("lo",
									CBasicExpression.number(
										(int)unboxed));
							}
							break;
							
						case FLOAT:
							try (CStructVariableBlock bits =
								 valueStruct.memberStructSet(
									 "jfloat"))
							{
								int unboxed = Float.floatToRawIntBits(
									(Float)value.boxedValue());
								bits.memberSet("value",
									CBasicExpression.number(unboxed));
							}
							break;
							
						case DOUBLE:
							try (CStructVariableBlock bits =
								 valueStruct.memberStructSet(
									 "jdouble"))
							{
								long unboxed = Double.doubleToRawLongBits(
									(Double)value.boxedValue());
								bits.memberSet("hi",
									CBasicExpression.number(
										(int)(unboxed >>> 32)));
								bits.memberSet("lo",
									CBasicExpression.number(
										(int)unboxed));
							}
							break;
							
						case STRING:
							valueStruct.memberSet("jstring",
								CExpressionBuilder.builder()
									.string((String)value.boxedValue())
									.build());
							break;
							
						case CLASS:
							valueStruct.memberSet("jclass",
								CExpressionBuilder.builder()
									.string(((ConstantValueClass)value)
										.className().toString())
									.build());
							break;
							
						default:
							throw Debugging.todo();
					}
				}
			}
		}
	}
}
