package com.endyary.function;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class SnsProvider {

  public static final String ENV_TOPIC_ARN = "ITEM_TOPIC_ARN";

  private SnsProvider() {
  }

  public static SnsClient getSnsClient() {
    return SnsClient.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
        .build();
  }

  public static String getTopicArn() {
    return System.getenv(ENV_TOPIC_ARN);
  }
}
