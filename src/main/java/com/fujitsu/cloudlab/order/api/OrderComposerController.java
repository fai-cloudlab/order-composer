package com.fujitsu.cloudlab.order.api;

import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.order.json.model.OrderInput;
import com.fujitsu.cloudlab.order.json.model.OrderResponse;
import com.fujitsu.cloudlab.order.service.OrderComposerService;
import com.fujitsu.cloudlab.order.service.OrderDeleteService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@Api(tags = {"orderComposer"})
@SwaggerDefinition(tags = {@Tag(name = "orderComposer", description = "order Composer API")})
@Slf4j
public class OrderComposerController {

  @Autowired private OrderComposerService orderComposerService;
  @Autowired private OrderDeleteService orderDeleteService;
  @Autowired Gson gson;

  @ApiOperation(value = "This feature enables create an Order.", nickname = "orderCreate")
  @PostMapping(
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public OrderResponse composeOrder(
      @RequestHeader HttpHeaders headers, @RequestBody OrderInput orderInput) throws ApiException {
    log.info("composeOrder");
    return orderComposerService.composeOrder(orderInput, headers);
  }

  @ApiOperation(value = "Delete orders based on OrderId", nickname = "deleteOrder")
  @DeleteMapping(
      value = "/{orderId}",
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> deleteOrder(
      @RequestHeader HttpHeaders headers, final @PathVariable String orderId) throws ApiException {
    log.info("deleteOrder");
    orderDeleteService.deleteOrder(orderId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
