package com.fujitsu.cloudlab.order.service;

import com.fujitsu.cloudlab.commons.constants.AppConstants;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.order.json.model.OrderResponse;
import com.fujitsu.cloudlab.order.orm.model.entity.OrderData;
import com.fujitsu.cloudlab.order.orm.repository.OrderDataRepository;
import com.google.gson.Gson;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDeleteService {

  @Autowired private OrderDataRepository retailOrderDataRepository;
  private static Gson gson = new Gson();

  public boolean deleteOrder(final String orderid) throws ApiException {
    Optional<OrderData> order = retrieveOrder(orderid, AppConstants.CREATED);
    if (order.isPresent()) {
      OrderData retailOrderData = order.get();
      retailOrderData.setOrderStatus(AppConstants.DELETED);
      OrderResponse orderResposne =
          gson.fromJson(retailOrderData.getOrderData(), OrderResponse.class);
      orderResposne.setOrderStatus(AppConstants.DELETED);

      retailOrderData.setOrderData(gson.toJson(orderResposne));
      retailOrderData.setOrderDeletedUtcTs(new Date());
      retailOrderDataRepository.save(retailOrderData);

      return true;
    } else
      throw new ApiException(AppConstants.ORD404, "Order not found", "Order not found in DB", null);
  }

  private Optional<OrderData> retrieveOrder(String orderid, String status) {
    return retailOrderDataRepository.findOrderByOrderIdAndOrderStatus(orderid, status);
  }
}
