package com.fujitsu.cloudlab.order.service;

import com.fujitsu.cloudlab.commons.constants.AppConstants;
import com.fujitsu.cloudlab.commons.exception.ApiException;
import com.fujitsu.cloudlab.commons.util.ResponseUtil;
import com.fujitsu.cloudlab.offer.json.model.Offer;
import com.fujitsu.cloudlab.offer.json.model.OffersList;
import com.fujitsu.cloudlab.order.json.model.Cash;
import com.fujitsu.cloudlab.order.json.model.OrderInput;
import com.fujitsu.cloudlab.order.json.model.OrderResponse;
import com.fujitsu.cloudlab.order.json.model.Price;
import com.fujitsu.cloudlab.order.json.model.Product;
import com.fujitsu.cloudlab.order.orm.model.entity.OrderData;
import com.fujitsu.cloudlab.order.orm.repository.OrderDataRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderComposerService {

  @Autowired private OrderDataRepository retailOrderDataRepository;
  @Autowired private RestTemplate restTemplate;

  @Value(value = "${offer.reader.url}")
  private String offerReaderUrl;

  @Autowired private Gson gson;

  public OrderResponse composeOrder(OrderInput orderInput, HttpHeaders headers)
      throws ApiException {
    OffersList offersList = retrieveOffer(orderInput.getSelectedOffers().get(0).getOfferId(), headers);
    
    if(!(offersList != null))
    	throw new ApiException(AppConstants.OFR404, "Offer not found", "Offer not found in DB", null);
	
	 //if(orderInput.getFormOfPayment().getPrice().getValue() !=  offersList.getOffers().get(0).getProduct().getProductPrice().getValue()) 
		 //throw new ApiException(AppConstants.OFR404, "Order price mismatch", "Order price value is not matching with Offer price", null);
    
    Date creationTs = new Date();

    OrderResponse order = createOrder(orderInput, offersList);
    order.setOrderCreationUtcTs(creationTs.toString());
    

    OrderData orderData = new OrderData();
    orderData.setOrderId(order.getOrderId());
    orderData.setOrderStatus(AppConstants.CREATED);
    
    orderData.setOrderData(gson.toJson(order));
    Offer offer = offersList.getOffers().get(0);
    orderData.setCurrencyCode(offer.getOfferPrice().getCurrency());
    orderData.setOrderTotalPrice(offer.getOfferPrice().getValue().toString());
    //orderData.setProductCategoryId(Arrays.asList(offer.getProductList().get(0).getProductCode()));

    orderData.setTransactionId(headers.getFirst(AppConstants.TRANSACTION_ID));
    // retailOrderData.setOrderCreationUtcTs(new SimpleDateFormat("EEEE, MMM dd HH:mm:ss
    // yyyy").parse(order.getOrderCreationUtcTs()));
    orderData.setOrderCreationUtcTs(creationTs);
    
    retailOrderDataRepository.save(orderData);
    return order;
  }

  private OrderResponse createOrder(OrderInput orderInput, OffersList offersList) {
    OrderResponse order = new OrderResponse();
    order.setOrderId(UUID.randomUUID().toString());
    order.setCustomer(orderInput.getCustomer());
    order.setOrderStatus(AppConstants.CREATED);
    Offer offer = offersList.getOffers().get(0);
    order.setProducts(mapProducts(Arrays.asList(offer.getProduct())));
    Price price = new Price();
    price.setCurrency(offer.getOfferPrice().getCurrency());
    price.setValue(offer.getOfferPrice().getValue());
    order.setFormOfPayment(orderInput.getFormOfPayment());
    order.getFormOfPayment().setPaymentReferenceId(UUID.randomUUID().toString().substring(0, 15));
    if(orderInput.getFormOfPayment().getPaymentMethod().getPaymentMethodType().equals("Cash")) {
    	Cash cash = new Cash();
    	cash.setCashReceiptId("CASH"+UUID.randomUUID().toString().substring(0, 5));
    	cash.setTerminalId("ID"+UUID.randomUUID().toString().substring(0, 1));
    	order.getFormOfPayment().getPaymentMethod().setCash(cash);
    	order.getFormOfPayment().getPaymentMethod().setPaymentCard(null);
    }else {
    	order.getFormOfPayment().getPaymentMethod().setCash(null);
    }
    order.setProducts(mapProducts(Arrays.asList(offer.getProduct())));
    return order;
  }

  private List<Product> mapProducts(List<com.fujitsu.cloudlab.offer.json.model.Product> productList) {
	  List<Product> prodcuts= new ArrayList<>();;
	  productList.forEach(
			  product -> {
				  Product p = new Product();
				  p.setProductCode(product.getProductCode());
				  p.setProductDescription(product.getProductDescription());
				  p.setProductPrice(mapPrice(product.getProductPrice()));
				  prodcuts.add(p);
			  }
			  );
	return prodcuts;
}

private Price mapPrice(com.fujitsu.cloudlab.offer.json.model.@Valid Price productPrice) {
	Price price = new Price();
	price.setCurrency(productPrice.getCurrency());
	price.setValue(productPrice.getValue());
	return price;
}

private OffersList retrieveOffer(final String offerId, HttpHeaders headers)
      throws ApiException {
    return ResponseUtil.process(
        OffersList.class,
        restTemplate.exchange(
            offerReaderUrl + offerId,
            HttpMethod.GET,
            new HttpEntity<String>(headers),
            String.class),
        "ORDER",
        "Offer Search");
  }
}
