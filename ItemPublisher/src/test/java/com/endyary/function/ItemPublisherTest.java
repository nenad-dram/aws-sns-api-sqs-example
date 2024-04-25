package com.endyary.function;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;

@ExtendWith(MockitoExtension.class)
@Testcontainers
class ItemPublisherTest {

  private static SnsClient snsClient;
  private static String topicArn;
  @Mock
  private Context context;

  @Container
  static LocalStackContainer localStack = new LocalStackContainer(
      DockerImageName.parse("localstack/localstack:latest")

  );

  @BeforeAll
  static void init() {
    snsClient = SnsClient.builder()
        .endpointOverride(localStack.getEndpoint())
        .build();

    CreateTopicResponse topicResponse =
        snsClient.createTopic(CreateTopicRequest.builder().name("ItemsTopic").build());
    topicArn = topicResponse.topicArn();
  }


  @Test
  void shouldPublishMessage() {
    try (MockedStatic<SnsProvider> snsClientMocked = mockStatic(SnsProvider.class)) {
      snsClientMocked.when(SnsProvider::getSnsClient).thenReturn(snsClient);
      snsClientMocked.when(SnsProvider::getTopicArn).thenReturn(topicArn);

      ItemCreated item = new ItemCreated();
      item.setName("FirstItem");
      item.setValue("FirstValue");

      ItemPublisher itemPublisher = new ItemPublisher();
      String response = itemPublisher.handleRequest(item, context);

      assertTrue(response.contains("Message published with id"));
    }
  }
}
