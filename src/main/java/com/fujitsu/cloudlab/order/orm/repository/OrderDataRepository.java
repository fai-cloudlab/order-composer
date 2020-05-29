package com.fujitsu.cloudlab.order.orm.repository;

import com.fujitsu.cloudlab.order.orm.model.entity.OrderData;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDataRepository extends CassandraRepository<OrderData, String> {
  @Query(
      "select * from order_db.order_data where order_id = ?0 and order_status = ?1 ALLOW FILTERING")
  Optional<OrderData> findOrderByOrderIdAndOrderStatus(String id, String status);
}
