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
