package com.flowlinx.fix.server.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table( name = "dynamic_sessions")
@IdClass(DynamicSession.DynamicSessionPK.class)
public class DynamicSession {

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "heart_bt_int")
    private String heartBtInt;

    @Column(name = "valid_order_types")
    private String validOrderTypes;

    @Column(name = "use_data_dictionary")
    private String useDataDictionary;

    @Column(name = "default_market_price")
    private String defaultMarketPrice;

    @Column(name = "validate_user_defined_fields")
    private String validateUserDefinedFields;

    @Column(name = "validate_fields_out_of_order")
    private String validateFieldsOutOfOrder;

    @Column(name = "validate_fields_have_value")
    private String validateFieldsHaveValue;

    @Column(name = "persist_messages")
    private String persistMessages;

    @Column(name = "reset_on_logout")
    private String resetOnLogout;

    @Column(name = "reset_on_disconnect")
    private String resetOnDisconnect;

    @Id
    @Column(name = "begin_string")
    private String beginString;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "socket_connect_host")
    private String socketConnectHost;

    @Column(name = "socket_connect_port")
    private String socketConnectPort;

    @Column(name = "socket_accept_port")
    private String socketAcceptPort;

    @Id
    @Column(name = "sender_comp_id")
    private String senderCompID;

    @Id
    @Column(name = "target_comp_id")
    private String targetCompID;

    @Column(name = "data_dictionary")
    private String dataDictionary;

    @Data
    public static class DynamicSessionPK implements Serializable {
        private String beginString;
        private String senderCompID;
        private String targetCompID;

    }
}
