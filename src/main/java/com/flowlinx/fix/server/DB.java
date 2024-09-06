package com.flowlinx.fix.server;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;


public class DB {
    Connection connection= null;

    @Value("${datasource.url}")
    static String jdbcURL = "jdbc:postgresql://localhost:5432/quickfix?socketTimeout=30";

    @Value("${datasource.username}")
    static String username = "postgres";

    @Value("${datasource.password}")
    static String password = "postgres";

    public static Connection ConnectToDB(String jdbcURL, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            return connection;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getdeliverToCompID(String deliverToCompID) {
        String dtcid = null;
        try {
            Connection con = ConnectToDB(jdbcURL, username, password);

//		String sql = "SELECT * FROM public.brokersessionsmapping WHERE brokercode = '"+deliverToCompID+"'";
            String sql = "SELECT * FROM \"BrokerSessionsMapping\" WHERE \"BrokerCode\" = '"+deliverToCompID+"'";

            Statement statement;

            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                dtcid = rs.getString("BrokerCode");
                return dtcid;
            }
            con.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dtcid;

    }

//    public static String getInboundSequnceNumber(String beginString, String sender, String receiver) {
//        String inboundSeqNum;
//        try {
//            Connection con = ConnectToDB(jdbcURL, username, password);
//            String sql = "SELECT * FROM \"sessions\"";
//            Statement statement;
//
//            statement = con.createStatement();
//            ResultSet rs = statement.executeQuery(sql);
//            if(rs.next()){
//                inboundSeqNum = rs.getString("incoming_seqnum_edit");
//                System.out.println("Inbound sequence === "+inboundSeqNum);
//                return inboundSeqNum;
//            }
//            con.close();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return "";
//    }

    public static String getInboundSequenceNumber(String beginString, String sender, String receiver) {
        String inboundSeqNum = "";
        String sql = "SELECT \"incoming_seqnum_edit\" FROM \"sessions\" WHERE \"beginstring\" = ? AND \"sendercompid\" = ? AND \"targetcompid\" = ?";

        try (Connection con = ConnectToDB(jdbcURL, username, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, beginString);
            pstmt.setString(2, sender);
            pstmt.setString(3, receiver);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                inboundSeqNum = rs.getString("incoming_seqnum_edit");
                System.out.println("Inbound sequence === " + inboundSeqNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inboundSeqNum;
    }


//    public static String getOutboundSequnceNumber(String fixsession) {
//        String outboundSeqNum;
//        try {
//            Connection con = ConnectToDB(jdbcURL, username, password);
//            String sql = "SELECT * FROM \"fixsessionseq\" WHERE \"sessionname\" = '"+fixsession+"'";
//            Statement statement;
//
//            statement = con.createStatement();
//            ResultSet rs = statement.executeQuery(sql);
//            if(rs.next()){
//                outboundSeqNum = rs.getString("outbound");
//                System.out.println("Outbound sequence  === "+outboundSeqNum);
//                return outboundSeqNum;
//            }
//            con.close();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return "";
//    }

    public static String getOutboundSequnceNumber(String beginString, String sender, String receiver) {
        String outboundSeqNum = "";
        String sql = "SELECT \"outgoing_seqnum_edit\" FROM \"sessions\" WHERE \"beginstring\" = ? AND \"sendercompid\" = ? AND \"targetcompid\" = ?";

        try (Connection con = ConnectToDB(jdbcURL, username, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, beginString);
            pstmt.setString(2, sender);
            pstmt.setString(3, receiver);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                outboundSeqNum = rs.getString("outgoing_seqnum_edit");
                System.out.println("Outbound sequence === " + outboundSeqNum);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return outboundSeqNum;
    }


    public static boolean isSequenceNumberUpdated(String beginString, String sender, String receiver) {
        try (Connection con = ConnectToDB(jdbcURL, username, password)) {
            String sql = "SELECT \"is_incoming_edited\" FROM \"sessions\" WHERE \"beginstring\" = ? AND \"sendercompid\" = ? AND \"targetcompid\" = ?";

            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, beginString);
                pstmt.setString(2, sender);
                pstmt.setString(3, receiver);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    boolean isUpdated = rs.getBoolean("is_incoming_edited");
                    System.out.println("isUpdated value: " + isUpdated);
                    return isUpdated;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void setIsUpdatedToFalse(String beginString, String sender, String receiver) {
        String sql = "UPDATE \"sessions\" SET \"is_incoming_edited\" = false WHERE \"beginstring\" = ? AND \"sendercompid\" = ? AND \"targetcompid\" = ?";

        try (Connection con = ConnectToDB(jdbcURL, username, password);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, beginString);
            pstmt.setString(2, sender);
            pstmt.setString(3, receiver);

            int rs = pstmt.executeUpdate();
            System.out.print("done" + rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String getRouteToSession(String deliverToCompID) {
        String RouteToSession = null;
        try {

            Connection con = DriverManager.getConnection(jdbcURL, username, password);

//	        String sql = "SELECT * FROM \"ClientBrokerMappings\" WHERE \"DeliverToCompID\" = '"+deliverToCompID+"'";
            String sql = "SELECT * FROM \"BrokerSessionsMapping\" WHERE \"BrokerCode\" = '"+deliverToCompID+"'";

            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            if(rs.next()) {
                RouteToSession= rs.getString("Session");
                return RouteToSession;
            }
            con.close();

        } catch (SQLException e) {
            System.out.println("Error in connecting");
            e.printStackTrace();

        }
        return RouteToSession;
    }

    public static void updateSessionStatus(String sessionID,String status) {

        String s= sessionID.toString();
        String simpleSessionName = s.substring(s.indexOf(':')+1,s.indexOf("->"))+"."+s.substring(s.indexOf('>')+1,s.length());
        System.out.println("=======>"+simpleSessionName);

        try {

            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            String sql = "UPDATE fixsessionstatus SET status = '" + status + "', last_updated = NOW() WHERE fixsession = '" + simpleSessionName + "'";

            Statement statement = con.createStatement();

            int rs = statement.executeUpdate(sql);
            System.out.print(sql + " done"+rs);
            con.close();

        } catch (SQLException e) {
            System.out.println("Error in connecting");
            e.printStackTrace();

        }
    }

    public static String getRoutingTag(String senderCompID) {
        String routingTag = null;
        try {
            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            String sql = "SELECT routingtag FROM public.fixsession WHERE sendercompid = '"+senderCompID+"'";

            Statement statement;

            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if(rs.next()){
                routingTag = rs.getString("routingtag");
                return routingTag;
            }
            con.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return routingTag;
    }

    public static void createAllFilesFromDB() throws SQLException, IOException {
        Connection con = DriverManager.getConnection(jdbcURL, username, password);

        try {
            Files.deleteIfExists(
                    Paths.get("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/fix/fix-acceptor.cfg"));
            Files.deleteIfExists(
                    Paths.get("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/fix/fix-initiator.cfg"));
            System.out.println("Deletion successful.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File acceptorFile = new File("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/fix/fix-acceptor.cfg");
        FileWriter acceptorFw = new FileWriter(acceptorFile,true);

        BufferedWriter acceptorOut = new BufferedWriter(acceptorFw);

        File initiatorFile = new File("/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/fix/fix-initiator.cfg");
        FileWriter initiatorFw = new FileWriter(initiatorFile,true);

        BufferedWriter initiatorOut = new BufferedWriter(initiatorFw);

        //        String defaultConfig = "#default setting for session\n[default]\nFileLogPath=logs\nFileStorePath=log\nValidateFieldsOutOfOrder=N\nValidateFieldsHaveValues=N\nValidateUserDefinedFields=N\nValidateUnorderedGroupFields=N\nValidateIncomingMessage=N\nAllowUnknownMsgFields=Y\nFileIncludeTimeStampForMessages=Y\n";
        String defaultAcceptorConfig = "[default]\n" +
                "ConnectionType=acceptor\n" +
                "StartTime=00:00:00\n" +
                "EndTime=00:00:00\n" +
                "HeartBtInt=30\n" +
                "ValidOrderTypes=1,2,F\n" +
                "UseDataDictionary=Y\n" +
                "DefaultMarketPrice=12.30\n" +
                "ValidateUserDefinedFields=N\n" +
                "ValidateFieldsOutOfOrder=N\n" +
                "ValidateFieldsHaveValue=N\n" +
                "AllowUnknownMsgFields=Y\n" +
                "PersistMessages=Y\n\n";
        defaultAcceptorConfig += "#Storage and logging\n" +
                "JdbcDriver=org.postgresql.Driver\n" +
                "JdbcURL=jdbc:postgresql://localhost:5432/quickfix?socketTimeout=30\n" +
                "JdbcUser=postgres\n" +
                "JdbcPassword=postgres\n" +
                "JdbcLogHeartBeats=N\n" +
                "JdbcStoreMessagesTableName=messages\n" +
                "JdbcStoreSessionsTableName=sessions\n" +
                "JdbcLogEventTable=log_events\n" +
                "JdbcLogIncomingTable=log_income_messages\n" +
                "JdbcLogOutgoingTable=log_outgoing_messages";
        acceptorOut.write(defaultAcceptorConfig);


        String defaultInitiatorConfig = "[default]\n" +
                "StartTime=00:00:00\n" +
                "EndTime=00:00:00\n" +
                "HeartBtInt=40\n" +
                "ValidOrderTypes=1,2,F\n" +
                "UseDataDictionary=Y\n" +
                "DefaultMarketPrice=12.30\n" +
                "ValidateUserDefinedFields=N\n" +
                "ValidateFieldsOutOfOrder=N\n" +
                "ValidateFieldsHaveValue=N\n" +
                "AllowUnknownMsgFields=Y\n" +
                "PersistMessages=Y\n" +
                "\n";
//                "ResetOnLogout=Y\n" +
//                "ResetOnDisconnect=Y\n\n";

        defaultInitiatorConfig += "#Storage and logging\n" +
                "JdbcDriver=org.postgresql.Driver\n" +
                "JdbcURL=jdbc:postgresql://localhost:5432/quickfix?socketTimeout=30\n" +
                "JdbcUser=postgres\n" +
                "JdbcPassword=postgres\n" +
                "JdbcLogHeartBeats=N\n" +
                "JdbcStoreMessagesTableName=messages\n" +
                "JdbcStoreSessionsTableName=sessions\n" +
                "JdbcLogEventTable=log_events\n" +
                "JdbcLogIncomingTable=log_income_messages\n" +
                "JdbcLogOutgoingTable=log_outgoing_messages";

        initiatorOut.write(defaultInitiatorConfig);

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.fixsession");
            while (rs.next()) {
                // Position the cursor
                System.out.println("[SESSION]");

                String SenderCompID = rs.getString("SenderCompID");
                String SenderSubID = rs.getString("SenderSubID");// Retrieve only the first column value
                String SenderLocationID = rs.getString("SenderLocationID");
                String TargetCompID= rs.getString("TargetCompID");
                String TargetSubID = rs.getString("TargetSubID");
                String TargetLocationID = rs.getString("TargetLocationID");
//                String fixuser = rs.getString("fixuser");
//                String fixpassword = rs.getString("fixpassword");
                String ConnectionType = rs.getString("connectiontype");
                String ipaddress = rs.getString("ipaddress");
                String port = rs.getString("port");
                String BeginString = rs.getString("fixversion");
                String StartTime = rs.getString("starttime");
                String EndTime = rs.getString("endtime");
                String HeartBtInt = rs.getString("heartbeatinterval");
                String appicationType = rs.getString("applicationtype");
                String firmname = rs.getString("firmname");
                String DataDictionary = "/opt/sosuv/repositories/flowlinx-fix-server/src/main/resources/fix/" + rs.getString("datadictionary");
                Boolean Normalization = rs.getBoolean("normalization");
                String NormalizationClients = rs.getString("normalization_clients");
                Boolean UseDataDictionary = rs.getBoolean("usedatadictionary");
                Boolean ValidateUserDefinedFields = rs.getBoolean("validateuserdefinedfields");
                Boolean ValidateFieldsOutOfOrder = rs.getBoolean("validatefieldsoutoforder");
                Boolean ValidateFieldsHaveValue = rs.getBoolean("validatefieldshavevalue");
                Boolean PersistMessages = rs.getBoolean("persistmessages");
                Boolean ResetOnLogout = rs.getBoolean("resetonlogout");
                Boolean ResetOnDisconnect = rs.getBoolean("resetondisconnect");

                System.out.println("sender sub id++++++++++++++++++ "+ SenderSubID+" "+TargetSubID);

//                File configfile = createSeparateConfigFile(SenderCompID,TargetCompID);

                try {
                    if (ConnectionType.equalsIgnoreCase("acceptor")) {
                        acceptorOut.write("\n\n" + "[SESSION]");

                        if(SenderCompID!=null && !SenderCompID.equals(""))
                            acceptorOut.write("\n" + "SenderCompID=" + SenderCompID);
                        if(SenderSubID!=null && !SenderSubID.equals(""))
                            acceptorOut.write("\n" + "SenderSubID=" + SenderSubID);
                        if(SenderLocationID!=null && !SenderLocationID.equals(""))
                            acceptorOut.write("\n" + "SenderLocationID=" + SenderLocationID);
                        if(TargetCompID!=null && !TargetCompID.equals(""))
                            acceptorOut.write("\n" + "TargetCompID=" + TargetCompID);
                        if(TargetSubID!=null && !TargetSubID.equals(""))
                            acceptorOut.write("\n" + "TargetSubID=" + TargetSubID);
                        if(TargetLocationID!=null && !TargetLocationID.equals(""))
                            acceptorOut.write("\n" + "TargetLocationID=" + TargetLocationID);
                        if(BeginString!=null && !BeginString.equals(""))
                            acceptorOut.write("\n" + "BeginString=" + BeginString);
                        if(StartTime!=null && !StartTime.isEmpty())
                            acceptorOut.write("\n" + "StartTime=" + StartTime);
                        if(EndTime!=null && !EndTime.isEmpty())
                            acceptorOut.write("\n" + "EndTime=" + EndTime);
                        if(HeartBtInt!=null && !HeartBtInt.isEmpty())
                            acceptorOut.write("\n" + "HeartBtInt=" + HeartBtInt);
                        if(appicationType!=null && !appicationType.isEmpty())
                            acceptorOut.write("\n" + "applicationType=" + appicationType);
                        if(firmname!=null && !firmname.isEmpty())
                            acceptorOut.write("\n" + "firmname=" + firmname);

                        if(rs.getString("datadictionary")!=null && !rs.getString("datadictionary").isEmpty())
                            acceptorOut.write("\n" + "DataDictionary="+DataDictionary);
                        if(ConnectionType!=null && !ConnectionType.isEmpty())
                            acceptorOut.write("\n" + "ConnectionType=" + ConnectionType.toLowerCase());
                        acceptorOut.write("\n" + "Normalization=" + (Normalization ? "Y" : "N"));
                        if(!NormalizationClients.isEmpty()) {
                            acceptorOut.write("\n" + "NormalizationClientIds=" + NormalizationClients);
                        }
                        acceptorOut.write("\n" + "SocketAcceptPort=" + port);

                        if(UseDataDictionary) {
                            acceptorOut.write("\n" + "UseDataDictionary=Y");
                        } else {
                            acceptorOut.write("\n" + "UseDataDictionary=N");
                        }

                        if(ValidateUserDefinedFields) {
                            acceptorOut.write("\n" + "ValidateUserDefinedFields=Y");
                        } else {
                            acceptorOut.write("\n" + "ValidateUserDefinedFields=N");
                        }

                        if(ValidateFieldsOutOfOrder) {
                            acceptorOut.write("\n" + "ValidateFieldsOutOfOrder=Y");
                        } else {
                            acceptorOut.write("\n" + "ValidateFieldsOutOfOrder=N");
                        }

                        if(ValidateFieldsHaveValue) {
                            acceptorOut.write("\n" + "ValidateFieldsHaveValue=Y");
                        } else {
                            acceptorOut.write("\n" + "ValidateFieldsHaveValue=N");
                        }

                        if(PersistMessages) {
                            acceptorOut.write("\n" + "PersistMessages=Y");
                        } else {
                            acceptorOut.write("\n" + "PersistMessages=N");
                        }

                        if(ResetOnLogout) {
                            acceptorOut.write("\n" + "ResetOnLogout=Y");
                        } else {
                            acceptorOut.write("\n" + "ResetOnLogout=N");
                        }

                        if(ResetOnDisconnect) {
                            acceptorOut.write("\n" + "ResetOnDisconnect=Y");
                        } else {
                            acceptorOut.write("\n" + "ResetOnDisconnect=N");
                        }

                        acceptorOut.write("\n");

                    } else if (ConnectionType.equalsIgnoreCase("initiator")) {
                        initiatorOut.write("\n\n" + "[SESSION]");
                        if(SenderCompID!=null && !SenderCompID.isEmpty())
                            initiatorOut.write("\n" + "SenderCompID=" + SenderCompID);
                        if(SenderSubID!=null && !SenderSubID.isEmpty())
                            initiatorOut.write("\n" + "SenderSubID=" + SenderSubID);
                        if(SenderLocationID!=null && !SenderLocationID.isEmpty())
                            initiatorOut.write("\n" + "SenderLocationID=" + SenderLocationID);
                        if(TargetCompID!=null && !TargetCompID.isEmpty())
                            initiatorOut.write("\n" + "TargetCompID=" + TargetCompID);
                        if(TargetSubID!=null && !TargetSubID.isEmpty())
                            initiatorOut.write("\n" + "TargetSubID=" + TargetSubID);
                        if(TargetLocationID!=null && !TargetLocationID.isEmpty())
                            initiatorOut.write("\n" + "TargetLocationID=" + TargetLocationID);
                        if(BeginString!=null && !BeginString.isEmpty())
                            initiatorOut.write("\n" + "BeginString=" + BeginString);
                        if(StartTime!=null && !StartTime.isEmpty())
                            initiatorOut.write("\n" + "StartTime=" + StartTime);
                        if(EndTime!=null && !EndTime.isEmpty())
                            initiatorOut.write("\n" + "EndTime=" + EndTime);
                        if(HeartBtInt!=null && !HeartBtInt.isEmpty())
                            initiatorOut.write("\n" + "HeartBtInt=" + HeartBtInt);
                        if(appicationType!=null && !appicationType.isEmpty())
                            initiatorOut.write("\n" + "applicationType=" + appicationType);
                        if(firmname!=null && !firmname.isEmpty())
                            initiatorOut.write("\n" + "firmname=" + firmname);

                        if(rs.getString("datadictionary")!=null && !rs.getString("datadictionary").isEmpty())
                            initiatorOut.write("\n" + "DataDictionary="+DataDictionary);
                        if(ConnectionType!=null && !ConnectionType.isEmpty())
                            initiatorOut.write("\n" + "ConnectionType=" + ConnectionType.toLowerCase());
                        initiatorOut.write("\n" + "Normalization=" + (Normalization ? "Y" : "N"));
                        if(!NormalizationClients.isEmpty()) {
                            initiatorOut.write("\n" + "NormalizationClientIds=" + NormalizationClients);
                        }
                        if(ipaddress!=null && !ipaddress.isEmpty())
                            initiatorOut.write("\n" + "SocketConnectHost=" + ipaddress);
                        if(port!=null && !port.isEmpty())
                            initiatorOut.write("\n" + "SocketConnectPort=" + port);


                        if(UseDataDictionary) {
                            initiatorOut.write("\n" + "UseDataDictionary=Y");
                        } else {
                            initiatorOut.write("\n" + "UseDataDictionary=N");
                        }

                        if(ValidateUserDefinedFields) {
                            initiatorOut.write("\n" + "ValidateUserDefinedFields=Y");
                        } else {
                            initiatorOut.write("\n" + "ValidateUserDefinedFields=N");
                        }

                        if(ValidateFieldsOutOfOrder) {
                            initiatorOut.write("\n" + "ValidateFieldsOutOfOrder=Y");
                        } else {
                            initiatorOut.write("\n" + "ValidateFieldsOutOfOrder=N");
                        }

                        if(ValidateFieldsHaveValue) {
                            initiatorOut.write("\n" + "ValidateFieldsHaveValue=Y");
                        } else {
                            initiatorOut.write("\n" + "ValidateFieldsHaveValue=N");
                        }

                        if(PersistMessages) {
                            initiatorOut.write("\n" + "PersistMessages=Y");
                        } else {
                            initiatorOut.write("\n" + "PersistMessages=N");
                        }

                        if(ResetOnLogout) {
                            initiatorOut.write("\n" + "ResetOnLogout=Y");
                        } else {
                            initiatorOut.write("\n" + "ResetOnLogout=N");
                        }

                        if(ResetOnDisconnect) {
                            initiatorOut.write("\n" + "ResetOnDisconnect=Y");
                        } else {
                            initiatorOut.write("\n" + "ResetOnDisconnect=N");
                        }

                        initiatorOut.write("\n");
                    }


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            acceptorOut.flush();
            acceptorOut.close();
            initiatorOut.flush();
            initiatorOut.close();
            rs.close();
        } finally {
            con.close();
            System.out.println("db connection closed");
        }
    }



    public static void main(String[] args) {
        //
        String DBRouteSession = DB.getdeliverToCompID("RAJ");
        System.out.println("DBRouteSession   "+DBRouteSession);

    }
}
