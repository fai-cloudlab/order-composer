package com.fujitsu.cloudlab.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

public class CassandraConfig extends AbstractCassandraConfiguration {

  @Value("${spring.data.cassandra.contact-points}")
  private String contactPoints;

  @Value("${spring.data.cassandra.port}")
  private int port;

  @Value("${spring.data.cassandra.keyspace-name}")
  private String keySpace;

  @Override
  protected String getKeyspaceName() {
    return keySpace;
  }

  @Override
  protected String getContactPoints() {
    return contactPoints;
  }

  @Override
  protected int getPort() {
    return port;
  }
}
