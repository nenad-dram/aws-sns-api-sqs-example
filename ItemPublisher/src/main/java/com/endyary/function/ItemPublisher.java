package com.endyary.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

/**
 * Handler for requests to Lambda function.
 */
public class ItemPublisher implements RequestHandler<ItemCreated, String> {

  private final Gson gson;
  private final SnsClient snsClient;
  private final String topicArn;

  public ItemPublisher() {
    gson = new Gson();
    snsClient = SnsProvider.getSnsClient();
    topicArn = SnsProvider.getTopicArn();
  }

  @Override
  public String handleRequest(final ItemCreated item, final Context context) {
    String itemJson = gson.toJson(item);

    PublishRequest request = PublishRequest.builder()
        .message(itemJson)
        .topicArn(topicArn)
        .build();
    PublishResponse result = snsClient.publish(request);

    return "Message published with id " + result.messageId();
  }
}
