# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Script that turns function table CSV to source code

import csv
import sys


class FuncArg:
	"""
	Represents a function argument or return type.
	"""
	def __init__(self, in_str: str, in_mods: str = None, in_type: str = None, in_name: str = None, in_star: str = None):
		if in_str is None or in_str == '':
			self.mods = in_mods
			self.type = in_type
			self.name = in_name
			self.star = in_star

		else:
			self.in_str = in_str.strip()

			if in_str.strip() == '...':
				self.mods = ''
				self.type = '...'
				self.name = '...'
				self.star = ''
			else:
				# Extract name first, if any
				last_space = in_str.strip().rfind(" ")
				if last_space >= 0:
					self.name = in_str[last_space + 1:].strip()
				else:
					self.name = ''
					last_space = len(in_str)

				# Determine if there are pointer stars
				type_side = in_str[0:last_space].strip()
				first_pointer_star = type_side.find("*")

				# Found pointer stars
				if first_pointer_star >= 0:
					self.type = type_side[0:first_pointer_star].strip()
					self.star = type_side[first_pointer_star:].strip()

				# There are none
				else:
					self.type = type_side
					self.star = ''

				# Remove const modifier
				if self.type.startswith('const '):
					self.type = self.type[6:].strip()
					self.mods = 'const'
				else:
					self.mods = ''

	def __repr__(self):
		typeish = self.type
		nameish = self.name
		starish = self.star

		if self.mods is not None and self.mods != '':
			modsish = '%s ' % self.mods
		else:
			modsish = ''

		if nameish is not None and nameish != '':
			return '%s%s%s %s' % (modsish, typeish, starish, nameish)
		return '%s%s%s' % (modsish, typeish, starish)


class FuncTableEntry:
	"""
	Function table entry, represents a single function to generate.
	"""
	def __init__(self, row_entry):
		self.table = row_entry['table']
		self.index = int(row_entry['index'])
		self.function = row_entry['function']

		if row_entry['returnType'] is not None and row_entry['returnType'] != '':
			self.returnType: FuncArg = FuncArg(row_entry['returnType'])
		else:
			self.returnType: FuncArg = None

		# Read in arguments
		self.args: [FuncArg] = []
		try:
			for i in range(0, 99):
				item = row_entry['arg' + str(i)]
				if item is not None and len(item) > 0:
					self.args.append(FuncArg(row_entry['arg' + str(i)]))
		except KeyError:
			pass

	def __repr__(self):
		return str([self.table, str(self.index),
					self.function if self.function is not None else "null",
					self.returnType if self.returnType is not None else "null",
					self.args])


def convert_type(func_type: FuncArg) -> FuncArg:
	"""
	Converts the function argument and represents it with a SquirrelJME type
	along with how it would be represented otherwise.

	:param func_type: Function argument.
	:return: The string which represents the function argument mapped to
	SquirrelJME types accordingly.
	"""
	typeish = func_type.type
	nameish = func_type.name
	starish = func_type.star

	if func_type.mods is not None and func_type.mods != '':
		modsish = '%s' % func_type.mods
	else:
		modsish = ''

	# Remap type to SquirrelJME?
	match typeish:
		case 'jfieldID':
			typeish = 'sjme_vmField'
		case 'jmethodID':
			typeish = 'sjme_vmMethod'
		case 'JavaVM':
			typeish = 'sjme_vmState'
		case 'JNIEnv':
			typeish = 'sjme_vmThread'
		case 'jobjectRefType':
			typeish = 'sjme_jobjectReferenceType'
		case 'JNINativeMethod':
			typeish = 'sjme_vmRegisterNative'

		case 'jvalue':
			typeish = 'sjme_jvalue'
		case 'jboolean':
			typeish = 'sjme_jboolean'
		case 'jbyte':
			typeish = 'sjme_jbyte'
		case 'jchar':
			typeish = 'sjme_jchar'
		case 'const jchar':
			typeish = 'const sjme_jchar'
		case 'jshort':
			typeish = 'sjme_jshort'
		case 'jint':
			typeish = 'sjme_jint'
		case 'jlong':
			typeish = 'sjme_jlong'
		case 'jfloat':
			typeish = 'sjme_jfloat'
		case 'jdouble':
			typeish = 'sjme_jdouble'
		case 'jobject':
			typeish = 'sjme_jobject'
		case 'jclass':
			typeish = 'sjme_jclass'
		case 'jstring':
			typeish = 'sjme_jstring'
		case 'jthrowable':
			typeish = 'sjme_jthrowable'
		case 'jweak':
			typeish = 'sjme_jweakReference'
		case 'jsize':
			typeish = 'sjme_jsize'

		case 'jarray':
			typeish = 'sjme_jarray'
		case 'jbooleanArray':
			typeish = 'sjme_jbooleanArray'
		case 'jbyteArray':
			typeish = 'sjme_jbyteArray'
		case 'jcharArray':
			typeish = 'sjme_jcharArray'
		case 'jshortArray':
			typeish = 'sjme_jshortArray'
		case 'jintArray':
			typeish = 'sjme_jintArray'
		case 'jlongArray':
			typeish = 'sjme_jlongArray'
		case 'jfloatArray':
			typeish = 'sjme_jfloatArray'
		case 'jdoubleArray':
			typeish = 'sjme_jdoubleArray'
		case 'jobjectArray':
			typeish = 'sjme_jobjectArray'

	# Remap name to SquirrelJME?
	match nameish:
		case 'vm':
			nameish = 'vmState'
		case 'p_env':
			nameish = 'vmThread'
		case 'penv':
			nameish = 'vmThread'
		case 'env':
			nameish = 'vmThread'
		case 'thr_args':
			nameish = 'threadArgs'
		case 'cls':
			nameish = 'classy'
		case 'clazz':
			nameish = 'classy'
		case 'clazz1':
			nameish = 'classyA'
		case 'clazz2':
			nameish = 'classyB'
		case 'ref':
			nameish = 'reference'
		case 'ref1':
			nameish = 'referenceA'
		case 'ref2':
			nameish = 'referenceB'
		case 'msg':
			nameish = 'message'
		case 'obj':
			nameish = 'object'
		case 'str':
			nameish = 'string'
		case 'sig':
			nameish = 'signature'
		case 'methodID':
			nameish = 'method'
		case 'fieldID':
			nameish = 'field'
		case 'length':
			nameish = 'len'
		case 'index':
			nameish = 'dx'
		case 'elems':
			nameish = 'elements'
		case 'carray':
			nameish = 'charArray'

	return FuncArg(None, modsish, typeish, nameish, starish)


# Header start
header_intro = '/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME\n\
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME is under the GNU General Public License v3+, or later.\n\
// See license.mkd for licensing and copyright information.\n\
// -------------------------------------------------------------------------*/\n\
\n'

# If you really mean to!
if len(sys.argv) < 2 or sys.argv[1] != 'yes':
	print('If you really mean to run this, pass "yes" as the first argument!\n')
	exit()
	print('Just for sanity purposes...')

# Setup file load
csvfile = open('funcTable.csv', newline='')
reader = csv.DictReader(csvfile)

# Read in each row and fill a mapping accordingly
entries = []
entries_by_tab_dex = {}  # dict[str, dict[int, entries]]
for row in reader:
	# Load in entry
	current = FuncTableEntry(row)
	entries.append(current)

	# Setup major table for the group
	if entries_by_tab_dex.get(current.table) is None:
		entries_by_tab_dex[current.table] = {}
	major_table = entries_by_tab_dex.get(current.table)

	# Add entries to sub-table
	major_table[current.index] = current

# Go through each table and generate source code for it
for table_name in entries_by_tab_dex:
	index_tables = entries_by_tab_dex[table_name]

	# Header name for prototypes
	header_struct_fields_file_name = 'sjmejni/tables/%sStructFields.h' % table_name
	header_function_prototypes_file_name = 'sjmejni/tables/%sProtos.h' % table_name
	header_function_prototypes_proxy_file_name = 'sjmejni/tables/%sProtosProxy.h' % table_name
	assign_file_name = 'sjmejni/tables/%sAssign.h' % table_name
	assign_proxy_file_name = 'sjmejni/tables/%sAssignProxy.h' % table_name

	# Temporary strings for header/source files
	header_function_prototypes = '' + header_intro
	header_function_proxy_prototypes = '' + header_intro
	header_struct_fields = '' + header_intro

	# Assignment file
	assign_file = '' + header_intro
	assign_proxy_file = '' + header_intro

	# Surround for function prototypes
	header_function_prototypes += '#include "sjmejni/sjmejni.h"\n'
	header_function_prototypes += '#include "debug.h"\n'
	header_function_prototypes += '#include "sjmejni/tables/surround/surroundprotoh.h"\n\n'

	# Surround for function prototypes proxy
	header_function_proxy_prototypes += '#include "sjmejni/sjmejni.h"\n'
	header_function_proxy_prototypes += '#include "debug.h"\n'
	header_function_proxy_prototypes += '#include "sjmejni/tables/surround/surroundprotoproxyh.h"\n\n'

	# Surround for header struct entries
	header_struct_fields += '#include "sjmejni/tables/surround/surroundstructfield.h"\n\n'

	# Handle each index accordingly
	try:
		for index in range(0, 999):
			# Load in index
			entry: FuncTableEntry = index_tables[index]

			# Nulls are blank specials
			if entry.function == 'NULL':
				header_struct_fields += '/** Do not use, reserved. */\n'
				header_struct_fields += 'void* reserved%d SJME_FUNC_SURROUND_SUFFIX\n\n' % entry.index

			# Otherwise do normal generation logic
			else:
				# Location of the C include for the prototype
				prototype_file_name = 'sjmejni/tables/%s/def%s.h' % \
					(entry.table, entry.function)

				source_file_name = 'tables/%s/def%s.c' % \
					(entry.table, entry.function)

				# Assignment file
				assign_file += 'out->%s = sjme_impl%s;\n' % (entry.function, entry.function)
				assign_proxy_file += 'out->%s = sjme_proxy%s;\n' % (entry.function, entry.function)

				# Utilize the C include here
				header_struct_fields += '#include "%s"\n' % prototype_file_name

				# Include within prototypes
				header_function_prototypes += '#include "%s"\n' % \
					prototype_file_name
				header_function_proxy_prototypes += '#include "%s"\n' % \
											  prototype_file_name

				# Build function arguments
				built_args = ''
				for func_arg in entry.args:
					# Spacer for arguments
					if len(built_args) > 0:
						built_args += ', '

					# Variadic args are a bit special, they just go right on
					if func_arg.type == '...':
						built_args += '...'

					# Otherwise add argument
					else:
						built_args += str(convert_type(func_arg))

				# if empty, make it void since it accepts no arguments
				if len(built_args) == 0:
					built_args = 'void'

				# Build prototype file
				prototype_file = '' + header_intro

				# Doxygen comment description
				prototype_file += '/**\n'
				prototype_file += ' * NOT DESCRIBED.\n'
				prototype_file += ' * \n'

				for func_arg in entry.args:
					prototype_file += ' * @param %s NOT DESCRIBED.\n' % convert_type(func_arg).name

				if entry.returnType is not None and entry.returnType.type != 'void':
					prototype_file += ' * @return NOT DESCRIBED.\n'

				prototype_file += ' * @since \n'
				prototype_file += ' */\n'

				# Function type
				prototype_file += '%s SJME_FUNC_SURROUND(%s)(%s) ' \
					'SJME_FUNC_SURROUND_SUFFIX\n\n' % \
					(str(convert_type(entry.returnType)),
					entry.function, built_args)

				# Source file for the method
				source_file = '' + header_intro
				source_file += '#include "sjmejni/sjmejni.h"\n'
				source_file += '#include "%s"\n\n' % header_function_prototypes_file_name

				source_file += '%s sjme_impl%s(%s)\n' % \
					(str(convert_type(entry.returnType)),
					entry.function, built_args)
				source_file += '{\n'
				source_file += '\tsjme_todo("Implement this?");\n'

				# Return type used?
				if entry.returnType.type != 'void' or entry.returnType.star != '':
					match entry.returnType.type:
						case 'jboolean':
							returning = 'sjme_false'
						case 'jbyte':
							returning = '0'
						case 'jchar':
							returning = '0'
						case 'jshort':
							returning = '0'
						case 'jint':
							returning = '0'
						case 'jlong':
							returning = '0'
						case 'jfloat':
							returning = '0'
						case 'jdouble':
							returning = '0'
						case 'jobjectRefType':
							returning = '0'
						case 'jsize':
							returning = '0'
						case _:
							returning = 'NULL'

					source_file += '\treturn %s;\n' % returning

				source_file += '}\n'

				source_file_out = open('../../include/' + prototype_file_name, 'w')
				source_file_out.write(prototype_file)
				source_file_out.close()

				source_file_out = open(source_file_name, 'w')
				source_file_out.write(source_file)
				source_file_out.close()

	except KeyError:
		pass

	source_file_out = open('../../include/' + header_struct_fields_file_name, 'w')
	source_file_out.write(header_struct_fields)
	source_file_out.close()

	source_file_out = open('../../include/' + header_function_prototypes_file_name, 'w')
	source_file_out.write(header_function_prototypes)
	source_file_out.close()

	source_file_out = open('../../include/' + header_function_prototypes_proxy_file_name, 'w')
	source_file_out.write(header_function_proxy_prototypes)
	source_file_out.close()

	source_file_out = open('../../include/' + assign_file_name, 'w')
	source_file_out.write(assign_file)
	source_file_out.close()

	source_file_out = open('../../include/' + assign_proxy_file_name, 'w')
	source_file_out.write(assign_proxy_file)
	source_file_out.close()
