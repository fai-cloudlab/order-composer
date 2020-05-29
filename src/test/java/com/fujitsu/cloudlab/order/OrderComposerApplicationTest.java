package com.fujitsu.cloudlab.order;

import com.fujitsu.cloudlab.order.OrderComposerApplication;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OrderComposerApplicationTest.class)
public class OrderComposerApplicationTest {
  @InjectMocks public OrderComposerApplication orderComposerApplication;

  public void setup() {
    MockitoAnnotations.initMocks(OrderComposerApplication.class);
  }

  @Test
  public void when_MainServiceCalled_Expect_main() {
	  OrderComposerApplication.main(new String[] {});
  }
}
