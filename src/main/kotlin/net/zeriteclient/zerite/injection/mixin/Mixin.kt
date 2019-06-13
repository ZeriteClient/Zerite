package net.zeriteclient.zerite.injection.mixin

import me.falsehonesty.asmhelper.dsl.At
import me.falsehonesty.asmhelper.dsl.InjectionPoint
import me.falsehonesty.asmhelper.dsl.inject
import me.falsehonesty.asmhelper.dsl.instructions.Descriptor
import me.falsehonesty.asmhelper.dsl.instructions.invokeKOBjectFunction

object Mixin {

    private fun buildMethodDescriptor(str: String): Descriptor {
        val classSplit = str.split(".")
        val paramSplit = classSplit[1].split("(")
        return Descriptor(classSplit[0], paramSplit[0], "(${paramSplit[1]}")
    }

    private fun buildInjectionPoint(str: String): InjectionPoint {
        if (str == "HEAD") {
            return InjectionPoint.HEAD
        }

        val classSplit = str.split(".")
        val paramSplit = classSplit[1].split("(")
        return InjectionPoint.INVOKE(Descriptor(classSplit[0], paramSplit[0], "(${paramSplit[1]}"))
    }

    fun injectInvocation(methodDescriptor: String, injectionPoint: String, handleFunction: String) {
        Mixin.injectInvocation(
            buildMethodDescriptor(methodDescriptor),
            buildInjectionPoint(injectionPoint),
            buildMethodDescriptor(handleFunction)
        );
    }

    private fun injectInvocation(
        methodDescriptor: Descriptor,
        injectionPoint: InjectionPoint,
        handleDescriptor: Descriptor
    ) = inject {
        className = methodDescriptor.owner
        methodName = methodDescriptor.name
        methodDesc = methodDescriptor.desc
        at = At(injectionPoint)

        insnList {
            invokeKOBjectFunction(
                handleDescriptor.owner,
                handleDescriptor.name,
                handleDescriptor.desc
            )
        }
    }

}