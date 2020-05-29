package com.fujitsu.cloudlab.order.api;


import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fujitsu.cloudlab.commons.constants.AppConstants;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.order.json.model.OrderInput;
import com.fujitsu.cloudlab.order.json.model.OrderResponse;
import com.fujitsu.cloudlab.order.service.OrderComposerService;
import com.fujitsu.cloudlab.order.service.OrderDeleteService;

public class OrderComposerControllerTest {

  @InjectMocks private OrderComposerController orderComposerController;
  @Mock private RestTemplate restTemplate;
  @Mock private OrderComposerService orderComposerService;
  @Mock private OrderDeleteService orderDeleteService;
  private OrderInput orderInput;
  private OrderResponse orderResponse;
  private HttpHeaders headers = new HttpHeaders();
  
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    orderResponse = new OrderResponse();
    orderInput = new OrderInput();
  }

  @Test
  public void when_ValidOrderRequest_Expect_createOrder() throws ApiException {
    Mockito.when(
            this.orderComposerService.composeOrder(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(orderResponse);

    Assert.assertNotNull(
        this.orderComposerController.composeOrder(headers, orderInput));
    
  }

  @Test(expected = ApiException.class)
  public void when_inValidOrderRequest_Expect_Exception_createOrder() throws ApiException {
    Mockito.when(
            this.orderComposerService.composeOrder(
                Mockito.any(),
                Mockito.any()))
        .thenThrow(
            new ApiException(
                AppConstants.ORD404, AppConstants.ORD404, null, null));
    orderComposerController.composeOrder(
        headers, orderInput);
  }

  @Test
  public void when_ValidOrderId_Expect_deleteOrder() throws ApiException {
    Mockito.when(
    		orderDeleteService.deleteOrder(
                Mockito.any()))
        .thenReturn(Boolean.TRUE);

    ResponseEntity<Void> responseEntity = orderComposerController.deleteOrder( headers, "orderId");
    
    Assert.assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  @Test(expected = ApiException.class)
  public void when_inValidOrderId_Expect_Exception_getOrder() throws ApiException {
	  Mockito.when(
	    		orderDeleteService.deleteOrder(
	                Mockito.any()))
	         .thenThrow(
            new ApiException(
                AppConstants.ORD404, AppConstants.ORD404, null, null));
	  orderComposerController.deleteOrder( headers, "orderId");
  }
  
}