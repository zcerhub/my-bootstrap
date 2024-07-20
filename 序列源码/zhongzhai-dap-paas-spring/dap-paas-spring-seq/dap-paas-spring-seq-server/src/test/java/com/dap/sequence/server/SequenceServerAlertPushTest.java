package com.dap.sequence.server;

import com.dap.sequence.server.dto.AlertEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

import static com.dap.sequence.server.rules.NumberRuleHandler.postAlertMsg;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SequenceServerAlertPushTest {
    @Test
    public void pushAlert() {
        Future<ResponseEntity<Map>> responseEntityFuture = postAlertMsg(AlertEntity.builder().name("序列生产超过告警阈值").severity(2).description("errorMessage").type("event").build());
        while (!responseEntityFuture.isDone()) ;
        try {
            assertEquals(1, Objects.requireNonNull(responseEntityFuture.get().getBody()).get("code"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
