package com.flowlinx.fix.server.type;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;

import java.util.Set;

@Getter
public enum CustomerType {

   ASSET_MANAGER(500, "Asset Manager"),
   GLOBAL_BANK(400, "Global Bank"),
   INTERNATIONAL_BROKERAGE(300, "International Brokerage"),
   REGIONAL_BANK(200, "Regional Bank"),
   LOCAL_BROKER(100, "Local Brokerage");

   private Integer code;
   private String name;

   public static final Set<CustomerType> ALL = ImmutableSet.of(
      CustomerType.ASSET_MANAGER,
      CustomerType.GLOBAL_BANK,
      CustomerType.INTERNATIONAL_BROKERAGE,
      CustomerType.REGIONAL_BANK,
      CustomerType.LOCAL_BROKER);

   public static final Set<CustomerType> BROKERS_SET = ImmutableSet.of(
      CustomerType.INTERNATIONAL_BROKERAGE,
      CustomerType.REGIONAL_BANK,
      CustomerType.LOCAL_BROKER);

   public static final Set<CustomerType> AM_SET = ImmutableSet.of(
      CustomerType.ASSET_MANAGER);

   public static final Set<CustomerType> ANONYMOUS_SET = ImmutableSet.of(
      CustomerType.ASSET_MANAGER,
      CustomerType.GLOBAL_BANK);


   CustomerType(Integer code, String name) {
      this.code = code;
      this.name = name;
   }

}
