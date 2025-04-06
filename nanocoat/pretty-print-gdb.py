# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: GDB Pretty Printers for SquirrelJME

import gdb
import gdb.printing

def base_type(stripped):
    # Remove all qualifiers and typedefs
    stripped = stripped.strip_typedefs().unqualified()

    # Dereference pointers and any nested qualifiers
    while stripped.code == gdb.TYPE_CODE_PTR:
        stripped = stripped.target()
        stripped = stripped.strip_typedefs().unqualified()

    # Remove all qualifiers and typedefs
    stripped = stripped.strip_typedefs().unqualified()

    # Use the tag if available
    if stripped.tag is not None:
        stripped = stripped.tag

    # If this starts with struct, remove it
    if str(stripped).startswith("struct "):
        stripped = str(stripped)[7:]

    return str(stripped)

def is_null(value):
    try:
        if (value.type.code == gdb.TYPE_CODE_PTR and
            value.address is not None and value.address <= 0x1000):
            return 1

        if (value.reference_value().address is not None and
            value.reference_value().address <= 0x1000):
            return 1

        return 0
    except:
        return 0

def generic_children(result, value):
    if is_null(value):
        return []

    for field in value.type.fields():
        result.append([field.name, value[field.name]])
    return result

class sjme_char_seq:
    def __init__(self, value):
        self.value = value

    def display_hint(self):
        return "string"

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = [[".toString()", self.to_string()]]
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        # SJME_CHAR_SEQ_TYPE_NULL
        type_id = self.value["type"]
        if type_id == 0:
            return ""

        # SJME_CHAR_SEQ_TYPE_FUNCTION
        elif type_id == 1:
            return "<function>"

        # SJME_CHAR_SEQ_TYPE_FUNCTION_STATIC
        elif type_id == 2:
            return "<static-function>"

        # SJME_CHAR_SEQ_TYPE_NARROW
        elif type_id == 3 or type_id == 5:
            return (self.value["data"]["bytes"].reference_value()
                    .cast(gdb.lookup_type("char*")))

        # SJME_CHAR_SEQ_TYPE_WIDE
        elif type_id == 4:
            return (self.value["data"]["chars"].reference_value()
                    .cast(gdb.lookup_type("wchar_t*")))

        # SJME_CHAR_SEQ_TYPE_UTF_STATIC
        elif type_id == 6:
            return self.value["data"].value["staticUtf"]

        return "<NULL>"

class sjme_nvm_string_pool:
    def __init__(self, value):
        self.value = value

    def display_hint(self):
        return "string"

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = [[".toString()", self.to_string()]]
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        return sjme_char_seq(self.value["seq"]).to_string()

# sjme_jobject
class sjme_jobject:
    def __init__(self, value):
        self.value = value

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = [["System.identityHashCode()", self.value["identityHash"]],
            [".getClass()", self.to_string()]]
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        is_class = self.value["isClass"]
        if is_class is None:
            return "<NULL>"
        return sjme_char_seq(is_class["binaryName"]).to_string()

# sjme_jclass
class sjme_jclass:
    def __init__(self, value):
        self.value = value

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = [[".name()", self.to_string()]]
        result += sjme_jobject(self.value["object"].reference_value()).children()
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        try:
            return sjme_char_seq(self.value["binaryName"]).to_string()
        except Exception:
            return "<unknown>"

# sjme_jstring
class sjme_jstring:
    def __init__(self, value):
        self.value = value

    def display_hint(self):
        return "string"

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = [[".toString()", self.to_string()],
            sjme_jobject(self.value["object"]).children()]
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        try:
            return sjme_char_seq(self.value["seq"]).to_string()
        except:
            return "<NULL>"

# Anything based on sjme_jobject
class sjme_jobject_like:
    def __init__(self, value):
        self.value = value

    def like_cast(self, what):
        if self.value.type.code == gdb.TYPE_CODE_PTR:
            return self.value.cast(gdb.lookup_type(f"{what}*"))
        return self.value.cast(gdb.lookup_type(f"{what}"))

    def like(self):
        struct_id = (self.value.cast(gdb.lookup_type("sjme_nvm_commonBase"))["type"])
        # SJME_NVM_STRUCT_ARRAY_INSTANCE
        #if struct_id == 1:
        #    return sjme_jarray(self.value.cast(gdb.lookup_type("sjme_jarray")))

        # SJME_NVM_STRUCT_CLASS_INSTANCE
        if struct_id == 4:
            return sjme_jclass(self.like_cast("sjme_jclassBase"))

        # SJME_NVM_STRUCT_STRING_INSTANCE
        if struct_id == 18:
            return sjme_jstring(self.like_cast("sjme_jstringBase"))

        # SJME_NVM_STRUCT_OBJECT_INSTANCE or otherwise
        return sjme_jobject(self.like_cast("sjme_jobjectBase"))

    def display_hint(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "string"

        return self.like().display_hint()

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        return self.like().children()

    def to_string(self):
        if is_null(self.value):
            return "<NULL>"
        return self.like().to_string()

# sjme_list_...
class sjme_jlist:
    def __init__(self, value):
        self.value = value

    def display_hint(self):
        return "array"

    def children(self):
        # Do nothing on nulls
        if is_null(self.value):
            return []

        result = []

        # Determine the subtype, to figure out how to render things
        # sjme_list_
        list_type_p = (gdb.lookup_type(base_type(self.value.type)[10:])
                       .pointer())

        # Add every single element
        length = self.value["length"]
        if not is_null(length):
            for i in range(0, length):
                element_value = ((self.value["elements"].cast(list_type_p)) + i)

                if is_null(element_value):
                    result.append([f"[{i}]", 0])
                else:
                    result.append([f"[{i}]",
                        (element_value.dereference())])

        # Other structure stuff
        generic_children(result, self.value)
        return result

    def to_string(self):
        # Do nothing on nulls
        if is_null(self.value):
            return "<NULL>"

        try:
            return f"{str(base_type(self.value.type))}[{self.value['length']}]"
        except:
            return "<NULL>"

# Install pretty printers
def sjme_printer_types(value):
    stripped = base_type(value.type)

    if str(stripped).find("sjme_charSeqStatic") == 0:
        return sjme_char_seq(value)
    elif str(stripped).find("sjme_nvm_stringPool_stringBase") == 0:
        return sjme_nvm_string_pool(value)
    elif ((str(stripped).find("sjme_jobject") == 0 or
        str(stripped).find("sjme_jclass") == 0 or
        str(stripped).find("sjme_jstring") == 0)):
        return sjme_jobject_like(value)
    elif str(stripped).find("sjme_list_") == 0:
        return sjme_jlist(value)
    return None

gdb.pretty_printers.append(sjme_printer_types)
