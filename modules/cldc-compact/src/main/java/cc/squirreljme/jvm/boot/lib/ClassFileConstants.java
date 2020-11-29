// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

/**
 * This interface defines all of the class file constants which is used to
 * remove the usages of magic numbers.
 *
 * @since 2019/10/12
 */
@Deprecated
public interface ClassFileConstants
{
	/** The magic number for the header. */
	@Deprecated
	int MAGIC_NUMBER =
		0x00586572;
	
	/** Magic number for the end of file. */
	@Deprecated
	int END_MAGIC_NUMBER =
		0x42796521;
	
	/** The size of the header without the magic number. */
	@Deprecated
	byte HEADER_SIZE_WITHOUT_MAGIC =
		108;
	
	/** The size of the header with the magic number. */
	@Deprecated
	byte HEADER_SIZE_WITH_MAGIC =
		112;
		
	/** Unused A. */
	@Deprecated
	byte OFFSET_OF_USHORT_UNUSEDA =
		4;
	
	/** The index of the method which is __start. */
	@Deprecated
	byte OFFSET_OF_UBYTE_STARTMETHODINDEX =
		6;
	
	/** The data type of the class. */
	@Deprecated
	byte OFFSET_OF_UBYTE_DATATYPE =
		7;
	
	/** Not used. */
	@Deprecated
	byte OFFSET_OF_USHORT_UNUSEDB =
		8;
	
	/** Class flags. */
	@Deprecated
	byte OFFSET_OF_INT_CLASSFLAGS =
		10;
	
	/** Name of class. */
	@Deprecated
	byte OFFSET_OF_USHORT_CLASSNAME =
		14;
	
	/** Super class name. */
	@Deprecated
	byte OFFSET_OF_USHORT_CLASSSUPER =
		16;
	
	/** Interfaces in class. */
	@Deprecated
	byte OFFSET_OF_USHORT_CLASSINTS =
		18;
	
	/** Class type. */
	@Deprecated
	byte OFFSET_OF_UBYTE_CLASSTYPE =
		20;
	
	/** Class version. */
	@Deprecated
	byte OFFSET_OF_UBYTE_CLASSVERS =
		21;
	
	/** Class source filename. */
	@Deprecated
	byte OFFSET_OF_USHORT_CLASSSFN =
		22;
	
	/** Static field count. */
	@Deprecated
	byte OFFSET_OF_USHORT_SFCOUNT =
		24;
	
	/** Static field bytes. */
	@Deprecated
	byte OFFSET_OF_USHORT_SFBYTES =
		26;
	
	/** Static field objects. */
	@Deprecated
	byte OFFSET_OF_USHORT_SFOBJS =
		28;
	
	/** Instance field count. */
	@Deprecated
	byte OFFSET_OF_USHORT_IFCOUNT =
		30;
	
	/** Instance field bytes. */
	@Deprecated
	byte OFFSET_OF_USHORT_IFBYTES =
		32;
	
	/** Instance field objects. */
	@Deprecated
	byte OFFSET_OF_USHORT_IFOBJS =
		34;
	
	/** Static method count. */
	@Deprecated
	byte OFFSET_OF_USHORT_SMCOUNT =
		36;
	
	/** Instance method count. */
	@Deprecated
	byte OFFSET_OF_USHORT_IMCOUNT =
		38;
	
	/** Not used. */
	@Deprecated
	byte OFFSET_OF_INT_UNUSEDC =
		40;
	
	/** Not used. */
	@Deprecated
	byte OFFSET_OF_INT_UNUSEDD =
		44;
	
	/** Static field data offset. */
	@Deprecated
	byte OFFSET_OF_INT_SFOFF =
		48;
	
	/** Static field data size. */
	@Deprecated
	byte OFFSET_OF_INT_SFSIZE =
		52;
	
	/** Interface field data offset. */
	@Deprecated
	byte OFFSET_OF_INT_IFOFF =
		56;
	
	/** Interface field data size. */
	@Deprecated
	byte OFFSET_OF_INT_IFSIZE =
		60;
	
	/** Static method data offset. */
	@Deprecated
	byte OFFSET_OF_INT_SMOFF =
		64;
	
	/** Static method data size. */
	@Deprecated
	byte OFFSET_OF_INT_SMSIZE =
		68;
	
	/** Instance method data offset. */
	@Deprecated
	byte OFFSET_OF_INT_IMOFF =
		72;
	
	/** Instance method data size. */
	@Deprecated
	byte OFFSET_OF_INT_IMSIZE =
		76;
	
	/** High bits for UUID. */
	@Deprecated
	byte OFFSET_OF_INT_UUIDHI =
		80;
	
	/** Low bits for UUID. */
	@Deprecated
	byte OFFSET_OF_INT_UUIDLO =
		84;
	
	/** File size. */
	@Deprecated
	byte OFFSET_OF_INT_FILESIZE =
		88;
	
	/** Not used. */
	@Deprecated
	byte OFFSET_OF_INT_UNUSEDE =
		92;
	
	/** Static constant pool offset. */
	@Deprecated
	byte OFFSET_OF_INT_STATICPOOLOFF =
		96;
	
	/** Static constant pool size. */
	@Deprecated
	byte OFFSET_OF_INT_STATICPOOLSIZE =
		100;
	
	/** Runtime constant pool offset. */
	@Deprecated
	byte OFFSET_OF_INT_RUNTIMEPOOLOFF =
		104;
	
	/** Runtime constant pool size. */
	@Deprecated
	byte OFFSET_OF_INT_RUNTIMEPOOLSIZE =
		108;
	
	/** Public class. */
	@Deprecated
	short FLAG_PUBLIC =
		0x0001;
	
	/** Final class. */
	@Deprecated
	short FLAG_FINAL =
		0x0010;
	
	/** Super class. */
	@Deprecated
	short FLAG_SUPER =
		0x0020;
	
	/** Interface class. */
	@Deprecated
	short FLAG_INTERFACE =
		0x0200;
	
	/** Abstract. */
	@Deprecated
	short FLAG_ABSTRACT =
		0x0400;
	
	/** Synthetic. */
	@Deprecated
	short FLAG_SYNTHETIC =
		0x1000;
	
	/** Annotation. */
	@Deprecated
	short FLAG_ANNOTATION =
		0x2000;
	
	/** Enum. */
	@Deprecated
	short FLAG_ENUM =
		0x4000;
}

