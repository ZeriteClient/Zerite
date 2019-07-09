package net.zeriteclient.zerite.injection.stages.asm.access

import com.google.common.base.Splitter
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import net.minecraft.launchwrapper.IClassTransformer
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import java.io.IOException

open class AccessTransformer(rulesFile: String) : IClassTransformer {

    open inner class Modifier {
        var name = ""
        var desc = ""
        var oldAccess = 0
        var newAccess = 0
        var targetAccess = 0
        var changeFinal = false
        var markFinal = false
        var modifyClassVisibility = false

        fun setTargetAccess(name: String) {
            when {
                name.startsWith("public") -> targetAccess = ACC_PUBLIC
                name.startsWith("private") -> targetAccess = ACC_PRIVATE
                name.startsWith("protected") -> targetAccess = ACC_PROTECTED
            }

            if (name.endsWith("-f")) {
                changeFinal = true
                markFinal = false
            } else if (name.endsWith("+f")) {
                changeFinal = true
                markFinal = true
            }
        }
    }

    val modifiers: ArrayListMultimap<String, Modifier> = ArrayListMultimap.create<String, Modifier>()
    val logger: Logger = LogManager.getLogger("Zerite-AT")
    private val debug = System.getProperty("zerite.atDebug") != null

    init {
        readMapFile(rulesFile)
    }

    @Throws(IOException::class)
    fun readMapFile(rulesFile: String) {
        processATFile(IOUtils.toString(AccessTransformer::class.java.getResourceAsStream(rulesFile)))
        logger.info(String.format("Loaded %d rules from AccessTransformer config file %s", modifiers.size(), rulesFile))
    }

    @Throws(IOException::class)
    fun processATFile(content: String) {
        content.split('\n').forEach { input ->
            val line = Iterables.getFirst(Splitter.on('#').limit(2).split(input), "")!!.trim { it <= ' ' }
            if (line.isEmpty()) {
                return@forEach
            }
            val parts = Lists.newArrayList(Splitter.on(" ").trimResults().split(line))
            if (parts.size > 3) {
                throw RuntimeException("Invalid config file line $input")
            }
            val m = Modifier()
            m.setTargetAccess(parts[0])

            if (parts.size == 2) {
                m.modifyClassVisibility = true
            } else {
                val nameReference = parts[2]
                val parenIdx = nameReference.indexOf('(')
                if (parenIdx > 0) {
                    m.desc = nameReference.substring(parenIdx)
                    m.name = nameReference.substring(0, parenIdx)
                } else {
                    m.name = nameReference
                }
            }
            val className = parts[1].replace('/', '.')
            modifiers.put(className, m)
            if (debug)
                println(
                    String.format(
                        "AT RULE: %s %s %s (type %s)",
                        toBinary(m.targetAccess),
                        m.name,
                        m.desc,
                        className
                    )
                )
        }
    }

    override fun transform(name: String, transformedName: String, bytes: ByteArray?): ByteArray? {
        if (bytes == null) {
            return null
        }

        if (debug) {
            logger.info(String.format("Considering all methods and fields on %s (%s)", transformedName, name))
        }

        if (!modifiers.containsKey(transformedName)) {
            return bytes
        }

        val classNode = ClassNode()
        val classReader = ClassReader(bytes)
        classReader.accept(classNode, 0)

        val mods = modifiers.get(transformedName)
        for (m in mods) {
            if (m.modifyClassVisibility) {
                classNode.access = getFixedAccess(classNode.access, m)
                if (debug) {
                    println(String.format("Class: %s %s -> %s", name, toBinary(m.oldAccess), toBinary(m.newAccess)))
                }
                continue
            }
            if (m.desc.isEmpty()) {
                for (n in classNode.fields) {
                    if (n.name == m.name || m.name == "*") {
                        n.access = getFixedAccess(n.access, m)
                        if (debug) {
                            println(
                                String.format(
                                    "Field: %s.%s %s -> %s",
                                    name,
                                    n.name,
                                    toBinary(m.oldAccess),
                                    toBinary(m.newAccess)
                                )
                            )
                        }

                        if (m.name != "*") {
                            break
                        }
                    }
                }
            } else {
                val nowOverridable = Lists.newArrayList<MethodNode>()
                for (n in classNode.methods) {
                    if (n.name == m.name && n.desc == m.desc || m.name == "*") {
                        n.access = getFixedAccess(n.access, m)

                        // constructors always use INVOKESPECIAL
                        if (n.name != "<init>") {
                            // if we changed from private to something else we need to replace all INVOKESPECIAL calls to this method with INVOKEVIRTUAL
                            // so that overridden methods will be called. Only need to scan this class, because obviously the method was private.
                            val wasPrivate = m.oldAccess and ACC_PRIVATE == ACC_PRIVATE
                            val isNowPrivate = m.newAccess and ACC_PRIVATE == ACC_PRIVATE

                            if (wasPrivate && !isNowPrivate) {
                                nowOverridable.add(n)
                            }

                        }

                        if (debug) {
                            println(
                                String.format(
                                    "Method: %s.%s%s %s -> %s",
                                    name,
                                    n.name,
                                    n.desc,
                                    toBinary(m.oldAccess),
                                    toBinary(m.newAccess)
                                )
                            )
                        }

                        if (m.name != "*") {
                            break
                        }
                    }
                }

                replaceInvokeSpecial(classNode, nowOverridable)
            }
        }

        val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
        classNode.accept(writer)
        return writer.toByteArray()
    }

    private fun replaceInvokeSpecial(clazz: ClassNode, toReplace: List<MethodNode>) {
        for (method in clazz.methods) {
            val it = method.instructions.iterator()
            while (it.hasNext()) {
                val insn = it.next()
                if (insn.opcode == INVOKESPECIAL) {
                    val mInsn = insn as MethodInsnNode
                    for (n in toReplace) {
                        if (n.name == mInsn.name && n.desc == mInsn.desc) {
                            mInsn.opcode = INVOKEVIRTUAL
                            break
                        }
                    }
                }
            }
        }
    }

    private fun toBinary(num: Int): String {
        return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0')
    }

    private fun getFixedAccess(access: Int, target: Modifier): Int {
        target.oldAccess = access
        val t = target.targetAccess
        var ret = access and 7.inv()

        ret = when (access and 7) {
            ACC_PRIVATE -> ret or t
            0 // default
            -> ret or (if (t != ACC_PRIVATE) t else 0 /* default */)
            ACC_PROTECTED -> ret or (if (t != ACC_PRIVATE && t != 0 /* default */) t else ACC_PROTECTED)
            ACC_PUBLIC -> ret or (if (t != ACC_PRIVATE && t != 0 /* default */ && t != ACC_PROTECTED) t else ACC_PUBLIC)
            else -> throw RuntimeException("What?")
        }

        // Clear the "final" marker on fields only if specified in control field
        if (target.changeFinal) {
            ret = if (target.markFinal) {
                ret or ACC_FINAL
            } else {
                ret and ACC_FINAL.inv()
            }
        }
        target.newAccess = ret
        return ret
    }
}