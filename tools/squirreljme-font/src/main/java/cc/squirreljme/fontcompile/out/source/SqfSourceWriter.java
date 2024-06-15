// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.source;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CArrayType;
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpression;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CPrimitiveType;
import cc.squirreljme.c.CStructKind;
import cc.squirreljme.c.CStructTypeBuilder;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CType;
import cc.squirreljme.c.CTypeDefType;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.c.out.AppendableCTokenOutput;
import cc.squirreljme.c.out.PrettyCTokenOutput;
import cc.squirreljme.fontcompile.out.SqfWriter;
import cc.squirreljme.fontcompile.out.struct.SqfFontStruct;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Writes a SQF structure as C source code.
 *
 * @since 2024/06/04
 */
public class SqfSourceWriter
	implements SqfWriter
{
	/** String type. */
	private static final CType _TYPE_LPCSTR =
		CTypeDefType.of(CPrimitiveType.CONST_CHAR_STAR,
			"sjme_lpcstr");
	
	/** Byte type. */
	private static final CType _TYPE_BYTE =
		CTypeDefType.of(CPrimitiveType.SIGNED_CHAR,
			"sjme_jbyte");
	
	/** Byte pointer type. */
	private static final CType _TYPE_BYTE_PC =
		SqfSourceWriter._TYPE_BYTE.constType().pointerType();
	
	/** Short pointer type. */
	private static final CType _TYPE_SHORT_PC =
		CTypeDefType.of(CPrimitiveType.SIGNED_SHORT,
			"sjme_jshort").constType().pointerType();
	
	/** Integer type. */
	private static final CType _TYPE_INT =
		CTypeDefType.of(CPrimitiveType.SIGNED_INTEGER,
			"sjme_jint");
	
	/** Huffman bits variable. */
	private static final CVariable _VAR_HUFF_BITS =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "huffBits");
	
	/** Character widths variable. */
	private static final CVariable _VAR_CHAR_WIDTHS =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charWidths");
	
	/** X offsets variable. */
	private static final CVariable _VAR_CHAR_X_OFFSETS =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charXOffset");
	
	/** Y offsets variable. */
	private static final CVariable _VAR_CHAR_Y_OFFSETS =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charYOffset");
	
	/** Character flags variable. */
	private static final CVariable _VAR_CHAR_FLAGS =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charFlags");
	
	/** Character bitmap offset variable. */ 
	private static final CVariable _VAR_CHAR_BMP_OFFSETS =
		CVariable.of(SqfSourceWriter._TYPE_SHORT_PC, "charBmpOffset");
	
	/** Character bitmap scan variable. */
	private static final CVariable _VAR_CHAR_BMP_SCAN =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charBmpScan");
	
	/** Character bitmap variable. */
	private static final CVariable _VAR_CHAR_BMP =
		CVariable.of(SqfSourceWriter._TYPE_BYTE_PC, "charBmp");
	
	/** SQF Structure type. */
	private static final CType _TYPE_SQF =
		CStructTypeBuilder.builder(CStructKind.STRUCT, "sjme_sqf")
			.member(SqfSourceWriter._TYPE_LPCSTR, "name")
			.member(SqfSourceWriter._TYPE_INT, "family")
			.member(SqfSourceWriter._TYPE_INT, "pixelHeight")
			.member(SqfSourceWriter._TYPE_INT, "ascent")
			.member(SqfSourceWriter._TYPE_INT, "descent")
			.member(SqfSourceWriter._TYPE_INT, "bbx")
			.member(SqfSourceWriter._TYPE_INT, "bby")
			.member(SqfSourceWriter._TYPE_INT, "bbw")
			.member(SqfSourceWriter._TYPE_INT, "bbh")
			.member(SqfSourceWriter._TYPE_INT, "codepointStart")
			.member(SqfSourceWriter._TYPE_INT, "codepointCount")
			.member(SqfSourceWriter._TYPE_INT, "huffBitsSize")
			.member(SqfSourceWriter._TYPE_INT, "charBmpSize")
			.member(SqfSourceWriter._VAR_HUFF_BITS)
			.member(SqfSourceWriter._VAR_CHAR_WIDTHS)
			.member(SqfSourceWriter._VAR_CHAR_X_OFFSETS)
			.member(SqfSourceWriter._VAR_CHAR_Y_OFFSETS)
			.member(SqfSourceWriter._VAR_CHAR_FLAGS)
			.member(SqfSourceWriter._VAR_CHAR_BMP_OFFSETS)
			.member(SqfSourceWriter._VAR_CHAR_BMP_SCAN)
			.member(SqfSourceWriter._VAR_CHAR_BMP)
			.build();
	
	/** SQF Codepage structure type. */
	private static final CType _TYPE_SQF_CODEPAGE =
		CStructTypeBuilder.builder(CStructKind.STRUCT,
			"sjme_sqf_codepage")
			.member(SqfSourceWriter._TYPE_LPCSTR, "name")
			.member(SqfSourceWriter._TYPE_INT, "numCodepages")
			.member(SqfSourceWriter._TYPE_SQF.constType().pointerType(),
				"codepages")
			.build();
	
	/** The resultant output. */
	protected final CFile cFile;
	
	/**
	 * Initializes the SQF writer.
	 *
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/04
	 */
	public SqfSourceWriter(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		try
		{
			this.cFile = new CFile(new PrettyCTokenOutput(
				new AppendableCTokenOutput(new PrintStream(__out,
					true, "utf-8"))));
		}
		catch (UnsupportedEncodingException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/04
	 */
	@Override
	public void close()
		throws IOException
	{
		this.cFile.flush();
		this.cFile.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/04
	 */
	@Override
	public void write(SqfFontStruct __struct)
		throws IOException, NullPointerException
	{
		if (__struct == null)
			throw new NullPointerException("NARG");
		
		CFile cFile = this.cFile;
		
		// Base name of constants
		String baseName = SqfSourceWriter.__baseName(__struct);
		
		String identHuffBits = baseName + "_huffBits";
		cFile.define(SqfSourceWriter._VAR_HUFF_BITS.rename(
			identHuffBits).staticize(),
			CBasicExpression.array(__struct.huffBits()));
		cFile.newLine(true);
		
		String identCharWidths = baseName + "_charWidths";
		cFile.define(SqfSourceWriter._VAR_CHAR_WIDTHS.rename(
			identCharWidths).staticize(),
			CBasicExpression.array(__struct.charWidths()));
		cFile.newLine(true);
		
		String identCharXOffset = baseName + "_charXOffset";
		cFile.define(SqfSourceWriter._VAR_CHAR_X_OFFSETS.rename(
			identCharXOffset).staticize(),
			CBasicExpression.array(__struct.charXOffset()));
		cFile.newLine(true);
		
		String identCharYOffset = baseName + "_charYOffset";
		cFile.define(SqfSourceWriter._VAR_CHAR_Y_OFFSETS.rename(
			identCharYOffset).staticize(),
			CBasicExpression.array(__struct.charYOffset()));
		cFile.newLine(true);
		
		String identCharFlags = baseName + "_charFlags";
		cFile.define(SqfSourceWriter._VAR_CHAR_FLAGS.rename(
			identCharFlags).staticize(),
			CBasicExpression.array(__struct.charFlags()));
		cFile.newLine(true);
		
		String identCharBmpOffset = baseName + "_charBmpOffset";
		cFile.define(SqfSourceWriter._VAR_CHAR_BMP_OFFSETS.rename(
			identCharBmpOffset).staticize(),
			CBasicExpression.array(__struct.charBmpOffset()));
		cFile.newLine(true);
		
		String identCharBmpScan = baseName + "_charBmpScan";
		cFile.define(SqfSourceWriter._VAR_CHAR_BMP_SCAN.rename(
			identCharBmpScan).staticize(),
			CBasicExpression.array(__struct.charBmpScan()));
		cFile.newLine(true);
		
		String identCharBmp = baseName + "_charBmp";
		cFile.define(SqfSourceWriter._VAR_CHAR_BMP.rename(
			identCharBmp).staticize(),
			CBasicExpression.array(__struct.charBmp()));
		cFile.newLine(true);
		
		// Open structure for defining
		try (CStructVariableBlock sqf = cFile.define(
				CStructVariableBlock.class,
				CVariable.of(SqfSourceWriter._TYPE_SQF.constType(), baseName)))
		{
			// Cleaner at the start
			cFile.newLine(true);
			
			sqf.memberSet("name",
				CBasicExpression.string(__struct.name));
			sqf.memberSet("family",
				CBasicExpression.number(__struct.family.ordinal()));
			sqf.memberSet("pixelHeight",
				CBasicExpression.number(__struct.pixelHeight));
			sqf.memberSet("ascent",
				CBasicExpression.number(__struct.ascent));
			sqf.memberSet("descent",
				CBasicExpression.number(__struct.descent));
			sqf.memberSet("bbx",
				CBasicExpression.number(__struct.bbx));
			sqf.memberSet("bby",
				CBasicExpression.number(__struct.bby));
			sqf.memberSet("bbw",
				CBasicExpression.number(__struct.bbw));
			sqf.memberSet("bbh",
				CBasicExpression.number(__struct.bbh));
			sqf.memberSet("codepointStart",
				CBasicExpression.number(__struct.codepointStart));
			sqf.memberSet("codepointCount",
				CBasicExpression.number(__struct.codepointCount));
			sqf.memberSet("huffBitsSize",
				CBasicExpression.number(__struct.huffBitsSize));
			sqf.memberSet("charBmpSize",
				CBasicExpression.number(__struct.charBmpSize));
			sqf.memberSet("huffBits",
				CBasicExpression.of(identHuffBits));
			sqf.memberSet("charWidths",
				CBasicExpression.of(identCharWidths));
			sqf.memberSet("charXOffset",
				CBasicExpression.of(identCharXOffset));
			sqf.memberSet("charYOffset",
				CBasicExpression.of(identCharYOffset));
			sqf.memberSet("charFlags",
				CBasicExpression.of(identCharFlags));
			sqf.memberSet("charBmpOffset",
				CBasicExpression.of(identCharBmpOffset));
			sqf.memberSet("charBmpScan",
				CBasicExpression.of(identCharBmpScan));
			sqf.memberSet("charBmp",
				CBasicExpression.of(identCharBmp));
		}
		
		// Padding
		cFile.newLine(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/09
	 */
	@Override
	public void write(SqfFontStruct... __structs)
		throws IOException, NullPointerException
	{
		if (__structs == null)
			throw new NullPointerException("NARG");
		
		// Write individual structs
		int n = __structs.length;
		CIdentifier[] structsId = new CIdentifier[n];
		for (int i = 0; i < n; i++)
		{
			this.write(__structs[i]);
			structsId[i] = CIdentifier.of(
				SqfSourceWriter.__baseName(__structs[i]));
		}
		
		CFile cFile = this.cFile;
		
		// Base name of constants
		String baseName = String.format("sqf_font_%s_%d", __structs[0].name,
			__structs[0].pixelHeight);
		
		// Sequence of fonts
		CIdentifier sqfs = CIdentifier.of(baseName + "_sqfs");
		CVariable sqfsVar = CVariable.of(
			SqfSourceWriter._TYPE_SQF.constType().pointerType().arrayType(n),
			sqfs);
		cFile.define(sqfsVar, CBasicExpression.arrayReferences(structsId));
		
		// Write codepages
		try (CStructVariableBlock sqf = cFile.define(
				CStructVariableBlock.class,
				CVariable.of(SqfSourceWriter._TYPE_SQF_CODEPAGE.constType(),
					baseName)))
		{
			// Cleaner at the start
			cFile.newLine(true);
			
			sqf.memberSet("name",
				CBasicExpression.string(__structs[0].name));
			sqf.memberSet("numCodepages",
				CBasicExpression.number(n));
			sqf.memberSet("codepages",
				sqfs);
		}
		
		// Padding
		cFile.newLine(true);
	}
	
	/**
	 * Calculates the base name of the structure.
	 *
	 * @param __struct The structure.
	 * @return The base name of the structure.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	private static String __baseName(SqfFontStruct __struct)
		throws NullPointerException
	{
		if (__struct == null)
			throw new NullPointerException("NARG");
		
		return String.format("sqf_font_%s_%d_%X", __struct.name,
			__struct.pixelHeight, __struct.codepointStart / 256);
	}
}
