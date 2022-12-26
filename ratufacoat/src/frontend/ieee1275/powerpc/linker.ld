ENTRY(_start)
OUTPUT_FORMAT(elf32-powerpc)

SECTIONS
{
	. = 0xE0200000;

	.text : AT(ADDR(.text) - 0xE0200000)
	{
		*(.text*)
		*(.rodata*)
	}

	.data ALIGN(0x1000) : AT(ADDR(.data) - 0xE0200000)
	{
		*(.data)
	}

	.bss : AT(ADDR(.bss) - 0xE0200000)
	{
		_sbss = .;
		*(COMMON)
		*(.bss)
		_ebss = .;
	}
}
