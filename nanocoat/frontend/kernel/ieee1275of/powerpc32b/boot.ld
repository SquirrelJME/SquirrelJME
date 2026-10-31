/* ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ------------------------------------------------------------------------ */

ENTRY(_start)
OUTPUT_FORMAT(elf32-powerpc)

SECTIONS
{
	/*. = 0x200000; */
	. = 0x0;

	/* Code. */
	.text : AT(ADDR(.text))
	{
		_start = .;
		*(.text*)
		*(.rodata*)
	}

	/* Data segment. */
	.data ALIGN(0x1000) : AT(ADDR(.data))
	{
		*(.data)
	}

	/* Allocated segment. */
	.bss : AT(ADDR(.bss))
	{
		_sbss = .;
		*(COMMON)
		*(.bss)
		_ebss = .;
	}
}
