package com.projectrevivesolutions.layertime.controller
import com.projectrevivesolutions.layertime.controller.metrics.*
import com.projectrevivesolutions.layertime.controller.ai.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Files
class KpiNonInterferenceTest {
 @Test fun failed_observer_does_not_change_ai_inputs_outputs_or_call_count()=runBlocking {
  val calls=mutableListOf<LocalModelRequest>()
  val manifest=LocalModelManifest("model","1","a".repeat(64),1,setOf(AiTask.EXPLAIN_EVIDENCE),setOf(ModelCapability.STRUCTURED_OUTPUT),1024,true)
  val provider=object:LocalModelProvider {
   override fun availability()=ModelAvailability(ModelState.READY,manifest)
   override suspend fun countInputTokens(request:LocalModelRequest):Int {calls.add(request);return 10}
   override suspend fun generate(request:LocalModelRequest):LocalModelResponse {calls.add(request);return LocalModelResponse("{}","model","1")}
  }
  val request=LocalModelRequest(AiTask.EXPLAIN_EVIDENCE,listOf(PromptMessage(PromptMessage.Role.USER,"same evidence")),"{}",16)
  val field=KpiTelemetry::class.java.getDeclaredField("recorder").apply {isAccessible=true}
  field.set(null,null)
  val baseline=AiCoordinator(provider).execute(request){it};val first=calls.toList();calls.clear()
  val badDir=Files.createTempFile("kpi-failure","tmp").toFile();val observer=KpiRecorder(badDir,"clock")
  try {
   field.set(null,observer)
   val measured=AiCoordinator(provider).execute(request){it}
   assertEquals(baseline,measured);assertEquals(first,calls);assertTrue(observer.awaitIdle());assertTrue((observer.health()["failed"] as Long)>0)
  } finally {field.set(null,null);observer.close()}
 }
}
