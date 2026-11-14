package com.projectrevivesolutions.layertime.controller.ai

import com.projectrevivesolutions.layertime.controller.flow.FlowJson

/** Bounded explanation input derived from the preserved native decoder report, never invented packet facts. */
object FieldPacketBrief {
    data class Facts(val sourceSha256:String,val savedFrames:Int,val observedFrames:Int,val droppedFrames:Int,
        val deauthFrames:Int,val eapolFrames:Int,val malformedFrames:Int) {
        init {
            require(sourceSha256.matches(Regex("[a-f0-9]{64}")))
            require(savedFrames>0 && observedFrames>=savedFrames && droppedFrames>=0)
            require(savedFrames.toLong()+droppedFrames==observedFrames.toLong())
            require(deauthFrames in 0..savedFrames && eapolFrames in 0..savedFrames && malformedFrames in 0..savedFrames)
        }
    }
    fun request(f:Facts):LocalModelRequest {
        val data=mapOf("capture_summary" to mapOf("sourcePcapSha256" to f.sourceSha256,"savedFrames" to f.savedFrames,
            "observedWindowFrames" to f.observedFrames,"droppedWindowFrames" to f.droppedFrames,
            "deauthenticationFrames" to f.deauthFrames,"eapolFrames" to f.eapolFrames,"malformedFrames" to f.malformedFrames),
            "capture_context" to mapOf("source" to "Hunter SD PCAP, decoded by Vixen", "completeRun" to false,
                "verifiedCountsSentence" to "Hunter saved ${f.savedFrames} of ${f.observedFrames} observed frames and dropped ${f.droppedFrames}. " +
                    "The saved window contains ${f.deauthFrames} deauthentication frames and ${f.eapolFrames} EAPOL frames."),
            "review_policy" to "Request frames do not prove delivery or disconnection. No EAPOL in this partial saved window does not establish no reconnect, no attack, or safety. Owner and attacker identity are unresolved.")
        return LocalModelRequest(AiTask.EXPLAIN_EVIDENCE,listOf(
            PromptMessage(PromptMessage.Role.SYSTEM,"Explain the supplied decoded packet window in plain language. " +
                "Copy verifiedCountsSentence exactly into significance, preserving every digit; do not calculate or restate its counts elsewhere. " +
                "In limitations state the dropped-frame count and incomplete coverage; no EAPOL here cannot establish no reconnect or safety. " +
                "Do not claim attacker identity, successful disconnection or password loss. " +
                "Return only the supplied schema, with factReferences capture_summary, capture_context or review_policy. " +
                "Use at most 80 words total across headline, significance, limitations and passiveNextStep. Do not copy input arrays or add fields.\nOUTPUT SCHEMA\n"+FieldShadowTasks.schema),
            PromptMessage(PromptMessage.Role.USER,"SAVED DECODED EVIDENCE\n"+FlowJson.write(data))),FieldShadowTasks.schema,256)
    }
    /** A narrow check for copied packet counts, not general semantic correctness. */
    fun reportProblems(f:Facts,answer:FieldShadowTasks.Explanation):List<String> = buildList {
        val all=listOf(answer.headline,answer.significance,answer.limitations,answer.passiveNextStep).joinToString(" ")
        val allowed=setOf(f.savedFrames,f.observedFrames,f.droppedFrames,f.deauthFrames,f.eapolFrames,f.malformedFrames)
        if(Regex("[0-9]+").findAll(all).any {it.value.toIntOrNull() !in allowed})
            add("The explanation introduced a number absent from the verified packet counts.")
        val expected="Hunter saved ${f.savedFrames} of ${f.observedFrames} observed frames and dropped ${f.droppedFrames}. " +
            "The saved window contains ${f.deauthFrames} deauthentication frames and ${f.eapolFrames} EAPOL frames."
        if(answer.significance!=expected)add("The verified packet-count sentence was omitted or changed.")
    }
    fun decode(json:String,f:Facts):FieldShadowTasks.Explanation = FieldShadowTasks.decode(json).also {
        require(reportProblems(f,it).isEmpty()) {"The packet explanation changed verified counts."}
    }

}
