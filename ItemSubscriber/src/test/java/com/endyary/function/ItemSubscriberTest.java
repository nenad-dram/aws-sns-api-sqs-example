package com.endyary.function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.google.gson.Gson;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemSubscriberTest {

    @Mock
    private SNSEvent snsEvent;
    @Mock
    private Context context;
    @Mock
    private LambdaLogger logger;
    @Mock private SNSEvent.SNSRecord record;
    @Mock
    private SNS sns;

    @Test
    void shouldLogMessage() {
        Item item = new Item();
        item.setName("itemName");
        item.setValue("itemValue");

        when(sns.getMessage()).thenReturn(new Gson().toJson(item));
        when(record.getSNS()).thenReturn(sns);
        when(snsEvent.getRecords()).thenReturn(List.of(record));
        when(context.getLogger()).thenReturn(logger);

        ItemSubscriber itemSubscriber = new ItemSubscriber();
        Void response = itemSubscriber.handleRequest(snsEvent,context);

        assertNull(response);
    }

}
