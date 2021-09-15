package com.flowlinx.fix.server.integration.dto;

import com.flowlinx.fix.server.type.Authority;
import com.flowlinx.fix.server.type.CustomerType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class UserDto implements Serializable{

   private static final long serialVersionUID = 7119688623108049876L;
   private Long id;
   private CustomerType type;
   private String email;
   private String password;
   private String token;
   private String firstName;
   private String lastName;
   private String phone;
   private String countryCode;
   private boolean active;
   private List<String> products;
   private String state;
   private String company;
   private Set<Authority> authorities;

}
