package com.endyary.function;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class ItemSubscriber implements RequestHandler<SNSEvent, Void> {

  private final Gson gson;

  public ItemSubscriber() {
    gson = new Gson();
  }

  public Void handleRequest(final SNSEvent snsEvent, final Context context) {
    LambdaLogger logger = context.getLogger();
    snsEvent.getRecords().forEach(snsRecord -> {
      Item item = gson.fromJson(snsRecord.getSNS().getMessage(), Item.class);
      item.setId(UUID.randomUUID().toString());
        logger.log("Received: "+ gson.toJson(item));
        });

    return null;
  }

}
