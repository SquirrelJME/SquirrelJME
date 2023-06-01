/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

ENTRY(_start)
OUTPUT_FORMAT(elf32-powerpc)

SECTIONS
{
	. = 0x20000;

	.text : AT(ADDR(.text))
	{
		*(.text.start)
		*(.text*)
		*(.rodata*)
	}

	.data ALIGN(0x1000) : AT(ADDR(.data))
	{
		*(.data)
	}

	.bss ALIGN(0x1000) : AT(ADDR(.bss))
	{
		_sbss = .;
		*(COMMON)
		*(.bss)
		_ebss = .;
	}
}
