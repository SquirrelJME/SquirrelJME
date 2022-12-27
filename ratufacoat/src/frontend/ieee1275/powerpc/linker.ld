ENTRY(_start)
OUTPUT_FORMAT(elf32-powerpc)

SECTIONS
{
	. = 0x0;

	.text : AT(ADDR(.text))
	{
		*(.text*)
		*(.rodata*)
	}

	.data ALIGN(0x1000) : AT(ADDR(.data))
	{
		*(.data)
	}

	.bss : AT(ADDR(.bss))
	{
		_sbss = .;
		*(COMMON)
		*(.bss)
		_ebss = .;
	}
}
