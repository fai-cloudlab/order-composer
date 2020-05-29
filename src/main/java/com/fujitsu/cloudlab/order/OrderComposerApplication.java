package com.fujitsu.cloudlab.order;

import com.fujitsu.cloudlab.commons.exception.ApiErrorHandler;
import com.fujitsu.cloudlab.commons.http.RestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration({RestConfig.class, ApiErrorHandler.class})
public class OrderComposerApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderComposerApplication.class, args);
  }
}
