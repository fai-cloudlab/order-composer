package com.fujitsu.cloudlab.order.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SwaggerConfigTest {
  @InjectMocks private SwaggerConfig swaggerConfig;

  @Before
  public void setup() {
    swaggerConfig = new SwaggerConfig();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void swaggerConfig() {
    assertNotNull(swaggerConfig.customImplementation());
  }
}
